package org.remipassmoilesel.bookme.utils;

import org.joda.time.DateTime;
import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 13/02/17.
 */

@Controller
public class DevTools {

    private static final Logger logger = LoggerFactory.getLogger(DevTools.class);
    private final Color cadetblue;
    private final Color blueviolet;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SharedResourceService sharedResourceService;

    public DevTools() {
        cadetblue = DefaultColors.get("cadetblue").getColor();
        blueviolet = DefaultColors.get("blueviolet").getColor();
    }

    @RequestMapping(value = Mappings.POPULATE_TABLES, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ArrayList<Reservation> populateTables() throws Exception {

        // create messages
        createMessages();

        // create rooms
        ArrayList<SharedResource> rooms = createRooms();

        // create reservations
        DateTime now = new DateTime();
        ArrayList<Reservation> reservations = createReservations(rooms, now.toString("MM/yyyy"));
        reservations.addAll(createReservations(rooms, now.plusMonths(1).toString("MM/yyyy")));

        return reservations;
    }

    private void createMessages() throws IOException {
        int messageNumber = 10;
        for (int i = 0; i < messageNumber; i++) {
            Message msg = new Message(null, Utils.generateLoremIpsum(2000));
            messageService.create(msg);
        }
    }

    private ArrayList<SharedResource> createRooms() throws IOException {

        // create rooms
        ArrayList<SharedResource> rooms = new ArrayList<>();
        int totalRooms = 10;
        Color color;
        for (int i = 0; i < totalRooms; i++) {
            color = i % 2 == 0 ? blueviolet : cadetblue;
            rooms.add(sharedResourceService.createResource("B " + i, 2, "", Type.ROOM, color));
        }

        return rooms;
    }


    /**
     * Partial date example: 02/2017
     *
     * @param rooms
     * @param partialDate
     * @return
     * @throws Exception
     */
    private ArrayList<Reservation> createReservations(ArrayList<SharedResource> rooms, String partialDate) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // create customers and reservations
        int totalReservations = 20;
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < totalReservations; i++) {

            Customer customer = customerService.create("Jean " + i, "Paul " + i, "+" + System.currentTimeMillis() + i);
            SharedResource resource = rooms.get(Utils.randInt(0, rooms.size() - 1));

            Reservation reservation = reservationService.create(customer, resource, 1,
                    sdf.parse(i + "/" + partialDate), sdf.parse((i + 1) + "/" + partialDate));
            reservations.add(reservation);

        }

        return reservations;
    }

}
