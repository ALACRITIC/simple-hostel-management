package org.remipassmoilesel.customers;

import org.remipassmoilesel.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomersController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.RESERVATION, method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
                           Model model) {
        model.addAttribute("name", name);

        // name of template
        return "greeting";
    }


}