package org.remipassmoilesel.bookme;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.export.ExportService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by remipassmoilesel on 27/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class ExportServiceTest {

    private final Logger logger = LoggerFactory.getLogger(ExportServiceTest.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private ExportService exportService;

    @Test
    public void test() throws Exception {

        // clear old customers
        customerService.clearAllEntities();
        merchantServiceService.clearAllEntities();
        reservationService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();
        accommodationService.clearAllEntities();

        // create fake customers
        ArrayList<Customer> customers = TestDataFactory.createCustomers(20, customerService);

        // create fake accommodations
        ArrayList<Accommodation> accommodations = TestDataFactory.createAccommodations(10, accommodationService);

        // create fake reservations
        ArrayList<Reservation> reservations = TestDataFactory.createReservations(30, accommodations,
                customers, new DateTime(), reservationService);

        // create fake service types
        ArrayList<MerchantServiceType> serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);

        // create fake service
        ArrayList<MerchantService> services = TestDataFactory.createServices(30, customers, serviceTypes,
                new DateTime(), merchantServiceService);

        // export reservations
        File exportRes = exportService.exportReserationsCsv(
                new DateTime().minusDays(1).toDate(),
                new DateTime().plusMonths(2).toDate());

        assertTrue(Files.exists(exportRes.toPath()));

        // test output
        String reservationStr = IOUtils.toString(Files.newInputStream(exportRes.toPath()));
        assertTrue(reservationStr.length() > 3800);

        // export services
        File exportSer = exportService.exportServicesCsv(
                new DateTime().minusDays(1).toDate(),
                new DateTime().plusMonths(2).toDate());

        assertTrue(Files.exists(exportSer.toPath()));

        // test output
        String serviceStr = IOUtils.toString(Files.newInputStream(exportSer.toPath()));
        assertTrue(serviceStr.length() > 24400);

    }
}
