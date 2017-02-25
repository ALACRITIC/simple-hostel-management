package org.remipassmoilesel.bookme.utils;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.Mappings;
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
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 13/02/17.
 */

@Controller
public class DevTools {

    private static final Logger logger = LoggerFactory.getLogger(DevTools.class);
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

    public DevTools() {
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
        ArrayList<Message> messages = createMessages(20);
        results.addAll(messages);

        // create customers
        ArrayList<Customer> customers = createCustomers(20);
        results.add(customers);

        // create rooms
        ArrayList<Accommodation> rooms = createRooms(20);

        // create reservations
        DateTime now = new DateTime();
        ArrayList<Reservation> reservations = createReservations(20, rooms, customers, now);
        reservations.addAll(createReservations(20, rooms, customers, now.plusMonths(1)));
        results.addAll(reservations);

        // create services
        ArrayList<MerchantServiceType> serviceTypes = createServiceTypes();
        results.addAll(serviceTypes);

        // create bills
        ArrayList<MerchantService> bills = createServices(50, customers, serviceTypes, now);
        bills.addAll(createServices(50, customers, serviceTypes, now.plusMonths(1)));
        results.addAll(bills);

        return results;
    }

    private ArrayList<Message> createMessages(int number) throws IOException {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Message msg = new Message(null, Utils.generateLoremIpsum(2000));
            messageService.create(msg);
            messages.add(msg);
        }

        return messages;
    }

    private ArrayList<Accommodation> createRooms(int number) throws IOException {

        // create rooms
        ArrayList<Accommodation> rooms = new ArrayList<>();
        Color color;
        for (int i = 0; i < number; i++) {
            color = i % 2 == 0 ? blueviolet : cadetblue;
            rooms.add(accommodationService.createAccommodation("B " + i, 2, 2.5, "", Type.ROOM, color));
        }

        return rooms;
    }


    public ArrayList<Customer> createCustomers(int number) throws IOException {

        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Customer customer = customerService.create("Jean " + i, "Paul " + i, "+" + System.currentTimeMillis() + i);
            customers.add(customer);
        }

        return customers;
    }

    /**
     * Partial date example: 02/2017
     *
     * @param rooms
     * @param startDate
     * @return
     * @throws Exception
     */
    private ArrayList<Reservation> createReservations(int number, List<Accommodation> rooms, List<Customer> customers,
                                                      DateTime startDate) throws Exception {


        // create customers and reservations
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Customer customer = customers.get(i);
            Accommodation accommodation = rooms.get(Utils.randInt(0, rooms.size() - 1));
            Reservation reservation = reservationService.create(customer, accommodation, 1,
                    startDate.plusDays(i).toDate(),
                    startDate.plusDays(i + Utils.randInt(3, 6)).toDate());
            reservations.add(reservation);
        }

        return reservations;
    }

    private ArrayList<MerchantServiceType> createServiceTypes() throws IOException {

        // create service types
        List<String> names = Arrays.asList(
                "Beer",
                "Coca-cola",
                "Sandwich",
                "Guided-tour"
        );

        ArrayList<MerchantServiceType> serviceTypes = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            Color color = i % 2 == 0 ? darkslategray : darkviolet;
            MerchantServiceType st = new MerchantServiceType(names.get(i), 5 + i, Utils.generateLoremIpsum(400), color);
            serviceTypes.add(st);
            merchantServiceTypesService.create(st);
        }

        return serviceTypes;
    }

    private ArrayList<MerchantService> createServices(int number, List<Customer> customers, List<MerchantServiceType> services, DateTime startDate) throws IOException, ParseException {

        ArrayList<MerchantService> bills = new ArrayList<>();

        // create non scheduled services
        for (int i = 0; i < number / 2; i++) {
            MerchantService serv = new MerchantService(
                    (MerchantServiceType) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    Utils.randInt(5, 25),
                    "",
                    startDate.plusDays(Utils.randInt(1, 31)).toDate(),
                    false,
                    null
            );
            merchantServicesService.create(serv);
            bills.add(serv);
        }

        // create scheduled services
        for (int i = number / 2; i < number; i++) {
            int plus = Utils.randInt(1, 2);
            DateTime srvDate = startDate.plusDays(Utils.randInt(1, 31));
            MerchantService serv = new MerchantService(
                    (MerchantServiceType) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    Utils.randInt(5, 25),
                    "",
                    srvDate.toDate(),
                    true,
                    srvDate.plusDays(Utils.randInt(1, 6)).toDate()
            );
            merchantServicesService.create(serv);
            bills.add(serv);
        }

        // create scheduled services
        return bills;
    }

}
