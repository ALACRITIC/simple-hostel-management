package org.remipassmoilesel.bookme;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.controllers.CustomerController;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        // create fake customers
        ArrayList<Customer> customers = new ArrayList<>();
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
                .andExpect(jsonPath("$", Matchers.notNullValue()));

    }
}
