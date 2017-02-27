package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.configuration.SpringConfiguration;
import org.remipassmoilesel.bookme.controllers.CustomerController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.testdata.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by remipassmoilesel on 22/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(SpringConfiguration.TEST_PROFILE)
public class CustomerControllerTest {

    private MockMvc mockMvc;

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
    private CustomerController customerController;

    private ArrayList<Customer> customers;
    private ArrayList<MerchantServiceType> serviceTypes;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        // clear old customers
        customerService.clearAllEntities();
        merchantServiceService.clearAllEntities();
        reservationService.clearAllEntities();
        merchantServiceTypesService.clearAllEntities();
        accommodationService.clearAllEntities();

        // create fake customers
        customers = TestDataFactory.createCustomers(20, customerService);

        // create fake service types
        serviceTypes = TestDataFactory.createServiceTypes(merchantServiceTypesService);

    }

    @Test
    public void testShowAll() throws Exception {
        mockMvc.perform(get(Mappings.CUSTOMERS_SHOW_ALL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists(Mappings.MODEL_ARGUMENT_NAME));
    }

    @Test
    public void testSearchCustomer() throws Exception {

        mockMvc.perform(get(Mappings.CUSTOMERS_SEARCH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstname", "Jean")
                .param("lastname", "Paul")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("results"))
                .andExpect(model().attributeExists(Mappings.MODEL_ARGUMENT_NAME));
        //TODO: check value returned
    }

    @Test
    public void testJsonGetAll() throws Exception {
        this.mockMvc.perform(get(Mappings.CUSTOMERS_JSON_GET_ALL)).andExpect(status().isOk());
    }

    @Test
    public void testJsonSearchCustomer() throws Exception {

        mockMvc.perform(get(Mappings.CUSTOMERS_JSON_SEARCH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstname", "Jean")
                .param("lastname", "Paul")
                .param("phonenumber", "+000")
                .param("term", "a")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(greaterThan(1))));

    }

    @Test
    public void testCustomerForm() throws Exception {

        // as an empty form
        mockMvc.perform(get(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", Matchers.not(Matchers.isEmptyString())));


        // ask a form with a wrong customer id
        mockMvc.perform(get(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "-2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", Matchers.not(Matchers.isEmptyString())))
                .andExpect(model().attribute("errorMessage", Matchers.not(Matchers.isEmptyString())));

        // update a user
        String customerId = String.valueOf(customers.get(0).getId());
        Map<String, Object> validFormModel = mockMvc.perform(get(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                .param("id", "-2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("token", Matchers.not(Matchers.isEmptyString())))
                .andExpect(model().attribute("errorMessage", Matchers.not(Matchers.isEmptyString())))
                .andReturn().getModelAndView().getModel();

        String formToken = String.valueOf(validFormModel.get("token"));
        String sessionTokenName = TokenManager.generateSessionTokenName(CustomerController.TOKEN_ATTR_SESSION_PREFIX);
        String newPhoneNumber = "+123456789";
        String newFirstname = "firstname";
        String newLastname = "lastname";

        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("id", customerId)
                .param("firstname", newFirstname)
                .param("lastname", newLastname)
                .param("phonenumber", newPhoneNumber)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", Matchers.isEmptyString()));

        // test inserted values
        Customer updatedCustomer = customerService.getByPhonenumber(newPhoneNumber);
        assertTrue(updatedCustomer != null);
        assertTrue(updatedCustomer.getFirstname().equals(newFirstname));
        assertTrue(updatedCustomer.getLastname().equals(newLastname));
        assertTrue(updatedCustomer.getPhonenumber().equals(newPhoneNumber));

        // try to resend same form, should fail because of lack of token
        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", customerId)
                .param("firstname", newFirstname)
                .param("lastname", newLastname)
                .param("phonenumber", newPhoneNumber)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", Matchers.not(Matchers.isEmptyString())));

        // empty first name
        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("id", customerId)
                .param("firstname", "")
                .param("lastname", newLastname)
                .param("phonenumber", newPhoneNumber)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

        // empty last name
        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("id", customerId)
                .param("firstname", newFirstname)
                .param("lastname", "")
                .param("phonenumber", newPhoneNumber)
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

        // empty and wrong phone number
        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("id", customerId)
                .param("firstname", newFirstname)
                .param("lastname", newLastname)
                .param("phonenumber", "++++++")
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

        // empty and wrong phone number
        mockMvc.perform(post(Mappings.CUSTOMERS_FORM)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(sessionTokenName, formToken)
                .param("id", customerId)
                .param("firstname", newFirstname)
                .param("lastname", newLastname)
                .param("phonenumber", "")
                .param("token", formToken))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());

    }

    @Test
    public void textCustomerBill() throws Exception {

        // ask for form
        mockMvc.perform(get(Mappings.CUSTOMERS_BILL_FORM))
                .andExpect(status().isOk());

        // create fake customer
        Customer customer = new Customer(TestDataFactory.getRandomFirstName(),
                TestDataFactory.getRandomLastName(),
                "+" + System.currentTimeMillis());
        customerService.create(customer);

        // create fake services
        double servicePrice = 25.356;
        ArrayList<MerchantService> specialServices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MerchantServiceType srvT = (MerchantServiceType) Utils.randValueFrom(serviceTypes);
            MerchantService srv = new MerchantService(
                    srvT,
                    customer,
                    servicePrice,
                    Utils.generateLoremIpsum(100),
                    new Date(),
                    i % 2 == 0 ? false : true,
                    i % 2 == 0 ? null : new Date());

            merchantServiceService.create(srv);
            specialServices.add(srv);
        }

        // create fake accommodation and fake reservations
        double reservationPrice = 46.25;
        double accommodationPrice = 30.25;
        Accommodation acc = new Accommodation("Accommodation 1", 2, accommodationPrice, "", Type.ROOM, Color.red);
        accommodationService.create(acc);

        ArrayList<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Reservation res = new Reservation(customer, acc, 2,
                    new DateTime(2017, 05, 14, 16, 00).toDate(),
                    new DateTime(2017, 05, 19, 10, 00).toDate());

            if (i != 0) {
                res.setTotalPrice(reservationPrice);
            }

            reservations.add(res);
            reservationService.create(res);
        }

        // compute expected total
        double expectedTotalServices = servicePrice * specialServices.size();
        double expectedTotalReservations = accommodationPrice * 5 +
                        reservationPrice * (reservations.size() - 1);
        double expectedTotal = expectedTotalReservations + expectedTotalServices;

        // ask for bill, complete
        MockHttpServletRequestBuilder req = post(Mappings.CUSTOMERS_BILL_PRINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        req.param("customerId", String.valueOf(customer.getId()));
        for (Reservation res : reservations) {
            req.param("reservationsToBill", String.valueOf(res.getId()));
        }
        for (MerchantService srv : specialServices) {
            req.param("servicesToBill", String.valueOf(srv.getId()));
        }

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(model().attribute("reservations", hasSize(reservations.size())))
                .andExpect(model().attribute("services", hasSize(specialServices.size())))
                .andExpect(model().attribute("totalServices", equalTo(Utils.roundPrice(expectedTotalServices))))
                .andExpect(model().attribute("totalReservations", equalTo(Utils.roundPrice(expectedTotalReservations))))
                .andExpect(model().attribute("totalPrice", equalTo(Utils.roundPrice(expectedTotal))));

        // ask for bill, just with reservations
        req = post(Mappings.CUSTOMERS_BILL_PRINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        req.param("customerId", String.valueOf(customer.getId()));
        for (Reservation res : reservations) {
            req.param("reservationsToBill", String.valueOf(res.getId()));
        }
        mockMvc.perform(req).andExpect(status().isOk());

        // ask for bill, just with services
        req = post(Mappings.CUSTOMERS_BILL_PRINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        req.param("customerId", String.valueOf(customer.getId()));
        for (MerchantService srv : specialServices) {
            req.param("servicesToBill", String.valueOf(srv.getId()));
        }
        mockMvc.perform(req).andExpect(status().isOk());

    }

}
