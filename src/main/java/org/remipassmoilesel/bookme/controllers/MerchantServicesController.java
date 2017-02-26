package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.*;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class MerchantServicesController {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServicesController.class);

    public static final String TOKEN_ATTR_SESSION_PREFIX = "merchant-services-form";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @RequestMapping(value = Mappings.SERVICES_SHOW_LASTS, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<MerchantServiceType> serviceTypesList = merchantServiceTypesService.getAll();
        List<MerchantService> servicesList = merchantServiceService.getAll();

        model.addAttribute("serviceTypesList", serviceTypesList);
        model.addAttribute("servicesList", servicesList);

        Mappings.includeMappings(model);
        return Templates.SERVICE_TYPES_SHOW_ALL;
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
    public String showServiceTypeForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long serviceTypeId,
            HttpServletRequest request,
            MerchantServiceTypeForm form,
            Model model) throws Exception {

        if (serviceTypeId != -1) {
            MerchantServiceType serType = merchantServiceTypesService.getById(serviceTypeId);
            form.load(serType);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", "");

        DefaultColors.includeDarkColors(model);
        Mappings.includeMappings(model);
        return Templates.SERVICE_TYPES_FORM;
    }

    @PostMapping(Mappings.SERVICE_TYPES_FORM)
    public String submitServiceType(
            @Valid MerchantServiceTypeForm serviceTypeForm,
            BindingResult serviceResults,
            Model model,
            HttpServletRequest request) throws Exception {

        if (serviceResults.hasErrors()) {

            model.addAttribute("token", serviceTypeForm.getToken());
            model.addAttribute("errorMessage", "");

            DefaultColors.includeDarkColors(model);
            Mappings.includeMappings(model);
            return Templates.SERVICE_TYPES_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";
        MerchantServiceType serviceType = null;

        try {

            // check if token is invalid
            tokenman.throwIfTokenInvalid(session, serviceTypeForm.getToken());

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            // add reservation if it is a new one
            if (serviceTypeForm.getId() == -1) {

                // Check if name already exist
                String name = serviceTypeForm.getName().trim();
                if (merchantServiceTypesService.getByName(name) != null) {
                    throw new Exception("This service already exist, please choose an other name");
                }

                try {

                    serviceType = new MerchantServiceType(
                            name,
                            serviceTypeForm.getPrice(),
                            serviceTypeForm.getComment(),
                            serviceTypeForm.getColor()
                    );

                    merchantServiceTypesService.create(serviceType);

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating reservation, please try again later.");
                }
            }

            // else update existing reservation
            else {
                try {
                    serviceType = merchantServiceTypesService.getById(serviceTypeForm.getId());
                } catch (Exception e) {
                    logger.error("Error while searching searching: " + serviceTypeForm.getId(), e);
                    throw new Exception("Error, please try again later.");
                }

                if (serviceType == null) {
                    logger.error("Invalid service: c/" + serviceType);
                    throw new IllegalStateException("Error, please try again later.");
                }

                // update customer
                serviceType.setColor(serviceTypeForm.getColor());
                serviceType.setComment(serviceTypeForm.getComment());
                serviceType.setName(serviceTypeForm.getName());
                serviceType.setPrice(serviceTypeForm.getPrice());

                try {
                    // update repository
                    merchantServiceTypesService.update(serviceType);

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
        model.addAttribute("serviceType", serviceType);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.SERVICE_TYPES_FORM;
    }

    /**
     * Delete a service
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.SERVICE_TYPES_DELETE)
    public String deleteServiceType(
            @RequestParam(value = "id", required = true) Long serviceId,
            @RequestParam(value = "token", required = true) Long token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";

        // check if token is invalid
        tokenman.throwIfTokenInvalid(session, token);

        // token is valid
        // always delete token before leave
        tokenman.deleteTokenFrom(session);

        merchantServiceTypesService.markAsDeleted(serviceId);

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        return Templates.SERVICE_TYPES_FORM;
    }

    /**
     * Show service form
     *
     * @param serviceId
     * @param request
     * @param form
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(Mappings.SERVICES_FORM)
    public String showServiceForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long serviceId,
            @RequestParam(value = "date", required = false, defaultValue = "") String executionDate,
            HttpServletRequest request,
            MerchantServiceForm form,
            Model model) throws Exception {

        if (serviceId != -1) {
            MerchantService serType = merchantServiceService.getById(serviceId);
            form.load(serType);
        }

        if (executionDate.isEmpty() == false) {
            form.setExecutionDate(executionDate);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", "");
        includeServiceTypes(model);
        Mappings.includeMappings(model);

        return Templates.SERVICES_FORM;
    }

    private void includeServiceTypes(Model model) throws IOException {
        model.addAttribute("serviceTypes", merchantServiceTypesService.getAll());
    }

    @PostMapping(Mappings.SERVICES_FORM)
    public String submitService(
            @Valid MerchantServiceForm serviceForm,
            BindingResult serviceResults,
            Model model,
            HttpServletRequest request) throws Exception {

        if (serviceResults.hasErrors()) {

            model.addAttribute("token", serviceForm.getToken());
            model.addAttribute("errorMessage", "");

            includeServiceTypes(model);
            Mappings.includeMappings(model);
            return Templates.SERVICES_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";
        MerchantService service = null;

        try {

            // check if token is invalid
            tokenman.throwIfTokenInvalid(session, serviceForm.getToken());

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            MerchantServiceType serviceType = merchantServiceTypesService.getById(serviceForm.getServiceType());

            // retrieve customer or create it if needed
            Customer customer = customerService.getById(serviceForm.getCustomerId());
            if (customer == null) {
                customer = new Customer(serviceForm.getCustomerFirstname(),
                        serviceForm.getCustomerLastname(), serviceForm.getCustomerPhonenumber());
                customerService.create(customer);
            }

            Date execDate = Utils.stringToDateTime(serviceForm.getExecutionDate(), "dd/MM/YY HH:mm").toDate();

            // add reservation if it is a new one
            if (serviceForm.getId() == -1) {

                try {

                    service = new MerchantService(
                            serviceType,
                            customer,
                            serviceForm.getTotalPrice(),
                            serviceForm.getComment(),
                            new Date(),
                            serviceForm.isScheduled(),
                            execDate
                    );

                    merchantServiceService.create(service);

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating reservation, please try again later.");
                }
            }

            // else update existing reservation
            else {
                try {
                    service = merchantServiceService.getById(serviceForm.getId());
                } catch (Exception e) {
                    logger.error("Error while searching searching: " + serviceForm.getId(), e);
                    throw new Exception("Error, please try again later.");
                }

                if (service == null) {
                    logger.error("Invalid service: c/" + service);
                    throw new IllegalStateException("Error, please try again later.");
                }

                // update service
                service.setServiceType(serviceType);
                service.setCustomer(customer);
                service.setTotalPrice(serviceForm.getTotalPrice());
                service.setComment(serviceForm.getComment());
                service.setScheduled(serviceForm.isScheduled());
                service.setExecutionDate(execDate);

                try {
                    // update repository
                    merchantServiceService.update(service);

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
        model.addAttribute("service", service);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.SERVICES_FORM;
    }

    /**
     * Delete a service
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.SERVICES_DELETE)
    public String deleteService(
            @RequestParam(value = "id", required = true) Long serviceId,
            @RequestParam(value = "token", required = true) Long token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";

        // check if token is invalid
        tokenman.throwIfTokenInvalid(session, token);

        // token is valid
        // always delete token before leave
        tokenman.deleteTokenFrom(session);

        merchantServiceService.deleteById(serviceId);

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        return Templates.SERVICES_FORM;
    }

    /**
     * Delete a service
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.SERVICES_BY_CUSTOMER)
    public String byCustomer(
            @RequestParam(value = "id", required = true) Long customerId,
            Model model) throws Exception {

        String errorMessage = "";
        Customer customer = null;
        List<MerchantService> servicesList = null;
        try {
            customer = customerService.getById(customerId);
            if (customer == null) {
                throw new NullPointerException("Invalid customer: " + customerId);
            }
            servicesList = merchantServiceService.getByCustomerId(customerId, true);
        } catch (Exception e) {
            logger.error("Error while retrieving services: ", e);
            errorMessage = "Error while processing services, please try again later.";
        }

        model.addAttribute("customer", customer);
        model.addAttribute("servicesList", servicesList);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.SERVICES_BY_CUSTOMER;
    }

    @RequestMapping(value = Mappings.SERVICES_CALENDAR, method = RequestMethod.GET)
    public String getScheduledServices(Model model) throws Exception {
        Mappings.includeMappings(model);
        return Templates.SERVICES_CALENDAR;
    }

    @RequestMapping(value = Mappings.SERVICES_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<MerchantService> getScheduledServices(
            @RequestParam(value = "start", required = true) String startDateStr,
            @RequestParam(value = "end", required = true) String endDateStr) throws Exception {

        Date startDate = Utils.stringToDate(startDateStr);
        Date endDate = Utils.stringToDate(endDateStr);

        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("Begin date is greater than end date: " + startDateStr + " / " + endDateStr);
        }

        List<MerchantService> result = merchantServiceService.getScheduledServicesByInterval(startDate, endDate, true);
        return result;

    }

    @RequestMapping(value = Mappings.SERVICES_JSON_SEARCH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<MerchantService> searchServices(
            @RequestParam(value = "customerId", required = true) Long customerId) throws Exception {

        List<MerchantService> result = merchantServiceService.getByCustomerId(customerId, true);

        return result;

    }


}
