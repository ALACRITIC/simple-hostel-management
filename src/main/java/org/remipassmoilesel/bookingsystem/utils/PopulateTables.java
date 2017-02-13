package org.remipassmoilesel.bookingsystem.utils;

import org.remipassmoilesel.bookingsystem.Mappings;
import org.remipassmoilesel.bookingsystem.customers.Customer;
import org.remipassmoilesel.bookingsystem.customers.CustomerService;
import org.remipassmoilesel.bookingsystem.reservations.Reservation;
import org.remipassmoilesel.bookingsystem.reservations.ReservationService;
import org.remipassmoilesel.bookingsystem.sharedresources.SharedResource;
import org.remipassmoilesel.bookingsystem.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookingsystem.sharedresources.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by remipassmoilesel on 13/02/17.
 */

@Controller
public class PopulateTables {

    private static final Logger logger = LoggerFactory.getLogger(PopulateTables.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SharedResourceService sharedResourceService;

    @RequestMapping(value = Mappings.POPULATE_TABLES, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ArrayList<Reservation> populateTables() throws IOException, ParseException {

        // create rooms
        ArrayList<SharedResource> rooms = new ArrayList<>();
        int totalRooms = 10;
        for (int i = 0; i < totalRooms; i++) {
            rooms.add(sharedResourceService.createRoom("A" + i, 2, "", Type.ROOM));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // create customers and reservations
        int totalReservations = 20;
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < totalReservations; i++) {

            Customer customer = customerService.createCustomer("Jean_" + i, "Paul_" + i, "000000" + i);
            SharedResource resource = rooms.get(Utils.randInt(0, rooms.size() - 1));

            Reservation reservation = reservationService.createReservation(customer, resource, sdf.parse(i + "/01/2017"), sdf.parse((i + 1) + "/01/2017"));
            reservations.add(reservation);

        }

        return reservations;
    }


}
