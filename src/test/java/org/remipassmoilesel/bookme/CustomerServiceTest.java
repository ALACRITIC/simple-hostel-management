package org.remipassmoilesel.bookme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.remipassmoilesel.bookme.configuration.CustomConfiguration;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by remipassmoilesel on 13/02/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(CustomConfiguration.TEST_PROFILE)
public class CustomerServiceTest {

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private CustomerService customerService;

    @Test
    public void test() throws IOException {

        int customerNumber = 10;
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customerNumber; i++) {
            Customer customer = new Customer("Jean_" + i, "Paul_" + i, "+00000" + i);
            customerService.create(customer);

            customers.add(customer);
        }

        // basic equality test
        assertTrue("Equality test 1", customers.get(0).equals(customers.get(0)));
        assertFalse("Equality test 2", customers.get(0).equals(customers.get(1)));

        // retrieving test
        for (Customer customerA : customers) {
            Customer customerB = customerService.getById(customerA.getId());
            assertTrue("Database retrieving test: " + customerA + " / " + customerB, customerA.equals(customerB));
        }

        // refresh test
        String newName = "Not a first name";
        customers.get(0).setFirstname(newName);
        customerService.refresh(customers.get(0));
        assertFalse("Refresh test", customers.get(0).getFirstname().equals(newName));

    }

}
