package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
    private SharedResourceService sharedResourceService;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void test() throws IOException {

        customerService.clearAllRows();
        sharedResourceService.clearAllRows();
        reservationService.clearAllRows();

        DateTime baseDate = new DateTime(2017, 02, 15, 12, 50);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Jean", "Claude", "+3333333"));
        customers.add(new Customer("Paul", "Bedel", "+3333333"));
        customerService.createCustomer(customers.get(0));
        customerService.createCustomer(customers.get(1));

        ArrayList<SharedResource> resources = new ArrayList<>();
        int resourcesNumber = 10;
        for (int i = 0; i < resourcesNumber; i++) {
            SharedResource res = new SharedResource("A" + i, 2, "Comment", Type.ROOM);
            resources.add(res);
            sharedResourceService.createResource(res);
        }

        // test creation with wrong dates, should fail
        Reservation wrongRes = null;
        try {
            wrongRes = new Reservation(customers.get(0), resources.get(0), 2,
                    baseDate.plusDays(5).toDate(), baseDate.toDate(), null);
        } catch (Exception e) {
            logger.info("Error while creating reservation: ", e);
        }
        assertTrue("Creation test: ", wrongRes == null);

        // creation test
        int reservationsNumber = 10;
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {
            Reservation res = new Reservation(customers.get(i % 2 == 0 ? 0 : 1), resources.get(i), 2,
                    baseDate.toDate(), baseDate.plusDays(5).toDate(), null);

            reservationService.createReservation(res);

            reservations.add(res);
        }

        // basic equality test
        assertTrue("Equality test 1", resources.get(0).equals(resources.get(0)));
        assertFalse("Equality test 2", resources.get(0).equals(resources.get(1)));

        // retrieving test
        for (Reservation resA : reservations) {
            Reservation resB = reservationService.getById(resA.getId());
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.equals(resB));
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.getCustomer().getFirstname() != null);
            assertTrue("Database retrieving test: " + resA + " / " + resB, resA.getResource().getName() != null);
        }

        // Free resources test

        DateTime beginTestPeriod = baseDate.plusDays(30);
        DateTime endTestPeriod = baseDate.plusDays(40);

        // add special reservations to test against test period (TP)
        // 1: begin before TP and finish in TP
        reservationService.createReservation(customers.get(0), resources.get(0), 2, beginTestPeriod.minusDays(2).toDate(), beginTestPeriod.plusDays(2).toDate());
        // 2: begin in TP and finish after TP
        reservationService.createReservation(customers.get(0), resources.get(1), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 3: begin before TP and finish after TP
        reservationService.createReservation(customers.get(0), resources.get(2), 2, beginTestPeriod.minusDays(2).toDate(), endTestPeriod.plusDays(2).toDate());
        // 4: begin after TP and finish before end of TP
        reservationService.createReservation(customers.get(0), resources.get(3), 2, beginTestPeriod.plusDays(2).toDate(), endTestPeriod.minusDays(2).toDate());
        // 5: same period
        reservationService.createReservation(customers.get(0), resources.get(4), 2, beginTestPeriod.toDate(), endTestPeriod.toDate());

        List<SharedResource> freeResources = reservationService.getAvailableResources(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 1);
        assertTrue("Free resources test 1", resources.size() > 1);

        for (SharedResource resource : freeResources) {
            // all ressources under 4 should be not free
            assertTrue("Free resources test: " + resources.indexOf(resource), resources.indexOf(resource) > 4);
        }

        // check if we ask too much places
        freeResources = reservationService.getAvailableResources(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 100);
        assertTrue("Free resources test: " + freeResources.size(), freeResources.size() == 0);
    }

}
