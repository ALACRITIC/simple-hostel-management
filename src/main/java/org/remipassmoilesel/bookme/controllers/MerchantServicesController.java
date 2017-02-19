package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerForm;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.*;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class MerchantServicesController {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServicesController.class);
    private static final String TOKEN_NAME = "merchant-services-form";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BillService merchantServicesService;

    @Autowired
    private MerchantServicesService merchantServiceTypesService;

    @RequestMapping(value = Mappings.SERVICES_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<MerchantServiceBill> services = merchantServicesService.getAll();

        model.addAttribute("services", services);

        Mappings.includeMappings(model);
        return Templates.SERVICE_SHOW_ALL;
    }

    /**
     * Show service type form
     *
     * @param serviceTypeId
     * @param request
     * @param form
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(Mappings.SERVICE_TYPES_FORM)
    public String showCreateServiceTypeForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long serviceTypeId,
            HttpServletRequest request,
            MerchantServiceForm form,
            Model model) throws Exception {

        if (serviceTypeId != -1) {
            MerchantService serType = merchantServiceTypesService.getById(serviceTypeId);
            form.load(serType);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", "");

        Mappings.includeMappings(model);
        return Templates.SERVICE_TYPES_FORM;
    }

    @PostMapping(Mappings.SERVICE_TYPES_FORM)
    public String submitReservation(
            @Valid CustomerForm customerForm,
            BindingResult customerResults,
            Model model,
            HttpServletRequest request) throws Exception {

        if (customerResults.hasErrors()) {

            model.addAttribute("token", customerForm.getToken());
            model.addAttribute("errorMessage", "");

            Mappings.includeMappings(model);
            return Templates.CUSTOMERS_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";
        Customer customer = null;

        try {

            // token is invalid
            if (tokenman.isTokenValid(session, customerForm.getToken()) == false) {
                logger.error("Invalid token: " + customerForm.getToken());
                throw new IllegalStateException("Invalid form, please update form and try again");
            }

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            // add reservation if it is a new one
            if (customerForm.getId() == -1) {
                try {

                    customer = customerService.create(
                            customerForm.getFirstname(),
                            customerForm.getLastname(),
                            customerForm.getPhonenumber());

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating reservation, please try again later.");
                }
            }

            // else update existing reservation
            else {
                try {
                    customer = customerService.getById(customerForm.getId());
                } catch (Exception e) {
                    logger.error("Error while updating reservation", e);
                    throw new Exception("Error while updating, please try again later.");
                }

                if (customer == null) {
                    logger.error("Invalid customer: c/" + customer);
                    throw new IllegalStateException("Error, please try again later.");
                }

                // update customer
                customer.setFirstname(customerForm.getFirstname());
                customer.setLastname(customerForm.getLastname());
                customer.setPhonenumber(customerForm.getPhonenumber());

                try {
                    // update repository
                    customerService.update(customer);

                } catch (Exception e) {
                    logger.error("Error while updating reservation", e);
                    throw new Exception("Error while updating, please try again later.");
                }

            }

        } catch (Exception e) {
            logger.error("Error while validating form: ", e);
            errorMessage = e.getMessage();
        }

        model.addAttribute("formstate", "completed");
        model.addAttribute("customer", customer);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.CUSTOMERS_FORM;
    }


}
