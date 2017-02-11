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
    public String index(Model model) {

        Mappings.includeMappings(model);

        // name of template
        return "pages/main";
    }


}