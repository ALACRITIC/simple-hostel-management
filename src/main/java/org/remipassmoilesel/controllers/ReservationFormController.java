package org.remipassmoilesel.controllers;

import org.remipassmoilesel.reservations.ReservationForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ReservationFormController {

    @GetMapping("reservation/form")
    public String showForm(ReservationForm reservationForm) {
        return "forms/form";
    }

    @PostMapping("reservation/form")
    public String checkPersonInfo(@Valid ReservationForm reservationForm, BindingResult reservationResults) {

        if (reservationResults.hasErrors()) {
            return "forms/form";
        }

        return "redirect:forms/results";
    }
}