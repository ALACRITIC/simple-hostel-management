package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class MerchantServicesServiceTest {

    private final Logger logger = LoggerFactory.getLogger(ReservationServiceTest.class);

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private MerchantServiceService merchantServicesService;

    @Autowired
    private CustomerService customerService;

    @Test
    public void test() throws IOException {

        DateTime beginTestPeriod = new DateTime();

        // clear all previous entries
        merchantServicesService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();
        customerService.clearAllEntities();

        // create fake customers
        ArrayList<Customer> customers = TestDataFactory.createCustomers(10, customerService);

        // create fake service types
        ArrayList<MerchantServiceType> serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);

        // create fake services
        ArrayList<MerchantService> services = TestDataFactory.createServices(20, customers, serviceTypes,
                beginTestPeriod, merchantServicesService);

        assertTrue("Creation test", merchantServicesService.getAll(-1l, -1l).size() == services.size());
        assertTrue("Creation test", merchantServiceTypesService.getAll().size() == serviceTypes.size());

        merchantServicesService.deleteById(services.get(0).getId());
        merchantServiceTypesService.deleteById(serviceTypes.get(0).getId());

        assertTrue("Suppression test", merchantServicesService.getAll(-1l, -1l).size() == services.size() - 1);
        assertTrue("Suppression test", merchantServiceTypesService.getAll().size() == serviceTypes.size() - 1);

        // get by interval
        List<MerchantService> scheduledServices = merchantServicesService.getScheduledServicesByInterval(
                beginTestPeriod.toDate(), beginTestPeriod.plusDays(60).toDate(), true);

        assertTrue(scheduledServices.size() == services.size() / 2);

        // check that result are refreshed
        assertTrue(scheduledServices.get(0).getCustomer() != null);
        assertTrue(scheduledServices.get(0).getServiceType() != null);
    }
}
