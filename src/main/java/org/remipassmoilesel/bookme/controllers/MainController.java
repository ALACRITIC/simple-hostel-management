package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.admin.TiledMenu;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.remipassmoilesel.bookme.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

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
    @RequestMapping(value = Mappings.DASHBOARD, method = RequestMethod.GET)
    public String showDashboard(Model model, Locale locale) throws IOException {

        // messageSource.getMessage("welcome.message", new Object[]{"John Doe"}, locale);

        // get last messages
        List<Message> lastMessagesList = messageService.getLasts(3, 0);

        // get lasts checkouts
        List<Reservation> nextCheckoutsList = reservationService.getNextCheckouts(5, 0);

        // get lasts reservations
        List<Reservation> lastReservationsList = reservationService.getLastReservations(3, 0);

        // get last clients
        List<Customer> lastCustomersList = customerService.getLasts(3, 0);

        model.addAttribute("lastMessagesList", lastMessagesList);
        model.addAttribute("nextCheckoutsList", nextCheckoutsList);
        model.addAttribute("lastReservationsList", lastReservationsList);
        model.addAttribute("lastCustomersList", lastCustomersList);

        Mappings.includeMappings(model);

        // name of template
        return Templates.DASHBOARD;
    }

    @RequestMapping(value = Mappings.APPLICATION_ROOT, method = RequestMethod.GET)
    public String showMainMenu(Model model) throws IOException {

        TiledMenu.includeMenus(model);
        Mappings.includeMappings(model);

        // name of template
        return Templates.MAIN_MENU;
    }


    /**
     * @return
     */
    @RequestMapping(value = Mappings.MAIN_TEMPLATE, method = RequestMethod.GET)
    public String showLiveTemplate(Model model) throws IOException {

        // fake messages
        int messageNumber = 10;
        ArrayList<Object> messagesList = new ArrayList<>();
        for (int i = 0; i < messageNumber; i++) {
            Message msg = new Message(null, Utils.generateLoremIpsum(2000));
            messagesList.add(msg);
        }

        // fake reservations, resources and customers
        int reservationsNumber = 10;
        ArrayList<Reservation> reservationsList = new ArrayList<>();
        ArrayList<Customer> customersList = new ArrayList<>();
        ArrayList<SharedResource> resourcesList = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {

            Customer customer = new Customer("Jean_" + i, "Paul_" + i, "000000" + i);
            customersList.add(customer);

            SharedResource resource = new SharedResource("Room " + i, 2, 2.5d, "Comment " + i, Type.ROOM, Color.blue);
            resourcesList.add(resource);

            Reservation reservation = new Reservation(customer, resource, 1, new Date(), new Date());
            reservationsList.add(reservation);
        }

        model.addAttribute("reservationsList", reservationsList);
        model.addAttribute("reservation", reservationsList.get(0));
        model.addAttribute("messagesList", messagesList);
        model.addAttribute("customersList", customersList);
        model.addAttribute("resourcesList", resourcesList);
        model.addAttribute("message", messagesList.get(0));
        model.addAttribute("resource", resourcesList.get(0));
        model.addAttribute("errorMessage", "Invalid date: 11/11/2555");
        model.addAttribute("informationMessage", "Operation success !");

        Mappings.includeMappings(model);

        // name of template
        return Templates.MAIN_TEMPLATE;
    }

}