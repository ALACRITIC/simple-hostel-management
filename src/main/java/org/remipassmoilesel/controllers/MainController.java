package org.remipassmoilesel.controllers;

import org.remipassmoilesel.Mappings;
import org.remipassmoilesel.customers.CustomerService;
import org.remipassmoilesel.reservations.Reservation;
import org.remipassmoilesel.reservations.ReservationForm;
import org.remipassmoilesel.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private static final String BOOK_FORM_TOKEN = "book-form-token";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    /**
     * Redirect root queries to reservation
     *
     * @return
     */
    @RequestMapping(value = Mappings.ROOT, method = RequestMethod.GET)
    public String root() {
        return "redirect:" + Mappings.APPLICATION_INDEX;
    }

    /**
     * Application index
     *
     * @return
     */
    @RequestMapping(value = Mappings.APPLICATION_INDEX, method = RequestMethod.GET)
    public String index() {
        // name of template
        return "pages/main";
    }

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
        } catch (SQLException e) {
            model.addAttribute("reservationNumber", new ArrayList<Reservation>());
            logger.error("Error while retrieving reservations", e);
        }

        // name of template
        return "last-reservations";
    }

    @RequestMapping(value = Mappings.RESERVATION_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        // name of template
        return "calendar";
    }

    /*
    @RequestMapping(value = Mappings.RESERVATION_FORM, method = RequestMethod.GET)
    public String showReservationForm(
            HttpServletRequest request,
            Model model) {

        // set request token
        long token = System.currentTimeMillis();

        // add it to model, to transmit it by forms
        model.addAttribute("token", token);

        // add it to session for check
        HttpSession session = request.getSession();
        session.setAttribute(BOOK_FORM_TOKEN, token);

        // name of template
        return "book-form";
    }
    */

    @RequestMapping(value = Mappings.DO_RESERVATION, method = RequestMethod.GET)
    public String doReservation(
            @Valid ReservationForm reservationForm,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {

        System.out.println("reservationForm");
        System.out.println(reservationForm);

        System.out.println("reservationResults");
        System.out.println(bindingResult);

        System.out.println("reservationResults.hasErrors()");
        System.out.println(bindingResult.hasErrors());

        /*
        String errorMessage = "";
        Customer customer = null;
        Reservation reservation = null;

        // checks tokens
        HttpSession session = request.getSession();

        //check tokens
        Long sessionToken = (Long) session.getAttribute(BOOK_FORM_TOKEN);

        // token is invalid
        if (sessionToken == null || sessionToken.equals(Long.valueOf(token)) == false) {
            logger.error("Invalid token: " + token + " / " + sessionToken);
            errorMessage = "Veuillez reprendre le formulaire";
        }

        // token is valid
        else {

            // always delete token before leave
            session.setAttribute(BOOK_FORM_TOKEN, null);

            try {
                customer = customerService.createClient(firstname, lastname, phonenumber);

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date departureDate = formatter.parse(departure);
                Date arrivalDate = formatter.parse(arrival);
                reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

            } catch (Exception e) {
                logger.error("Error while parsing dates: " + departure, e);
                errorMessage = e.getMessage();
            }
        }
        */

        model.addAttribute("reservationForm", reservationForm);

        // name of template
        return "book-completed";
    }

    /*
    @RequestMapping(value = Mappings.DO_RESERVATION, method = RequestMethod.GET)
    public String doReservation(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "firstname", required = true) String firstname,
            @RequestParam(value = "lastname", required = true) String lastname,
            @RequestParam(value = "phonenumber", required = true) String phonenumber,
            @RequestParam(value = "departure", required = true) String departure,
            @RequestParam(value = "arrival", required = true) String arrival,
            Model model) {

        String errorMessage = "";
        Customer customer = null;
        Reservation reservation = null;

        // checks tokens
        HttpSession session = request.getSession();

        //check tokens
        Long sessionToken = (Long) session.getAttribute(BOOK_FORM_TOKEN);

        // token is invalid
        if (sessionToken == null || sessionToken.equals(Long.valueOf(token)) == false) {
            logger.error("Invalid token: " + token + " / " + sessionToken);
            errorMessage = "Veuillez reprendre le formulaire";
        }

        // token is valid
        else {

            // always delete token before leave
            session.setAttribute(BOOK_FORM_TOKEN, null);

            try {
                customer = customerService.createClient(firstname, lastname, phonenumber);

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date departureDate = formatter.parse(departure);
                Date arrivalDate = formatter.parse(arrival);
                reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

            } catch (Exception e) {
                logger.error("Error while parsing dates: " + departure, e);
                errorMessage = e.getMessage();
            }
        }

        model.addAttribute("customer", customer);
        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("response", response);


        // name of template
        return "book-completed";
    }
    */


}