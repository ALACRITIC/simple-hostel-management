package org.remipassmoilesel.bookme.controllers;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.menu.MainMenu;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private MerchantServiceService merchantServiceService;


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
        List<Reservation> lastReservationsList = reservationService.getLastReservations(5, 0);

        // get lasts reservations
        List<MerchantService> nextScheduledServicesList = merchantServiceService.getLastScheduledServices(5, 0);

        // get last clients
        List<Customer> lastCustomersList = customerService.getLasts(6, 0);

        model.addAttribute("lastMessagesList", lastMessagesList);
        model.addAttribute("nextCheckoutsList", nextCheckoutsList);
        model.addAttribute("nextScheduledServicesList", nextScheduledServicesList);
        model.addAttribute("lastReservationsList", lastReservationsList);
        model.addAttribute("lastCustomersList", lastCustomersList);

        Mappings.includeMappings(model);

        // name of template
        return Templates.DASHBOARD;
    }

    @RequestMapping(value = Mappings.APPLICATION_ROOT, method = RequestMethod.GET)
    public String showMainMenu(Model model) throws IOException {

        MainMenu.includeMenus(model);
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
        ArrayList<Accommodation> accommodationsList = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {

            Customer customer = new Customer("Jean_" + i, "Paul_" + i, "000000" + i);
            customersList.add(customer);

            Accommodation acco = new Accommodation("Room " + i, 2, 2.5d, "Comment " + i, Type.ROOM, Color.blue);
            accommodationsList.add(acco);

            Reservation reservation = new Reservation(customer, acco, 1, new Date(), new Date());
            reservationsList.add(reservation);
        }

        // create fake services
        ArrayList<MerchantServiceType> serviceTypesList = new ArrayList<>();
        ArrayList<MerchantService> servicesList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MerchantServiceType st = new MerchantServiceType("Service 1", 5.6,
                    Utils.generateLoremIpsum(200), Color.blue);
            MerchantService sv = new MerchantService(st, customersList.get(0), 35.6,
                    Utils.generateLoremIpsum(200), new Date(), true, new DateTime().plusDays(5).toDate());
            serviceTypesList.add(st);
            servicesList.add(sv);
        }

        model.addAttribute("reservationsList", reservationsList);
        model.addAttribute("reservation", reservationsList.get(0));
        model.addAttribute("messagesList", messagesList);
        model.addAttribute("customersList", customersList);
        model.addAttribute("customer", customersList.get(0));
        model.addAttribute("accommodationsList", accommodationsList);
        model.addAttribute("message", messagesList.get(0));
        model.addAttribute("accommodation", accommodationsList.get(0));
        model.addAttribute("errorMessage", "Invalid date: 11/11/2555");
        model.addAttribute("informationMessage", "Operation success !");
        model.addAttribute("serviceTypesList", serviceTypesList);
        model.addAttribute("serviceTypesList", serviceTypesList);
        model.addAttribute("servicesList", servicesList);
        model.addAttribute("serviceType", serviceTypesList.get(0));
        model.addAttribute("service", servicesList.get(0));
        model.addAttribute("adviceMessage", "Advice message");

        Mappings.includeMappings(model);

        // name of template
        return Templates.MAIN_TEMPLATE;
    }

}