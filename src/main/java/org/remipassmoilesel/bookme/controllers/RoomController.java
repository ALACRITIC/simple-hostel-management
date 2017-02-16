package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.remipassmoilesel.bookme.sharedresources.CreateRoomForm;
import org.remipassmoilesel.bookme.sharedresources.SharedResource;
import org.remipassmoilesel.bookme.sharedresources.SharedResourceService;
import org.remipassmoilesel.bookme.sharedresources.Type;
import org.remipassmoilesel.bookme.utils.TokenManager;
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
import java.util.List;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
@Controller
public class RoomController {

    private static final String TOKEN_NAME = "room";

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private SharedResourceService resourceService;

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = Mappings.ROOMS_JSON_GET, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SharedResource> getRooms() throws Exception {
        List<SharedResource> result = resourceService.getAll(Type.ROOM);
        return result;
    }

    @RequestMapping(value = Mappings.ROOMS_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<SharedResource> roomsList = resourceService.getAll(Type.ROOM);

        model.addAttribute("roomsList", roomsList);

        Mappings.includeMappings(model);
        return Templates.ROOMS_SHOW;
    }

    /**
     * Show reservation form
     *
     * @param request
     * @param roomForm
     * @param model
     * @return
     */
    @GetMapping(Mappings.ROOMS_FORM)
    public String showForm(
            HttpServletRequest request,
            CreateRoomForm roomForm,
            Model model) {

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        tokenman.addToken(model);
        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return Templates.ROOMS_FORM;
    }

    @PostMapping(Mappings.ROOMS_FORM)
    public String submitRoom(
            @Valid CreateRoomForm roomForm,
            BindingResult roomResults,
            Model model,
            HttpServletRequest request) {

        if (roomResults.hasErrors()) {
            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", roomForm.getToken());
            model.addAttribute("errorMessage", "");

            Mappings.includeMappings(model);
            return Templates.ROOMS_FORM;
        }

        SharedResource room = null;

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";

        // token is invalid
        if (tokenman.isTokenValid(session, roomForm.getToken()) == false) {
            errorMessage = "Invalid session";
        }

        // token is valid
        else {

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            try {
                room = resourceService.createRoom(
                        roomForm.getRoomName(),
                        roomForm.getPlaces(),
                        roomForm.getRoomComment(),
                        Type.ROOM
                );

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("room", room);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.ROOMS_COMPLETED;
    }

}
