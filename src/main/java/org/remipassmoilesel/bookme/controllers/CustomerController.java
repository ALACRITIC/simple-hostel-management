package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
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
        return Templates.CUSTOMER_SHOW;
    }

    @RequestMapping(value = Mappings.CUSTOMERS_JSON_GET_ALL, method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> getJsonAll(Model model) throws Exception {

        List<Customer> customers = customerService.getAll();

        return customers;
    }

}
