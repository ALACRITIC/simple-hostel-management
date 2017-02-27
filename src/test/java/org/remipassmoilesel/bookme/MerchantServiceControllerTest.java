package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.controllers.MerchantServicesController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by remipassmoilesel on 22/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class MerchantServiceControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @Autowired
    private MerchantServicesController merchantServicesController;

    private ArrayList<Customer> customers;
    private ArrayList<MerchantServiceType> serviceTypes;
    private ArrayList<MerchantService> services;
    private DateTime beginTestPeriod;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(merchantServicesController).build();

        beginTestPeriod = new DateTime();

        // clear old values
        customerService.clearAllEntities();
        merchantServiceService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();

        // create fake customers
        customers = TestDataFactory.createCustomers(10, customerService);

        // create fake service types
        serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);

        // create fake services
        services = TestDataFactory.createServices(20, customers, serviceTypes,
                new DateTime(), merchantServiceService);

    }

    @Test
    public void deleteService() throws Exception {

        // get a valid token
        Map<String, Object> validFormModel = mockMvc.perform(get(Mappings.SERVICE_TYPES_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "-1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TokenManager.DEFAULT_MODEL_TOKEN_NAME,
                        Matchers.not(Matchers.isEmptyString())))
                .andExpect(model().attribute("errorMessage", Matchers.isEmptyString()))
                .andReturn().getModelAndView().getModel();

        // delete service type
        String formToken = String.valueOf(validFormModel.get(TokenManager.DEFAULT_MODEL_TOKEN_NAME));
        mockMvc.perform(get(Mappings.SERVICE_TYPES_DELETE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(serviceTypes.get(0).getId()))
                .param("token", formToken)
                .sessionAttr(TokenManager.generateSessionTokenName(
                        MerchantServicesController.TOKEN_ATTR_SESSION_PREFIX), formToken))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        // delete service
        mockMvc.perform(get(Mappings.SERVICES_DELETE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", String.valueOf(services.get(0).getId()))
                .param("token", formToken)
                .sessionAttr(TokenManager.generateSessionTokenName(
                        MerchantServicesController.TOKEN_ATTR_SESSION_PREFIX), formToken))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", isEmptyString()));

        // check for services
        assertTrue(merchantServiceService.getById(services.get(0).getId()) == null);
        assertTrue(merchantServiceTypesService.getById(serviceTypes.get(0).getId()) != null);
        assertTrue(merchantServiceTypesService.getById(serviceTypes.get(0).getId()).isDeleted() == true);

    }

    // TODO valid forms

}