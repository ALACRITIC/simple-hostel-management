package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.BillService;
import org.remipassmoilesel.bookme.services.MerchantService;
import org.remipassmoilesel.bookme.services.MerchantServiceForm;
import org.remipassmoilesel.bookme.services.MerchantServicesService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class MerchantServicesController {

    private static final Logger logger = LoggerFactory.getLogger(MerchantServicesController.class);
    private static final String TOKEN_NAME = "merchant-services-form";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BillService billService;

    @Autowired
    private MerchantServicesService merchantServicesService;

    @RequestMapping(value = Mappings.SERVICES_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<MerchantService> servicesList = merchantServicesService.getAll();

        model.addAttribute("servicesList", servicesList);

        Mappings.includeMappings(model);
        return Templates.SERVICES_SHOW_ALL;
    }

    /**
     * Show service type form
     *
     * @param serviceTypeId
     * @param request
     * @param form
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(Mappings.SERVICES_FORM)
    public String showCreateServiceTypeForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long serviceTypeId,
            HttpServletRequest request,
            MerchantServiceForm form,
            Model model) throws Exception {

        if (serviceTypeId != -1) {
            MerchantService serType = merchantServicesService.getById(serviceTypeId);
            form.load(serType);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", "");

        Mappings.includeMappings(model);
        return Templates.SERVICES_FORM;
    }

}
