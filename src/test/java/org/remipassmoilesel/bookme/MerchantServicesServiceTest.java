package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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

    @Test
    public void test() throws IOException {

        merchantServicesService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();

        MerchantService srv = merchantServicesService.create(new MerchantService());
        MerchantServiceType srvType = merchantServiceTypesService.create(new MerchantServiceType());

        assertTrue("Creation test", merchantServicesService.getAll().size() == 1);
        assertTrue("Creation test", merchantServiceTypesService.getAll().size() == 1);

        merchantServicesService.deleteById(srv.getId());
        merchantServiceTypesService.deleteById(srvType.getId());

        assertTrue("Suppression test", merchantServicesService.getAll().size() == 0);
        assertTrue("Suppression test", merchantServiceTypesService.getAll().size() == 0);

        // TODO
    }
}
