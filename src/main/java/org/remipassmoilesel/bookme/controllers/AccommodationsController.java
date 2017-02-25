package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.accommodations.Accommodation;
import org.remipassmoilesel.bookme.accommodations.AccommodationForm;
import org.remipassmoilesel.bookme.accommodations.AccommodationService;
import org.remipassmoilesel.bookme.accommodations.Type;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.remipassmoilesel.bookme.utils.Utils;
import org.remipassmoilesel.bookme.utils.colors.DefaultColors;
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
public class AccommodationsController {

    private static final String TOKEN_ATTR_SESSION_PREFIX = "accommodation-form";

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private AccommodationService accommodationService;


    @RequestMapping(value = Mappings.ACCOMMODATIONS_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Accommodation> getRooms() throws Exception {
        List<Accommodation> result = accommodationService.getAll(Type.ROOM);
        return result;
    }

    @RequestMapping(value = Mappings.ACCOMMODATIONS_SHOW_ALL, method = RequestMethod.GET)
    public String showAllAccommodations(Model model) throws Exception {

        List<Accommodation> accommodationsList = accommodationService.getAll();

        model.addAttribute("accommodationsList", accommodationsList);

        Mappings.includeMappings(model);
        return Templates.ACCOMMODATIONS_SHOW;
    }

    /**
     * Show reservation form
     *
     * @param request
     * @param accommodationForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.ACCOMMODATIONS_FORM)
    public String showAccommodationForm(
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long accommodationId,
            HttpServletRequest request,
            AccommodationForm accommodationForm,
            Model model) throws IOException {

        if (accommodationId != -1) {
            Accommodation res = accommodationService.getById(accommodationId);
            accommodationForm.load(res);
        }

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        tokenman.addToken(model);
        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        model.addAttribute("accommodationTypes", Type.values());

        DefaultColors.includeDarkColors(model);
        Mappings.includeMappings(model);
        return Templates.ACCOMMODATIONS_FORM;
    }

    @PostMapping(Mappings.ACCOMMODATIONS_FORM)
    public String submitAccommodationForm(
            @Valid AccommodationForm accommodationForm,
            BindingResult accommodationResult,
            Model model,
            HttpServletRequest request) {

        if (accommodationResult.hasErrors()) {

            model.addAttribute("token", accommodationForm.getToken());
            model.addAttribute("errorMessage", "");
            model.addAttribute("accommodationTypes", Type.values());

            DefaultColors.includeDarkColors(model);
            Mappings.includeMappings(model);
            return Templates.ACCOMMODATIONS_FORM;
        }

        String errorMessage = "";
        Accommodation accommodation = null;

        try {
            // checks tokens
            HttpSession session = request.getSession();
            TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

            // check if token is invalid
            tokenman.throwIfTokenInvalid(session, accommodationForm.getToken());

            // always delete token just after
            tokenman.deleteTokenFrom(session);

            Color color = Utils.rgbStringToColor(accommodationForm.getColor());

            // update room
            if (accommodationForm.getId() == -1) {

                try {

                    accommodation = accommodationService.createAccommodation(
                            accommodationForm.getName(),
                            accommodationForm.getPlaces(),
                            accommodationForm.getPricePerDay(),
                            accommodationForm.getComment(),
                            accommodationForm.getType(),
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

                    accommodation = accommodationService.getById(accommodationForm.getId());
                    accommodation.setName(accommodationForm.getName());
                    accommodation.setPlaces(accommodationForm.getPlaces());
                    accommodation.setComment(accommodationForm.getComment());
                    accommodation.setType(accommodationForm.getType());
                    accommodation.setColor(accommodationForm.getColor());
                    accommodation.setPricePerDay(accommodationForm.getPricePerDay());

                    accommodationService.update(accommodation);

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
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("errorMessage", errorMessage);

        DefaultColors.includeDarkColors(model);
        Mappings.includeMappings(model);
        return Templates.ACCOMMODATIONS_FORM;
    }

    /**
     * Delete a resource
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.ACCOMMODATIONS_DELETE)
    public String deleteAccommodation(
            @RequestParam(value = "id", required = true) Long accommodationId,
            @RequestParam(value = "token", required = true) Long token,
            HttpServletRequest request,
            Model model) throws Exception {

        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_ATTR_SESSION_PREFIX);

        String errorMessage = "";

        // check if token is invalid
        tokenman.throwIfTokenInvalid(session, token);

        // token is valid
        // always delete token before leave
        tokenman.deleteTokenFrom(session);

        accommodationService.markAsDeleted(accommodationId);

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "deleted");

        Mappings.includeMappings(model);
        DefaultColors.includeDarkColors(model);
        return Templates.ACCOMMODATIONS_FORM;
    }

    /**
     * Show a calendar
     *
     * @param model
     * @return
     */
    @GetMapping(Mappings.ACCOMMODATIONS_CALENDAR)
    public String showAccommodationCalendar(Model model) throws Exception {

        // list all accommodations
        List<Accommodation> accommodations = accommodationService.getAll();
        model.addAttribute("accommodationsList", accommodations);

        Mappings.includeMappings(model);
        return Templates.ACCOMMODATIONS_CALENDAR;
    }

}
