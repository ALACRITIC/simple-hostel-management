package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by remipassmoilesel on 13/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class ReservationServiceTest {

    private final Logger logger = LoggerFactory.getLogger(ReservationServiceTest.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void test() throws IOException {

        customerService.clearAllEntities();
        accommodationService.clearAllEntities();
        reservationService.clearAllEntities();

        DateTime baseDate = new DateTime(2017, 02, 15, 12, 50);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Jean", "Claude", "+3333333"));
        customers.add(new Customer("Paul", "Bedel", "+444444"));
        customerService.create(customers.get(0));
        customerService.create(customers.get(1));

        ArrayList<Accommodation> accommodations = new ArrayList<>();
        int accommodationsNumber = 10;
        for (int i = 0; i < accommodationsNumber; i++) {
            Accommodation res = new Accommodation("A" + i, 2, 2.5, "Comment", Type.ROOM, Color.blue);
            accommodations.add(res);
            accommodationService.create(res);
        }

        // test creation with wrong dates, should fail
        Reservation wrongRes = null;
        try {
            wrongRes = new Reservation(customers.get(0), accommodations.get(0), 2,
                    baseDate.plusDays(5).toDate(), baseDate.toDate(), null);
        } catch (Exception e) {
            logger.info("Error while creating reservation: ", e);
        }
        assertTrue("Creation test: ", wrongRes == null);

        // creation test
        int reservationsNumber = 10;
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {
            Reservation res = new Reservation(customers.get(i % 2 == 0 ? 0 : 1), accommodations.get(i), 2,
                    baseDate.toDate(), baseDate.plusDays(5).toDate(), null);

            reservationService.create(res);

            reservations.add(res);
        }

        // basic equality test
        assertTrue("Equality test 1", accommodations.get(0).equals(accommodations.get(0)));
        assertFalse("Equality test 2", accommodations.get(0).equals(accommodations.get(1)));

        // retrieving test
        for (Reservation resA : reservations) {
            Reservation resB = reservationService.getById(resA.getId());
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.equals(resB));
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.getCustomer().getFirstname() != null);
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.getAccommodation().getName() != null);
        }

        // Free resources test

        DateTime beginTestPeriod = baseDate.plusDays(30);
        DateTime endTestPeriod = baseDate.plusDays(40);

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

        List<Accommodation> freeAccomodations = reservationService.getAvailableAccommodations(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 1);
        assertTrue("Free resources test 1", accommodations.size() > 1);

        for (Accommodation accomodations : freeAccomodations) {
            // all ressources under 4 should be not free
            assertTrue("Free resources test: " + accommodations.indexOf(accomodations), accommodations.indexOf(accomodations) > 4);
        }

        // check if we ask too much places
        freeAccomodations = reservationService.getAvailableAccommodations(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 100);
        assertTrue("Free resources test: " + freeAccomodations.size(), freeAccomodations.size() == 0);
    }

}
