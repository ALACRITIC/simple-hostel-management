package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.Customer;
import org.remipassmoilesel.bookme.customers.CustomerForm;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.customers.SearchCustomerForm;
import org.remipassmoilesel.bookme.reservations.Reservation;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.utils.TokenManager;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    public static final String TOKEN_ATTR_SESSION_PREFIX = "customer-form";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MerchantServiceService merchantServiceService;

    @RequestMapping(value = Mappings.CUSTOMERS_SHOW_ALL, method = RequestMethod.GET)
    public String showLastCustomers(Model model) throws Exception {

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
                List<Customer> results = customerService.search(searchForm.getFirstname(), searchForm.getLastname(), "", 40, 0);
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

    @RequestMapping(value = Mappings.CUSTOMERS_JSON_SEARCH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Customer> searchJson(
            @RequestParam(value = "phonenumber", required = false, defaultValue = "") String phonenumber,
            @RequestParam(value = "firstname", required = false, defaultValue = "") String firstname,
            @RequestParam(value = "lastname", required = false, defaultValue = "") String lastname,
            @RequestParam(value = "term", required = false, defaultValue = "") String term)
            throws Exception {

        ArrayList<Customer> results = new ArrayList<>();

        if (term.isEmpty() == false) {
            results.addAll(customerService.search(term, "", "", 50, 0));
            results.addAll(customerService.search("", term, "", 50, 0));
            results.addAll(customerService.search("", "", term, 50, 0));
        }

        if (phonenumber.isEmpty() == false) {
            results.add(customerService.getByPhonenumber(phonenumber));
        }

        if (firstname.isEmpty() == false && lastname.isEmpty() == false) {
            results.addAll(customerService.search(firstname, lastname, "", 50, 0));
        }

        return results;
    }


    /**
     * Show reservation form
     *
     * @param request
     * @param customerForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.CUSTOMERS_FORM)
    public String showForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long customerId,
            HttpServletRequest request,
            CustomerForm customerForm,
            Model model) throws Exception {

        String errorMessage = "";
        if (customerId != -1) {
            Customer res = customerService.getById(customerId);

            if (res != null) {
                customerForm.load(res);
            } else {
                errorMessage = "Customer not found, please try again later";
            }

        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.CUSTOMERS_FORM;
    }

    @PostMapping(Mappings.CUSTOMERS_FORM)
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
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";
        Customer customer = null;

        try {

            // token is invalid
            tokenman.throwIfTokenInvalid(session, customerForm.getToken());

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

    @RequestMapping(value = Mappings.CUSTOMERS_BILL_FORM, method = RequestMethod.GET)
    public String showExportBillFom(
            @RequestParam(value = "begin", required = false) String begin,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "customerId", required = false) Long customerId,
            Model model) throws Exception {

        Mappings.includeMappings(model);
        return Templates.CUSTOMERS_BILL_FORM;
    }

    @RequestMapping(value = Mappings.CUSTOMERS_BILL_PRINT, method = RequestMethod.POST)
    public String exportBillHtml(
            @RequestParam(value = "servicesToExport", required = false) long[] servicesId,
            @RequestParam(value = "reservationsToExport", required = false) long[] reservationsId,
            @RequestParam("customerId") Long customerId,
            Model model) throws Exception {

        Customer customer = customerService.getById(customerId);
        List<Reservation> reservations = reservationService.getByIds(reservationsId);
        List<MerchantService> services = merchantServiceService.getByIds(servicesId);

        // count reservations and mark them as paid
        int totalPrice = 0;
        for (Reservation res : reservations) {
            double pricePerDay = res.getAccommodation().getPricePerDay();
            totalPrice += res.getDuration().getStandardDays() * pricePerDay;
            res.setPaid(true);
        }
        reservationService.update(reservations);

        // count services
        for (MerchantService srv : services) {
            totalPrice += srv.getTotalPrice();
            srv.setPaid(true);
        }
        merchantServiceService.update(services);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("customer", customer);
        model.addAttribute("reservations", reservations);
        model.addAttribute("services", services);
        return Templates.CUSTOMERS_PRINT_BILL;
    }

}
