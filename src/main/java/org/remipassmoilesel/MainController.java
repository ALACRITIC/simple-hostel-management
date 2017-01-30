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

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

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
    public String showLastsReservation(Model model) {

        try {
            model.addAttribute("reservations", reservationService.getLasts(10));
        } catch (SQLException e) {
            model.addAttribute("reservations", "No reservations");
            logger.error("Error while retrieving reservations", e);
        }

        // name of template
        return "last-reservations";
    }

    @RequestMapping(value = Mappings.RESERVATION_FORM, method = RequestMethod.GET)
    public String showReservationForm(Model model) {

        //model.addAttribute("name", name);

        // name of template
        return "book-form";
    }

    @RequestMapping(value = Mappings.RESERVATION_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        // name of template
        return "calendar";
    }

    @RequestMapping(value = Mappings.DO_RESERVATION, method = RequestMethod.GET)
    public String doReservation(
            @RequestParam(value = "firstname", required = true) String firstname,
            @RequestParam(value = "lastname", required = true) String lastname,
            @RequestParam(value = "phonenumber", required = true) String phonenumber,
            @RequestParam(value = "departure", required = true) String departure,
            @RequestParam(value = "arrival", required = true) String arrival,
            Model model) {

        String errorMessage = "";
        Customer customer = null;
        Reservation reservation = null;
        try {
            customer = customerService.createClient(firstname, lastname, phonenumber);

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date departureDate = formatter.parse(departure);
            Date arrivalDate = formatter.parse(departure);
            reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

        } catch (Exception e) {
            logger.error("Error while parsing dates: " + departure, e);
            errorMessage = e.getMessage();
        }

        model.addAttribute("customer", customer);
        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);

        // name of template
        return "book-completed";
    }


}