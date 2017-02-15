package org.remipassmoilesel.bookme.security;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by remipassmoilesel on 15/02/17.
 */
@Controller
public class SecurityController {

    @RequestMapping(Mappings.LOGIN_PAGE)
    public String showForm(Model model) {

        Mappings.includeMappings(model);
        return Templates.PAGES_LOGIN;
    }

}
