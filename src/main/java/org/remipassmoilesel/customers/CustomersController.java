package org.remipassmoilesel.customers;

import org.remipassmoilesel.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomersController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.RESERVATION_INDEX, method = RequestMethod.GET)
    public String reservationIndex(
            @RequestParam(value = "mode", required = false, defaultValue = "display") String display,
            Model model) {

        model.addAttribute("mode", display);

        // name of template
        return "reservation-index";
    }

    @RequestMapping(value = Mappings.RESERVATION, method = RequestMethod.GET)
    public String reservation(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "firstname", required = true) String firstname,
            @RequestParam(value = "departure", required = true) String departure,
            @RequestParam(value = "arrival", required = true) String arrival,

            Model model) {
        model.addAttribute("name", name);

        // name of template
        return "greeting";
    }


}