package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by remipassmoilesel on 13/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
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
                    new DateTime().plusDays(5).toDate(), new Date(), null);
        } catch (Exception e) {
            logger.info("Error while creating reservation: ", e);
        }
        assertTrue("Creation test: ", wrongRes == null);

        // creation test
        int reservationsNumber = 10;
        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < reservationsNumber; i++) {
            Reservation res = new Reservation(customers.get(i % 2 == 0 ? 0 : 1), resources.get(i), 2,
                    new Date(), new DateTime().plusDays(5).toDate(), null);

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


    }

}
