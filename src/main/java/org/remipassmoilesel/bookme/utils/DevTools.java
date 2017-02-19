package org.remipassmoilesel.bookme.utils;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.BillService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceBill;
import org.remipassmoilesel.bookme.services.MerchantServicesService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
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
import java.text.SimpleDateFormat;
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
    private SharedResourceService sharedResourceService;

    @Autowired
    private MerchantServicesService merchantServiceTypesService;

    @Autowired
    private BillService merchantServicesService;

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
        ArrayList<SharedResource> rooms = createRooms(20);

        // create reservations
        DateTime now = new DateTime();
        ArrayList<Reservation> reservations = createReservations(20, rooms, customers, now.toString("MM/yyyy"));
        reservations.addAll(createReservations(20, rooms, customers, now.plusMonths(1).toString("MM/yyyy")));
        results.addAll(reservations);

        // create services
        ArrayList<MerchantService> serviceTypes = createServiceTypes(7);
        results.addAll(serviceTypes);

        // create bills
        ArrayList<MerchantServiceBill> bills = createServiceBills(10, customers, serviceTypes, now.toString("MM/yyyy"));
        bills.addAll(createServiceBills(10, customers, serviceTypes, now.plusMonths(1).toString("MM/yyyy")));
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

    private ArrayList<SharedResource> createRooms(int number) throws IOException {

        // create rooms
        ArrayList<SharedResource> rooms = new ArrayList<>();
        Color color;
        for (int i = 0; i < number; i++) {
            color = i % 2 == 0 ? blueviolet : cadetblue;
            rooms.add(sharedResourceService.createResource("B " + i, 2, "", Type.ROOM, color));
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
     * @param partialDate
     * @return
     * @throws Exception
     */
    private ArrayList<Reservation> createReservations(int number, List<SharedResource> rooms, List<Customer> customers, String partialDate) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // create customers and reservations
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Customer customer = customers.get(i);
            SharedResource resource = rooms.get(Utils.randInt(0, rooms.size() - 1));
            Reservation reservation = reservationService.create(customer, resource, 1,
                    sdf.parse(i + "/" + partialDate), sdf.parse((i + 1) + "/" + partialDate));
            reservations.add(reservation);
        }

        return reservations;
    }

    private ArrayList<MerchantService> createServiceTypes(int number) throws IOException {

        // create service types
        List<String> names = Arrays.asList(
                "Une Petite Mousse - La Brasserie, à Comboire",
                "Pilote 1 - Orange Is The New Black (Bière Ambrée aux écorces d'oranges)",
                "Pilote 2 - Winter Is Coming (Winter Ale)", "Brasserie du Mont Aiguille de Clelles en Trièves",
                "La PréamBulle Bière Blonde",
                "La FunamBulle Bière Ambrée",
                "La NoctamBulle Bière Brune",
                "La Mandrin, bière artisanale de Saint-Martin-d'Hères",
                "Mandrin aux noix",
                "Mandrin à la réglisse",
                "La glutte",
                "La Fax",
                "Mandrin au sapin",
                "Mandrin au miel",
                "Mandrin aux 7 plantes du Massif de la Chartreuse");

        ArrayList<MerchantService> serviceTypes = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            Color color = i % 2 == 0 ? darkslategray : darkviolet;
            MerchantService st = new MerchantService(names.get(i), 5 + i, Utils.generateLoremIpsum(400), color);
            serviceTypes.add(st);
            merchantServiceTypesService.create(st);
        }

        return serviceTypes;
    }

    private ArrayList<MerchantServiceBill> createServiceBills(int number, List<Customer> customers, List<MerchantService> services, String partialDate) throws IOException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        ArrayList<MerchantServiceBill> bills = new ArrayList<>();

        // create non scheduled services
        for (int i = 0; i < number / 2; i++) {
            MerchantServiceBill bill = new MerchantServiceBill(
                    (MerchantService) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    5 + i,
                    "",
                    sdf.parse(i + "/" + partialDate),
                    false,
                    null
            );
        }

        // create non scheduled services
        for (int i = number / 2; i < number; i++) {
            MerchantServiceBill bill = new MerchantServiceBill(
                    (MerchantService) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    5 + i,
                    "",
                    sdf.parse(i + "/" + partialDate),
                    true,
                    sdf.parse(i + "/" + partialDate)
            );
        }

        // create scheduled services
        return bills;
    }

}
