package org.remipassmoilesel;

import org.remipassmoilesel.customers.Customer;
import org.remipassmoilesel.customers.CustomerService;
import org.remipassmoilesel.reservations.Reservation;
import org.remipassmoilesel.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private static final String BOOK_FORM_TOKEN = "book-form-token";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = Mappings.INDEX, method = RequestMethod.GET)
    public String index(Model model) {

        //model.addAttribute("mode", display);

        // name of template
        return "main";
    }

    @RequestMapping(value = Mappings.RESERVATION_LASTS, method = RequestMethod.GET)
    public String showLastsReservation(HttpServletRequest request, Model model) {

        try {
            model.addAttribute("reservations", reservationService.getLasts(10));
        } catch (SQLException e) {
            model.addAttribute("reservations", "No reservations");
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


}