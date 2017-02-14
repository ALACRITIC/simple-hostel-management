package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageService messageService;

    /**
     * Redirect root queries to reservation
     *
     * @return
     */
    @RequestMapping(value = Mappings.ROOT, method = RequestMethod.GET)
    public String root() {
        return "redirect:" + Mappings.APPLICATION_ROOT;
    }

    /**
     * Application index
     *
     * @return
     */
    @RequestMapping(value = Mappings.APPLICATION_ROOT, method = RequestMethod.GET)
    public String showIndex(Model model) throws IOException {

        // get last messages
        List<Message> lastMessagesList = messageService.getLasts(3, 0);

        // get lasts checkouts
        List<Reservation> todayCheckoutsList = reservationService.getNextCheckouts(3, 0);

        // get lasts reservations
        List<Reservation> lastReservationsList = reservationService.getLastReservations(3, 0);

        // get last clients
        List<Customer> lastCustomersList = customerService.getLasts(3, 0);

        model.addAttribute("lastMessagesList", lastMessagesList);
        model.addAttribute("todayCheckoutsList", todayCheckoutsList);
        model.addAttribute("lastReservationsList", lastReservationsList);
        model.addAttribute("lastCustomersList", lastCustomersList);

        Mappings.includeMappings(model);

        // name of template
        return Templates.DASHBOARD;
    }

  /**
     *
     *
     * @return
     */
    @RequestMapping(value = Mappings.MAIN_TEMPLATE, method = RequestMethod.GET)
    public String showLiveTemplate(Model model) throws IOException {

        Mappings.includeMappings(model);

        // name of template
        return Templates.MAIN_TEMPLATE;
    }


}