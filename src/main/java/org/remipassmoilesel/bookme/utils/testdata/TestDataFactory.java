package org.remipassmoilesel.bookme.utils.testdata;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
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
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by remipassmoilesel on 26/02/17.
 */
public class TestDataFactory {

    private final static List<Color> colors = Arrays.asList(
            DefaultColors.get("CadetBlue").getColor(),
            DefaultColors.get("DarkCyan").getColor(),
            DefaultColors.get("DarkSlateBlue").getColor(),
            DefaultColors.get("Grey").getColor(),
            DefaultColors.get("OliveDrab").getColor(),
            DefaultColors.get("RebeccaPurple").getColor(),
            DefaultColors.get("YellowGreen").getColor());

    private final static List<String> firstnames = Arrays.asList(
            "Jean", "Paul",
            "Claude", "Evelyne",
            "Marion", "Alice");

    private final static List<String> lastnames = Arrays.asList(
            "Paluatus", "Claduatos",
            "Pinel", "Montant",
            "Descendant", "Huaraz");

    // create service types
    private static List<String> serviceTypeNames = Arrays.asList(
            "Beer",
            "Coca-cola",
            "Sandwich",
            "Guided-tour"
    );

    public static Color getRandomColor() {
        return (Color) Utils.randValueFrom(colors);
    }

    public static String getRandomFirstName() {
        return (String) Utils.randValueFrom(firstnames);
    }

    public static String getRandomLastName() {
        return (String) Utils.randValueFrom(lastnames);
    }

    public static ArrayList<Accommodation> createAccommodations(int number, AccommodationService accommodationService) throws IOException {

        ArrayList<Accommodation> accommodations = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Accommodation accomm = new Accommodation("Room A" + i, 2, Utils.randDouble(5, 15),
                    Utils.generateLoremIpsum(300), Type.ROOM, getRandomColor());

            accommodationService.create(accomm);
            accommodations.add(accomm);
        }

        return accommodations;
    }

    public static ArrayList<Customer> createCustomers(int number, CustomerService customerService) throws IOException {

        // create fake customers
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Customer customer = new Customer((String) getRandomFirstName(), getRandomLastName() + " " + i, "+" + System.currentTimeMillis() + i);

            customers.add(customer);
            customerService.create(customer);
        }

        return customers;
    }


    public static ArrayList<Reservation> createReservations(int number, List<Accommodation> rooms,
                                                            List<Customer> customers,
                                                            DateTime startDate,
                                                            ReservationService reservationService) throws Exception {

        // create customers and reservations
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Customer customer = (Customer) Utils.randValueFrom(customers);
            Accommodation accommodation = rooms.get(Utils.randInt(0, rooms.size() - 1));
            Reservation reservation = new Reservation(customer, accommodation, 1,
                    startDate.plusDays(i).toDate(),
                    startDate.plusDays(i + Utils.randInt(3, 6)).toDate());
            reservation.computeStandardTotalPrice();

            reservationService.create(reservation);
            reservations.add(reservation);
        }

        return reservations;
    }

    public static ArrayList<MerchantServiceType> createServiceTypes(MerchantServiceTypesService merchantServiceTypesService) throws IOException {

        ArrayList<MerchantServiceType> serviceTypes = new ArrayList<>();
        for (int i = 0; i < serviceTypeNames.size(); i++) {
            Color color = getRandomColor();
            MerchantServiceType st = new MerchantServiceType(serviceTypeNames.get(i), 5 + i,
                    Utils.generateLoremIpsum(400), color);

            serviceTypes.add(st);
            merchantServiceTypesService.create(st);
        }

        return serviceTypes;
    }

    public static ArrayList<MerchantService> createServices(int number, List<Customer> customers,
                                                            List<MerchantServiceType> services,
                                                            DateTime startDate,
                                                            MerchantServiceService merchantServiceService) throws IOException {

        ArrayList<MerchantService> bills = new ArrayList<>();

        // create non scheduled services
        for (int i = 0; i < number / 2; i++) {
            MerchantService serv = new MerchantService(
                    (MerchantServiceType) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    Utils.randInt(5, 25),
                    Utils.generateLoremIpsum(200),
                    startDate.plusDays(Utils.randInt(1, 31)).toDate(),
                    false,
                    null
            );

            merchantServiceService.create(serv);
            bills.add(serv);
        }

        // create scheduled services
        for (int i = number / 2; i < number; i++) {
            DateTime srvDate = startDate.plusDays(Utils.randInt(1, 31));
            MerchantService serv = new MerchantService(
                    (MerchantServiceType) Utils.randValueFrom(services),
                    (Customer) Utils.randValueFrom(customers),
                    Utils.randInt(5, 25),
                    Utils.generateLoremIpsum(400),
                    srvDate.toDate(),
                    true,
                    srvDate.plusDays(Utils.randInt(1, 6)).toDate()
            );
            merchantServiceService.create(serv);
            bills.add(serv);
        }

        // create scheduled services
        return bills;
    }

    public static ArrayList<Message> createMessages(int number, MessageService messageService) throws IOException {

        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Message msg = new Message(null, Utils.generateLoremIpsum(2000));
            messageService.create(msg);
            messages.add(msg);
        }

        return messages;
    }

    public static void createReservationsForIntervalTest(ReservationService reservationService,
                                                         List<Customer> customers,
                                                         List<Accommodation> accommodations,
                                                         DateTime beginTestPeriod,
                                                         DateTime endTestPeriod)
            throws IOException {

        // add special reservations to test against test period (TP)
        // 1: begin before TP and finish in TP
        reservationService.create(customers.get(0), accommodations.get(0), 2, beginTestPeriod.minusDays(2).toDate(), beginTestPeriod.plusDays(2).toDate());
        // 2: begin in TP and finish after TP
        reservationService.create(customers.get(0), accommodations.get(1), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 3: begin before TP and finish after TP
        reservationService.create(customers.get(0), accommodations.get(2), 2, beginTestPeriod.minusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 4: begin after TP and finish before end of TP
        reservationService.create(customers.get(0), accommodations.get(3), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.minusDays(2).toDate());
        // 5: same period
        reservationService.create(customers.get(0), accommodations.get(4), 2, beginTestPeriod.toDate(), endTestPeriod.toDate());

    }


}
