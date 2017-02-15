package org.remipassmoilesel.bookme.credentials;

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

    @RequestMapping(Mappings.LOGIN_URL)
    public String showForm(Model model) {

        Mappings.includeMappings(model);
        return Templates.PAGES_LOGIN;
    }

}
