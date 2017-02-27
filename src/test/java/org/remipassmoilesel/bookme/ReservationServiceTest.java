package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
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
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
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

        // remove old entities
        customerService.clearAllEntities();
        accommodationService.clearAllEntities();
        reservationService.clearAllEntities();

        DateTime baseDate = new DateTime(2017, 02, 15, 12, 50);

        ArrayList<Customer> customers = TestDataFactory.createCustomers(10, customerService);
        ArrayList<Accommodation> accommodations = TestDataFactory.createAccommodations(10, accommodationService);

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

        TestDataFactory.createReservationsForIntervalTest(reservationService, customers,
                accommodations, beginTestPeriod, endTestPeriod);

        List<Accommodation> freeAccomodations = reservationService.getAvailableAccommodations(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 1);
        assertTrue("Free resources test 1", accommodations.size() > 1);

        for (Accommodation accomodations : freeAccomodations) {
            // all ressources under 4 should be not free
            assertTrue("Free resources test: " + accommodations.indexOf(accomodations), accommodations.indexOf(accomodations) > 4);
        }

        // check if we ask too much places
        freeAccomodations = reservationService.getAvailableAccommodations(Type.ROOM, beginTestPeriod.toDate(), endTestPeriod.toDate(), 100);
        assertTrue("Free resources test: " + freeAccomodations.size(), freeAccomodations.size() == 0);

        // check reservation price computing
        Reservation res = reservations.get(0);
        res.setBegin(new DateTime(2017, 05, 14, 16, 00, 00).toDate());
        res.setEnd(new DateTime(2017, 05, 19, 10, 00, 00).toDate());
        accommodations.get(0).setPricePerDay(18.66);
        res.setAccommodation(accommodations.get(0));

        res.computeStandardTotalPrice();
        assertTrue(res.getTotalPrice() == Math.round(5 * 18.66 * 100) / 100);
    }

}
