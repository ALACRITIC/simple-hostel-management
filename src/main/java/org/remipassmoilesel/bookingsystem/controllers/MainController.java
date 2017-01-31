package org.remipassmoilesel.bookingsystem.controllers;

import org.remipassmoilesel.bookingsystem.Mappings;
import org.remipassmoilesel.bookingsystem.customers.CustomerService;
import org.remipassmoilesel.bookingsystem.reservations.Reservation;
import org.remipassmoilesel.bookingsystem.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

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
        } catch (IOException e) {
            model.addAttribute("reservationNumber", new ArrayList<Reservation>());
            logger.error("Error while retrieving reservations", e);
        }

        // name of template
        return "pages/last-reservations";
    }

    @RequestMapping(value = Mappings.RESERVATION_CALENDAR, method = RequestMethod.GET)
    public String showCalendar(Model model) {

        //model.addAttribute("name", name);

        // name of template
        return "pages/reservation-calendar";
    }

}