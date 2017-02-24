package org.remipassmoilesel.bookme;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.Utils;
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

import static junit.framework.TestCase.assertTrue;

/**
 * Created by remipassmoilesel on 19/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
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

        // clear old customers
        customerService.clearAllEntities();

        // create fake customers
        ArrayList<Object> customers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customers.add(new Customer("Jean " + i, "Paul " + i, "+0015513131" + i));
        }

        // create fake service types
        int serviceTypesNumber = 5;
        ArrayList<MerchantServiceType> serviceTypes = new ArrayList<>();
        for (int i = 0; i < serviceTypesNumber; i++) {
            MerchantServiceType srvType = new MerchantServiceType("Service " + i, 5 + i,
                    Utils.generateLoremIpsum(100), Color.blue);
            merchantServiceTypesService.create(srvType);
            serviceTypes.add(srvType);
        }

        // create fake services
        int servicesNumber = 20;
        ArrayList<MerchantService> services = new ArrayList<>();
        for (int i = 0; i < servicesNumber; i++) {
            MerchantServiceType srvT = (MerchantServiceType) Utils.randValueFrom(serviceTypes);
            Customer customer = (Customer) Utils.randValueFrom(customers);
            int price = srvT.getPrice() + i;
            String comment = Utils.generateLoremIpsum(100);
            MerchantService srv = new MerchantService(srvT, customer, price, comment,
                    beginTestPeriod.plusDays(i).toDate(),
                    false, null);
            merchantServicesService.create(srv);
            services.add(srv);
        }

        for (int i = 0; i < servicesNumber; i++) {
            MerchantServiceType srvT = (MerchantServiceType) Utils.randValueFrom(serviceTypes);
            Customer customer = (Customer) Utils.randValueFrom(customers);
            int price = srvT.getPrice() + i;
            String comment = Utils.generateLoremIpsum(100);
            MerchantService srv = new MerchantService(srvT, customer, price, comment,
                    beginTestPeriod.plusDays(i).toDate(),
                    true, beginTestPeriod.plusDays(i).toDate());
            merchantServicesService.create(srv);
            services.add(srv);
        }

        assertTrue("Creation test", merchantServicesService.getAll().size() == servicesNumber * 2);
        assertTrue("Creation test", merchantServiceTypesService.getAll().size() == serviceTypesNumber);

        merchantServicesService.deleteById(services.get(0).getId());
        merchantServiceTypesService.deleteById(serviceTypes.get(0).getId());

        assertTrue("Suppression test", merchantServicesService.getAll().size() == servicesNumber * 2 - 1);
        assertTrue("Suppression test", merchantServiceTypesService.getAll().size() == serviceTypesNumber - 1);

        // get by interval
        List<MerchantService> scheduledServices = merchantServicesService.getScheduledServicesByInterval(
                beginTestPeriod.toDate(), beginTestPeriod.plusDays(10).toDate(), true);

        assertTrue(scheduledServices.size() == 11);

        // check that result are refreshed
        assertTrue(scheduledServices.get(0).getCustomer() != null);
        assertTrue(scheduledServices.get(0).getServiceType() != null);
    }
}
