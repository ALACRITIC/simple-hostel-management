package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.controllers.CustomerController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by remipassmoilesel on 22/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerController customerController;

    private ArrayList<Customer> customers;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        // create fake customers
        customers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customers.add(new Customer("Jean " + i, "Paul " + i, "+000000" + i));
        }

        // add it
        customerService.clearAllEntities();
        for (Customer customer : customers) {
            customerService.create(customer);
        }

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
        String sessionTokenName = TokenManager.generateSessionTokenName(CustomerController.TOKEN_ATTR_SESSION_NAME);
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

}
