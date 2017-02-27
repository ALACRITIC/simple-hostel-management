package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationForm;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    public static final String TOKEN_ATTR_SESSION_PREFIX = "reservation-form";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AccommodationService accommodationService;

    /**
     * Display lasts reservation
     *
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATIONS_LASTS, method = RequestMethod.GET)
    public String showLastsReservation(Model model) {

        try {
            List<Reservation> lasts = reservationService.getLastReservations(20, 0);
            model.addAttribute("reservationsList", lasts);
        } catch (IOException e) {
            logger.error("Error while retrieving reservations", e);
        }

        Mappings.includeMappings(model);

        // name of template
        return Templates.RESERVATIONS_LAST;
    }

    /**
     * Display reservations by resource id
     *
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATIONS_BY_ACCOMMODATION, method = RequestMethod.GET)
    public String showByAccommodationId(
            @RequestParam(value = "id", required = true) Long accommodationId,
            Model model) throws IOException {

        List<Reservation> reservationsList = reservationService.getByAccommodationId(accommodationId, 30, 0);

        model.addAttribute("accommodation", accommodationService.getById(accommodationId));
        model.addAttribute("reservationsList", reservationsList);

        Mappings.includeMappings(model);
        return Templates.RESERVATIONS_BY_ACCOMMODATION;
    }

    /**
     * Display reservations by customer id
     *
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATIONS_BY_CUSTOMER, method = RequestMethod.GET)
    public String showByCustomer(
            @RequestParam(value = "id", required = true) Long customerId,
            Model model) throws IOException {

        List<Reservation> reservationsList = reservationService.getByCustomerId(customerId, null, 30, 0);

        model.addAttribute("customer", customerService.getById(customerId));
        model.addAttribute("reservationsList", reservationsList);

        Mappings.includeMappings(model);
        return Templates.RESERVATIONS_BY_CUSTOMER;
    }


    /**
     * Display lasts reservation
     *
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATIONS_NEXT_CHECKOUTS, method = RequestMethod.GET)
    public String showNextCheckouts(Model model) {

        try {
            List<Reservation> nextCheckouts = reservationService.getNextCheckouts(40, 0);
            model.addAttribute("checkoutsList", nextCheckouts);
        } catch (IOException e) {
            logger.error("Error while retrieving reservations", e);
        }

        Mappings.includeMappings(model);

        // name of template
        return Templates.RESERVATIONS_NEXT_CHECKOUTS;
    }

    @RequestMapping(value = Mappings.RESERVATIONS_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        Mappings.includeMappings(model);

        // name of template
        return Templates.RESERVATIONS_CALENDAR;
    }

    /**
     * Show reservation form
     *
     * @param request
     * @param reservationForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESERVATIONS_FORM)
    public String showForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long reservationId,
            @RequestParam(value = "date", required = false, defaultValue = "") String beginDate,
            @RequestParam(value = "accommodation", required = false, defaultValue = "-1") Long accommodationId,
            HttpServletRequest request,
            ReservationForm reservationForm,
            Model model) throws Exception {

        if (reservationId != -1) {
            Reservation res = reservationService.getById(reservationId);
            reservationForm.load(res);
            Accommodation acc = res.getAccommodation();
            model.addAttribute("primaryAccommodationId", acc.getId());
            model.addAttribute("primaryAccommodationName", acc.getName());
            model.addAttribute("primaryAccommodationPrice", acc.getPricePerDay());
        }

        if (accommodationId != -1) {
            Accommodation acc = accommodationService.getById(accommodationId);
            reservationForm.setAccommodationId(acc.getId());
            model.addAttribute("primaryAccommodationId", acc.getId());
            model.addAttribute("primaryAccommodationName", acc.getName());
            model.addAttribute("primaryAccommodationPrice", acc.getPricePerDay());
        }

        if (beginDate.isEmpty() == false) {
            String inFormat = "dd/MM/YYYY";
            String outFormat = "dd/MM/YYYY HH:mm";
            reservationForm.setBegin(Utils.stringToDateTime(beginDate, inFormat).plusHours(16)
                    .toString(outFormat));
            reservationForm.setEnd(Utils.stringToDateTime(beginDate, inFormat).plusDays(2)
                    .plusHours(10).toString(outFormat));
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);
        tokenman.addToken(model);

        model.addAttribute("accommodationsList", accommodationService.getAll());

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return Templates.RESERVATIONS_FORM;
    }

    @PostMapping(Mappings.RESERVATIONS_FORM)
    public String submitReservation(
            @Valid ReservationForm reservationForm,
            BindingResult reservationResults,
            Model model,
            HttpServletRequest request) throws Exception {

        String errorMessage = "";

        if (reservationResults.hasErrors()) {

            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", reservationForm.getToken());
            model.addAttribute("accommodationsList", accommodationService.getAll());

            if (reservationForm.getAccommodationId() != -1) {
                Reservation res = reservationService.getById(reservationForm.getAccommodationId());
                if (res == null) {
                    logger.error("Unknown accommodation: " + reservationForm.getAccommodationId());
                    errorMessage = "Invalid accommodation, please try again later.";
                } else {
                    model.addAttribute("primaryAccommodationId", res.getId());
                    model.addAttribute("primaryAccommodationName", res.getAccommodation().getName());
                }
            }

            model.addAttribute("errorMessage", errorMessage);

            Mappings.includeMappings(model);
            return Templates.RESERVATIONS_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);
        Reservation reservation = null;

        try {

            // check if token is invalid
            tokenman.throwIfTokenInvalid(session, reservationForm.getToken());

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            // parse dates
            Date beginDate = null;
            Date endDate = null;
            try {
                beginDate = Utils.stringToDateTime(reservationForm.getBegin(), "dd/MM/YYYY HH:mm").toDate();
                endDate = Utils.stringToDateTime(reservationForm.getEnd(), "dd/MM/YYYY HH:mm").toDate();
            } catch (Exception e) {
                logger.error("Invalid date: ", e);
                throw new IllegalStateException("Invalid dates: " + reservationForm.getBegin() + ", " + reservationForm.getEnd());
            }

            // add reservation if it is a new one
            if (reservationForm.getReservationId() == -1) {

                try {

                    Customer customer = customerService.getByPhonenumber(reservationForm.getCustomerPhonenumber());

                    // customer does not exit, create it
                    if (customer == null) {
                        customer = new Customer();
                        customer.setFirstname(reservationForm.getCustomerFirstname());
                        customer.setLastname(reservationForm.getCustomerLastname());
                        customer.setPhonenumber(reservationForm.getCustomerPhonenumber());
                        customerService.create(customer);
                    }

                    // customer exist, update customer information
                    else {
                        customer.setFirstname(reservationForm.getCustomerFirstname());
                        customer.setLastname(reservationForm.getCustomerLastname());
                        customer.setPhonenumber(reservationForm.getCustomerPhonenumber());
                        customerService.update(customer);
                    }

                    // get resource
                    int pl = reservationForm.getPlaces();
                    Accommodation accommodation = accommodationService.getById(reservationForm.getAccommodationId());

                    // create reservation
                    reservation = new Reservation(customer, accommodation, pl, beginDate, endDate);
                    reservation.setPaid(reservationForm.isPaid());
                    reservation.setTotalPrice(reservationForm.getTotalPrice());

                    reservationService.create(reservation);

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating reservation, please try again later.");
                }
            }

            // else update existing reservation
            else {

                Customer customer = null;
                Accommodation accommodation = null;
                try {
                    reservation = reservationService.getById(reservationForm.getReservationId());
                    customer = customerService.getById(reservationForm.getCustomerId());
                    accommodation = accommodationService.getById(reservationForm.getAccommodationId());
                } catch (Exception e) {
                    logger.error("Error while updating reservation", e);
                    throw new Exception("Error while updating, please try again later.");
                }

                if (accommodation == null || customer == null) {
                    logger.error("Invalid accommodation or customer: a/" + accommodation + " c/" + customer);
                    throw new IllegalStateException("Error, please try again later.");
                }

                // update customer
                customer.setFirstname(reservationForm.getCustomerFirstname());
                customer.setLastname(reservationForm.getCustomerLastname());
                customer.setPhonenumber(reservationForm.getCustomerPhonenumber());

                // update reservation
                reservation.setAccommodation(accommodation);
                reservation.setBegin(beginDate);
                reservation.setEnd(endDate);
                reservation.setCustomer(customer);
                reservation.setComment(reservationForm.getComment());
                reservation.setPlaces(reservationForm.getPlaces());
                reservation.setPaid(reservationForm.isPaid());
                reservation.setTotalPrice(reservationForm.getTotalPrice());

                try {

                    // update repository
                    reservationService.update(reservation);
                    customerService.update(customer);

                } catch (Exception e) {
                    logger.error("Error while updating reservation", e);
                    throw new Exception("Error while updating, please try again later.");
                }

            }

        } catch (Exception e) {
            logger.error("Error while validating form: ", e);
            errorMessage = e.getMessage();
        }

        model.addAttribute("formstate", "completed");
        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.RESERVATIONS_FORM;
    }

    /**
     * Delete reservation
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESERVATIONS_DELETE)
    public String deleteReservation(
            @RequestParam(value = "id", required = true) Long reservationId,
            @RequestParam(value = "token", required = true) Long token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";

        // check if token is invalid
        tokenman.throwIfTokenInvalid(session, token);

        // token is valid
        // always delete token before leave
        tokenman.deleteTokenFrom(session);

        reservationService.deleteById(reservationId);


        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        return Templates.RESERVATIONS_FORM;
    }

    @RequestMapping(value = Mappings.RESERVATIONS_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Reservation> getReservations(
            @RequestParam(value = "start", required = true) String startDateStr,
            @RequestParam(value = "end", required = true) String endDateStr,
            @RequestParam(value = "accommodation", required = false, defaultValue = "-1")
                    Long accommodationId) throws Exception {

        Date startDate = Utils.stringToDateTime(startDateStr, "dd/MM/YYYY HH:mm").toDate();
        Date endDate = Utils.stringToDateTime(endDateStr, "dd/MM/YYYYYY HH:mm").toDate();

        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("Begin date is greater than end date: " + startDateStr + " / " + endDateStr);
        }

        List<Reservation> result;
        if (accommodationId != -1) {
            result = reservationService.getByInterval(startDate, endDate, true, accommodationId);
        } else {
            result = reservationService.getByInterval(startDate, endDate, true);
        }
        return result;

    }

    @RequestMapping(value = Mappings.RESERVATIONS_JSON_SEARCH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Reservation> searchReservations(
            @RequestParam("customerId") Long customerId,
            @RequestParam(value = "paid", defaultValue = "null") Boolean paid) throws Exception {

        return reservationService.getByCustomerId(customerId, paid, 200, 0);

    }

    @RequestMapping(value = Mappings.RESERVATIONS_ACCOMMODATIONS_AVAILABLE_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Accommodation> getAvailableRooms(
            @RequestParam(value = "start", required = true) String startDateStr,
            @RequestParam(value = "end", required = true) String endDateStr,
            @RequestParam(value = "places", required = false) int places) throws Exception {

        if (places < 1) {
            places = 1;
        }

        // 24/02/2017 16:00
        Date startDate = Utils.stringToDateTime(startDateStr, "dd/MM/YYYY HH:mm").toDate();
        Date endDate = Utils.stringToDateTime(endDateStr, "dd/MM/YYYY HH:mm").toDate();

        List<Accommodation> result = reservationService.getAvailableAccommodations(Type.ROOM, startDate, endDate, places);

        return result;
    }

}