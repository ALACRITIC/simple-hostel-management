package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.customers.SearchCustomerForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = Mappings.CUSTOMERS_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<Customer> customers = customerService.getAll();

        model.addAttribute("customers", customers);

        Mappings.includeMappings(model);
        return Templates.CUSTOMERS_SHOW;
    }

    @RequestMapping(value = Mappings.CUSTOMERS_SEARCH, method = RequestMethod.GET)
    public String searchCustomer(
            SearchCustomerForm searchForm,
            Model model) throws Exception {

        String errorMessage = "";
        if (searchForm.getFirstname() != null || searchForm.getLastname() != null) {
            try {
                List<Customer> results = customerService.search(searchForm.getFirstname(), searchForm.getLastname(), 40, 0);
                model.addAttribute("results", results);
            } catch (Exception e) {
                logger.error("Error while searching customers: ", e);
                errorMessage = "You must specify a valid first name or last name";
            }
        }

        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.CUSTOMERS_SEARCH;
    }

    @RequestMapping(value = Mappings.CUSTOMERS_JSON_GET_ALL, method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> getJsonAll() throws Exception {
        List<Customer> customers = customerService.getAll();
        return customers;
    }

}
