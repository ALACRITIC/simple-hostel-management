package org.remipassmoilesel.bookme.utils.testdata;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
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
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 13/02/17.
 */

@Controller
public class PopulateTables {

    private static final Logger logger = LoggerFactory.getLogger(PopulateTables.class);
    private final Color cadetblue;
    private final Color blueviolet;
    private final Color darkviolet;
    private final Color darkslategray;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private MerchantServiceService merchantServicesService;

    public PopulateTables() {
        cadetblue = DefaultColors.get("cadetblue").getColor();
        blueviolet = DefaultColors.get("blueviolet").getColor();
        darkslategray = DefaultColors.get("darkslategray").getColor();
        darkviolet = DefaultColors.get("darkviolet").getColor();
    }

    @RequestMapping(value = Mappings.POPULATE_TABLES, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ArrayList<Object> populateTables() throws Exception {

        ArrayList<Object> results = new ArrayList<>();

        // create messages
        ArrayList<Message> messages = TestDataFactory.createMessages(20, messageService);
        results.addAll(messages);

        // create customers
        ArrayList<Customer> customers = TestDataFactory.createCustomers(20, customerService);
        results.add(customers);

        // create rooms
        ArrayList<Accommodation> rooms = TestDataFactory.createAccommodations(20, accommodationService);

        // create reservations
        DateTime now = new DateTime();
        ArrayList<Reservation> reservations = TestDataFactory.createReservations(20, rooms, customers, now, reservationService);
        reservations.addAll(TestDataFactory.createReservations(20, rooms, customers, now.plusMonths(1), reservationService));
        results.addAll(reservations);

        // create services
        ArrayList<MerchantServiceType> serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);
        results.addAll(serviceTypes);

        // create bills
        ArrayList<MerchantService> bills = TestDataFactory.createServices(50, customers, serviceTypes,
                now, merchantServicesService);
        bills.addAll(TestDataFactory.createServices(50, customers, serviceTypes,
                now.plusMonths(1), merchantServicesService));
        results.addAll(bills);

        return results;
    }

}
