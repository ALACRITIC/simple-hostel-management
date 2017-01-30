package org.remipassmoilesel;

import org.remipassmoilesel.reservations.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.INDEX, method = RequestMethod.GET)
    public String index(Model model) {

        //model.addAttribute("mode", display);

        // name of template
        return "main";
    }

    @RequestMapping(value = Mappings.DO_RESERVATION, method = RequestMethod.GET)
    public String doReservation(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "firstname", required = true) String firstname,
            @RequestParam(value = "departure", required = true) String departure,
            @RequestParam(value = "arrival", required = true) String arrival,

            Model model) {
        model.addAttribute("name", name);

        // name of template
        return "fill-me";
    }


}