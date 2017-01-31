package org.remipassmoilesel.controllers;

import org.remipassmoilesel.Mappings;
import org.remipassmoilesel.customers.Customer;
import org.remipassmoilesel.customers.CustomerService;
import org.remipassmoilesel.reservations.Reservation;
import org.remipassmoilesel.reservations.ReservationForm;
import org.remipassmoilesel.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ReservationFormController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationFormController.class);

    private static final String FORM_TOKEN = "reservation-form-token";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    /**
     * Show reservation form
     *
     * @param request
     * @param reservationForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESERVATION_FORM)
    public String showForm(
            HttpServletRequest request,
            ReservationForm reservationForm,
            Model model) {

        // set request token
        long token = System.currentTimeMillis();

        // add it to model, to transmit it by forms
        model.addAttribute("token", token);
        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        session.setAttribute(FORM_TOKEN, token);

        return "pages/reservation-form";
    }

    @PostMapping(Mappings.RESERVATION_FORM)
    public String submitReservation(
            @Valid ReservationForm reservationForm,
            BindingResult reservationResults,
            Model model,
            HttpServletRequest request) {

        if (reservationResults.hasErrors()) {
            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", reservationForm.getToken());
            model.addAttribute("errorMessage", "");
            return "pages/reservation-form";
        }

        Customer customer = null;
        Reservation reservation = null;

        // checks tokens
        HttpSession session = request.getSession();

        //check tokens
        Long token = reservationForm.getToken();
        Long sessionToken = (Long) session.getAttribute(FORM_TOKEN);

        String errorMessage = "";

        // token is invalid
        if (sessionToken == null || sessionToken.equals(Long.valueOf(token)) == false) {
            logger.error("Invalid token: " + token + " / " + sessionToken);
            errorMessage = "Invalid session";
        }

        // token is valid
        else {

            // always delete token before leave
            session.setAttribute(FORM_TOKEN, null);

            try {
                customer = customerService.createClient(
                        reservationForm.getCustomerFirstname(),
                        reservationForm.getCustomerLastname(),
                        reservationForm.getCustomerPhonenumber());

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date departureDate = formatter.parse(reservationForm.getDepartureDate());
                Date arrivalDate = formatter.parse(reservationForm.getArrivalDate());
                reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("reservationForm", reservationForm);
        model.addAttribute("reservation", reservation);
        model.addAttribute("errorMessage", errorMessage);

        return "pages/reservation-completed";
    }

}