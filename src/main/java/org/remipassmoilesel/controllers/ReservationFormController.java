package org.remipassmoilesel.controllers;

import org.remipassmoilesel.Mappings;
import org.remipassmoilesel.customers.Customer;
import org.remipassmoilesel.reservations.Reservation;
import org.remipassmoilesel.reservations.ReservationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ReservationFormController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationFormController.class);

    private static final String FORM_TOKEN = "reservation-form-token";

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

        // add it to session for check
        HttpSession session = request.getSession();
        session.setAttribute(FORM_TOKEN, token);

        return "pages/reservation-form";
    }

    @PostMapping(Mappings.RESERVATION_FORM)
    public String checkPersonInfo(
            @Valid ReservationForm reservationForm,
            BindingResult reservationResults,
            Model model,
            HttpServletRequest request) {

        if (reservationResults.hasErrors()) {

            System.out.println(reservationResults.getAllErrors());

            return "pages/reservation-form";
        }

        Customer customer = null;
        Reservation reservation = null;

        // checks tokens
        HttpSession session = request.getSession();

        //check tokens
        Long token = reservationForm.getToken();
        Long sessionToken = (Long) session.getAttribute(FORM_TOKEN);

        // token is invalid
        if (sessionToken == null || sessionToken.equals(Long.valueOf(token)) == false) {
            logger.error("Invalid token: " + token + " / " + sessionToken);
        }

        // token is valid
        else {

            // always delete token before leave
            session.setAttribute(FORM_TOKEN, null);

            /*

            try {
                customer = customerService.createClient(firstname, lastname, phonenumber);

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date departureDate = formatter.parse(departure);
                Date arrivalDate = formatter.parse(arrival);
                reservation = reservationService.createReservation(customer, departureDate, arrivalDate);

            } catch (Exception e) {
                logger.error("Error while parsing dates: " + departure, e);
                errorMessage = e.getMessage();
            }
            */
        }


        model.addAttribute("reservationForm", reservationForm);


        return "redirect:pages/reservation-completed";
    }

}