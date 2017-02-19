package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.ResourceForm;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class SharedResourceController {

    private static final String TOKEN_NAME = "sharedresources";

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private SharedResourceService sharedResourceService;

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.ROOMS_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SharedResource> getRooms() throws Exception {
        List<SharedResource> result = sharedResourceService.getAll(Type.ROOM);
        return result;
    }

    @RequestMapping(value = Mappings.RESOURCES_SHOW_ALL, method = RequestMethod.GET)
    public String showAllResources(Model model) throws Exception {

        List<SharedResource> resourcesList = sharedResourceService.getAll();

        model.addAttribute("resourcesList", resourcesList);

        Mappings.includeMappings(model);
        return Templates.RESOURCES_SHOW;
    }

    /**
     * Show reservation form
     *
     * @param request
     * @param resourceForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESOURCES_FORM)
    public String showResourceForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long resourceId,
            HttpServletRequest request,
            ResourceForm resourceForm,
            Model model) throws IOException {

        if (resourceId != -1) {
            SharedResource res = sharedResourceService.getById(resourceId);
            resourceForm.load(res);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        tokenman.addToken(model);
        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("sharedResourceTypes", Type.values());

        DefaultColors.includeColors(model);
        Mappings.includeMappings(model);
        return Templates.RESOURCES_FORM;
    }

    @PostMapping(Mappings.RESOURCES_FORM)
    public String submitResourceForm(
            @Valid ResourceForm resourceForm,
            BindingResult resourceResult,
            Model model,
            HttpServletRequest request) {

        if (resourceResult.hasErrors()) {

            model.addAttribute("token", resourceForm.getToken());
            model.addAttribute("errorMessage", "");
            model.addAttribute("sharedResourceTypes", Type.values());

            DefaultColors.includeColors(model);
            Mappings.includeMappings(model);
            return Templates.RESOURCES_FORM;
        }

        String errorMessage = "";
        SharedResource resource = null;

        try {
            // checks tokens
            HttpSession session = request.getSession();
            TokenManager tokenman = new TokenManager(TOKEN_NAME);

            // token is invalid
            if (tokenman.isTokenValid(session, resourceForm.getToken()) == false) {
                throw new IllegalStateException("Invalid form, please update form and try again");
            }

            // always delete token just after
            tokenman.deleteTokenFrom(session);

            Color color = Utils.rgbStringToColor(resourceForm.getColor());

            // update room
            if (resourceForm.getId() == -1) {

                try {

                    resource = sharedResourceService.createResource(
                            resourceForm.getName(),
                            resourceForm.getPlaces(),
                            resourceForm.getComment(),
                            resourceForm.getType(),
                            color
                    );
                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating room, please try again later.");
                }
            }

            // update existing room
            else {
                try {

                    resource = sharedResourceService.getById(resourceForm.getId());
                    resource.setName(resourceForm.getName());
                    resource.setPlaces(resourceForm.getPlaces());
                    resource.setComment(resourceForm.getComment());
                    resource.setType(resourceForm.getType());
                    resource.setColor(resourceForm.getColor());

                    sharedResourceService.update(resource);

                } catch (Exception e) {
                    logger.error("Error while creating reservation", e);
                    throw new Exception("Error while creating room, please try again later.");
                }
            }

        } catch (Exception e) {
            logger.error("Error while updating rooms", e);
            errorMessage = e.getMessage();
        }

        model.addAttribute("formstate", "completed");
        model.addAttribute("resource", resource);
        model.addAttribute("errorMessage", errorMessage);

        DefaultColors.includeColors(model);
        Mappings.includeMappings(model);
        return Templates.RESOURCES_FORM;
    }

    /**
     * Delete a resource
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.RESOURCES_DELETE)
    public String deleteResource(
            @RequestParam(value = "id", required = true) Long resourceId,
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

            sharedResourceService.markAsDeleted(resourceId);

        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        DefaultColors.includeColors(model);
        return Templates.RESOURCES_FORM;
    }

}
