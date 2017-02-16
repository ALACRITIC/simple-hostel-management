package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.CreateReservationForm;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private static final String TOKEN_NAME = "reservation";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SharedResourceService sharedResourceService;


    /**
     * Display lasts reservation
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATION_LASTS, method = RequestMethod.GET)
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
     * Display lasts reservation
     *
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATION_NEXT_CHECKOUTS, method = RequestMethod.GET)
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

    @RequestMapping(value = Mappings.RESERVATION_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        Mappings.includeMappings(model);

        // name of template
        return Templates.RESERVATION_CALENDAR;
    }

    /**
     * Show reservation form
     *
     * @param request
     * @param reservationForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESERVATION_FORM)
    public String showForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long reservationId,
            HttpServletRequest request,
            CreateReservationForm reservationForm,
            Model model) throws Exception {

        if (reservationId != -1) {
            Reservation res = reservationService.getById(reservationId);
            reservationForm.load(res);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);
        tokenman.addToken(model);

        model.addAttribute("errorMessage", "");
        model.addAttribute("sharedResources", sharedResourceService.getAll());

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return Templates.RESERVATION_FORM;
    }

    /**
     * Delete reservation
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESERVATION_DELETE)
    public String deleteReservation(
            @RequestParam(value = "id", required = true) Long reservationId,
            @RequestParam(value = "token", required = true) String token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";

        // token is invalid
        if (tokenman.isTokenValid(session, Long.valueOf(token)) == false) {
            errorMessage = "Invalid session";
        }

        // token is valid
        else {

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            sharedResourceService.deleteById(reservationId);

            // TODO: delete
        }

        System.out.println("reservationId : " + reservationId);
        System.out.println("errorMessage : " + errorMessage);

        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.RESERVATION_ACTION_COMPLETED;
    }

    @PostMapping(Mappings.RESERVATION_FORM)
    public String submitReservation(
            @Valid CreateReservationForm reservationForm,
            BindingResult reservationResults,
            Model model,
            HttpServletRequest request) throws Exception {

        if (reservationResults.hasErrors()) {

            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", reservationForm.getToken());
            model.addAttribute("errorMessage", "");

            model.addAttribute("sharedResources", sharedResourceService.getAll(null));

            Mappings.includeMappings(model);
            return Templates.RESERVATION_FORM;
        }

        Customer customer = null;
        Reservation reservation = null;

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";

        // token is invalid
        if (tokenman.isTokenValid(session, reservationForm.getToken()) == false) {
            errorMessage = "Invalid session";
        }

        // token is valid
        else {

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            try {
                customer = customerService.create(
                        reservationForm.getCustomerFirstname(),
                        reservationForm.getCustomerLastname(),
                        reservationForm.getCustomerPhonenumber());

                Date beginDate = Utils.stringToDate(reservationForm.getBegin());
                Date endDate = Utils.stringToDate(reservationForm.getEnd());
                SharedResource res = sharedResourceService.getById(reservationForm.getSharedResourceId());
                int pl = reservationForm.getPlaces();
                reservation = reservationService.create(customer, res, pl, beginDate, endDate);

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.RESERVATION_ACTION_COMPLETED;
    }

    @RequestMapping(value = Mappings.RESERVATION_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Reservation> getReservations(
            @RequestParam(value = "start", required = true) String startDateStr,
            @RequestParam(value = "end", required = true) String endDateStr) throws Exception {

        Date startDate = Utils.stringToDate(startDateStr);
        Date endDate = Utils.stringToDate(endDateStr);

        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("Begin date is greater than end date: " + startDateStr + " / " + endDateStr);
        }

        List<Reservation> result = reservationService.getByInterval(startDate, endDate, true);
        return result;

    }

    @RequestMapping(value = Mappings.RESERVATION_JSON_GET_ALL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Reservation> getAllReservations() throws Exception {
        return reservationService.getAll();
    }

    @RequestMapping(value = Mappings.RESERVATION_ROOMS_AVAILABLE_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SharedResource> getAvailableRooms(
            @RequestParam(value = "start", required = true) String startDateStr,
            @RequestParam(value = "end", required = true) String endDateStr,
            @RequestParam(value = "places", required = false) int places) throws Exception {

        if (places < 1) {
            places = 1;
        }

        Date startDate = Utils.stringToDate(startDateStr);
        Date endDate = Utils.stringToDate(endDateStr);

        List<SharedResource> result = reservationService.getAvailableResources(Type.ROOM, startDate, endDate, places);

        return result;
    }


}