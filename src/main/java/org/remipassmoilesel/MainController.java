package org.remipassmoilesel;

import org.remipassmoilesel.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.INDEX, method = RequestMethod.GET)
    public String index(Model model) {

        //model.addAttribute("mode", display);

        // name of template
        return "main";
    }

    @RequestMapping(value = Mappings.RESERVATION_LASTS, method = RequestMethod.GET)
    public String showLastsReservation(Model model) {

        try {
            model.addAttribute("reservations", reservationService.getLasts(10));
        } catch (SQLException e) {
            model.addAttribute("reservations", "No reservations");
            logger.error("Error while retrieving reservations", e);
        }

        // name of template
        return "last-reservations";
    }

    @RequestMapping(value = Mappings.RESERVATION_FORM, method = RequestMethod.GET)
    public String showReservationForm(Model model) {

        //model.addAttribute("name", name);

        // name of template
        return "book-form";
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
        return "book-completed";
    }


}