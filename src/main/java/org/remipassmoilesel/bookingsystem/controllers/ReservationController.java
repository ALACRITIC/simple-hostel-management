package org.remipassmoilesel.bookingsystem.controllers;

import org.remipassmoilesel.bookingsystem.Mappings;
import org.remipassmoilesel.bookingsystem.customers.Customer;
import org.remipassmoilesel.bookingsystem.customers.CustomerService;
import org.remipassmoilesel.bookingsystem.reservations.CreateReservationForm;
import org.remipassmoilesel.bookingsystem.reservations.Reservation;
import org.remipassmoilesel.bookingsystem.reservations.ReservationService;
import org.remipassmoilesel.bookingsystem.utils.FormErrorMap;
import org.remipassmoilesel.bookingsystem.utils.TokenManager;
import org.remipassmoilesel.bookingsystem.utils.Utils;
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


    /**
     * Display lasts reservation
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = Mappings.RESERVATION_LASTS, method = RequestMethod.GET)
    public String showLastsReservation(HttpServletRequest request, Model model) {

        try {
            List<Reservation> lasts = reservationService.getLasts(20, 0);
            model.addAttribute("reservationList", lasts);
        } catch (IOException e) {
            model.addAttribute("reservationNumber", new ArrayList<Reservation>());
            logger.error("Error while retrieving reservations", e);
        }

        Mappings.includeMappings(model);

        // name of template
        return "pages/last-reservations";
    }

    @RequestMapping(value = Mappings.RESERVATION_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        Mappings.includeMappings(model);

        // name of template
        return "pages/reservation-calendar";
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
            HttpServletRequest request,
            CreateReservationForm reservationForm,
            Model model) {

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);
        tokenman.addToken(model);

        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return "pages/reservation-form";
    }

    @PostMapping(Mappings.RESERVATION_FORM)
    public String submitReservation(
            @Valid CreateReservationForm reservationForm,
            BindingResult reservationResults,
            Model model,
            HttpServletRequest request) {

        if (reservationResults.hasErrors()) {

            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", reservationForm.getToken());
            model.addAttribute("errorMessage", "");

            Mappings.includeMappings(model);
            return "pages/reservation-form";
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
                customer = customerService.createCustomer(
                        reservationForm.getCustomerFirstname(),
                        reservationForm.getCustomerLastname(),
                        reservationForm.getCustomerPhonenumber());

                Date departureDate = Utils.stringToDate(reservationForm.getDepartureDate());
                Date arrivalDate = Utils.stringToDate(reservationForm.getArrivalDate());
                reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return "pages/reservation-completed";
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

}