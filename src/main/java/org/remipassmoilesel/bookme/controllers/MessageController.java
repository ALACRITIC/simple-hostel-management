package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageForm;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.utils.PaginationUtil;
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
public class MessageController {

    private static final String TOKEN_NAME = "message";

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = Mappings.MESSAGES_SHOW_BY_ID, method = RequestMethod.GET)
    public String showById(
            @RequestParam("id") Long id,
            Model model) throws Exception {

        Message message = messageService.getById(id);

        model.addAttribute("message", message);

        Mappings.includeMappings(model);
        return Templates.MESSAGES_SHOW_ONE;
    }

    @RequestMapping(value = Mappings.MESSAGES_SHOW_LATEST, method = RequestMethod.GET)
    public String showAll(
            @RequestParam(value = "limit", required = false, defaultValue = "5") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset,
            Model model) throws Exception {

        List<Message> messagesList = messageService.getAll(limit, offset);

        model.addAttribute("messagesList", messagesList);

        PaginationUtil pu = new PaginationUtil(Mappings.MESSAGES_SHOW_LATEST, limit, offset);
        pu.addLinks(model);

        Mappings.includeMappings(model);
        return Templates.MESSAGES_SHOW_LATEST;
    }

    @ResponseBody
    @RequestMapping(value = Mappings.MESSAGES_JSON_GET_LATEST, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Message> getMessagesAsJson() throws Exception {

        List<Message> messagesList = messageService.getAll(20l, 0l);
        return messagesList;

    }

    /**
     * Show message form
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping(Mappings.MESSAGES_FORM)
    public String showForm(
            HttpServletRequest request,
            MessageForm messageForm,
            Model model) {

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        tokenman.addToken(model);

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return Templates.MESSAGES_FORM;
    }

    @PostMapping(Mappings.MESSAGES_FORM)
    public String submitRoom(
            @Valid MessageForm messageForm,
            BindingResult messageResults,
            Model model,
            HttpServletRequest request) {

        if (messageResults.hasErrors()) {
            model.addAttribute("token", messageForm.getToken());
            Mappings.includeMappings(model);
            return Templates.MESSAGES_FORM;
        }

        // checks tokens
        HttpSession session = request.getSession();
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        String errorMessage = "";
        Message message = null;

        // token is invalid
        if (tokenman.isTokenValid(session, messageForm.getToken()) == false) {
            errorMessage = "Invalid session";
        }

        // token is valid
        else {

            // always delete token before leave
            tokenman.deleteTokenFrom(session);

            try {
                message = messageService.create(messageForm.getMessage());

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("formstate", "completed");

        Mappings.includeMappings(model);
        return Templates.MESSAGES_FORM;
    }

}
