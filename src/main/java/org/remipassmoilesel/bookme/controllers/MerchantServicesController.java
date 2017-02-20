package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.customers.CustomerService;
import org.remipassmoilesel.bookme.services.MerchantServiceService;
import org.remipassmoilesel.bookme.services.MerchantServiceType;
import org.remipassmoilesel.bookme.services.MerchantServiceTypeForm;
import org.remipassmoilesel.bookme.services.MerchantServiceTypesService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    private MerchantServiceService merchantServiceService;

    @Autowired
    private MerchantServiceTypesService merchantServiceTypesService;

    @RequestMapping(value = Mappings.SERVICES_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<MerchantServiceType> serviceTypesList = merchantServiceTypesService.getAll();

        model.addAttribute("serviceTypesList", serviceTypesList);

        Mappings.includeMappings(model);
        return Templates.SERVICES_TYPES_SHOW_ALL;
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
            MerchantServiceTypeForm form,
            Model model) throws Exception {

        if (serviceTypeId != -1) {
            MerchantServiceType serType = merchantServiceTypesService.getById(serviceTypeId);
            form.load(serType);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);
        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("errorMessage", "");

        DefaultColors.includeDarkColors(model);
        Mappings.includeMappings(model);
        return Templates.SERVICES_TYPE_FORM;
    }

    @PostMapping(Mappings.SERVICES_FORM)
    public String submitServiceType(
            @Valid MerchantServiceTypeForm serviceForm,
            BindingResult serviceResults,
            Model model,
            HttpServletRequest request) throws Exception {

        if (serviceResults.hasErrors()) {

            model.addAttribute("token", serviceForm.getToken());
            model.addAttribute("errorMessage", "");

            DefaultColors.includeDarkColors(model);
            Mappings.includeMappings(model);
            return Templates.SERVICES_TYPE_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";
        MerchantServiceType service = null;

        try {

            // token is invalid
            if (tokenman.isTokenValid(session, serviceForm.getToken()) == false) {
                logger.error("Invalid token: " + serviceForm.getToken());
                throw new IllegalStateException("Invalid form, please update form and try again");
            }

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            // add reservation if it is a new one
            if (serviceForm.getId() == -1) {

                String name = serviceForm.getName().trim();

                if(merchantServiceTypesService.getByName(name) != null){
                    throw new Exception("This service already exist, please choose an other name");
                }

                try {

                    service = new MerchantServiceType(
                            name,
                            serviceForm.getPrice(),
                            serviceForm.getComment(),
                            serviceForm.getColor()
                    );

                    merchantServiceTypesService.create(service);

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating reservation, please try again later.");
                }
            }

            // else update existing reservation
            else {
                try {
                    service = merchantServiceTypesService.getById(serviceForm.getId());
                } catch (Exception e) {
                    logger.error("Error while searching searching: " + serviceForm.getId(), e);
                    throw new Exception("Error, please try again later.");
                }

                if (service == null) {
                    logger.error("Invalid service: c/" + service);
                    throw new IllegalStateException("Error, please try again later.");
                }

                // update customer
                service.setColor(serviceForm.getColor());
                service.setComment(serviceForm.getComment());
                service.setName(serviceForm.getName());
                service.setPrice(serviceForm.getPrice());

                try {
                    // update repository
                    merchantServiceTypesService.update(service);

                } catch (Exception e) {
                    logger.error("Error while updating reservation", e);
                    throw new Exception("Error while updating, please try again later.");
                }

            }

        } catch (Exception e) {
            logger.error("Error while validating form: ", e);
            errorMessage = e.getMessage();
        }

        model.addAttribute("formstate", "completed");
        model.addAttribute("serviceType", service);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.SERVICES_TYPE_FORM;
    }

    /**
     * Delete a service
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.SERVICES_DELETE)
    public String deleteServiceType(
            @RequestParam(value = "id", required = true) Long serviceId,
            @RequestParam(value = "token", required = true) String token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";

        // token is invalid
        if (tokenman.isTokenValid(session, Long.valueOf(token)) == false) {
            errorMessage = "Invalid form. Please reload form and try again.";
        }

        // token is valid
        else {

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            merchantServiceTypesService.markAsDeleted(serviceId);

        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        return Templates.SERVICES_TYPE_FORM;
    }

}
