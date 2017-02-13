package org.remipassmoilesel.bookme.controllers;

import org.remipassmoilesel.bookme.Mappings;
import org.remipassmoilesel.bookme.Templates;
import org.remipassmoilesel.bookme.messages.CreateMessageForm;
import org.remipassmoilesel.bookme.messages.Message;
import org.remipassmoilesel.bookme.messages.MessageService;
import org.remipassmoilesel.bookme.utils.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = Mappings.MESSAGES_SHOW_ALL, method = RequestMethod.GET)
    public String showAll(Model model) throws Exception {

        List<Message> messagesList = messageService.getAll();

        model.addAttribute("messagesList", messagesList);

        Mappings.includeMappings(model);
        return Templates.MESSAGE_SHOW_ALL;
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
            CreateMessageForm messageForm,
            Model model) {

        // create a token and add it to model
        TokenManager tokenman = new TokenManager(TOKEN_NAME);

        tokenman.addToken(model);
        model.addAttribute("errorMessage", "");

        // add it to session for check
        HttpSession session = request.getSession();
        tokenman.addToken(session);

        Mappings.includeMappings(model);
        return Templates.MESSAGE_FORM;
    }

    @PostMapping(Mappings.MESSAGES_FORM)
    public String submitRoom(
            @Valid CreateMessageForm messageForm,
            BindingResult messageResults,
            Model model,
            HttpServletRequest request) {

        if (messageResults.hasErrors()) {
            //System.out.println(reservationResults.getAllErrors());
            model.addAttribute("token", messageForm.getToken());
            model.addAttribute("errorMessage", "");

            Mappings.includeMappings(model);
            return Templates.MESSAGE_FORM;
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
                message = messageService.createMessage(messageForm.getMessage());

            } catch (Exception e) {
                logger.error("Error while creating reservation", e);
                errorMessage = e.getMessage();
            }

        }

        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);

        Mappings.includeMappings(model);
        return Templates.MESSAGE_COMPLETED;
    }

}
