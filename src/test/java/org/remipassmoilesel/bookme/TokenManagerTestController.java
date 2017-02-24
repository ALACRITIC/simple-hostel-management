package org.remipassmoilesel.bookme;

import org.remipassmoilesel.bookme.utils.TokenManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Controller
public class TokenManagerTestController {

    @RequestMapping(value = TokenManagerTest.TEST_MAPPING, method = RequestMethod.GET)
    public void testController(HttpSession session, Model model) {

        TokenManager tokenman = new TokenManager("suffix");
        tokenman.addToken(session);
        tokenman.addToken(model);

        assertTrue(tokenman.getTokenFrom(session) != -1);
        assertTrue(tokenman.getTokenFrom(model) != -1);
        assertTrue(tokenman.isTokenValid(session, model));

        tokenman.deleteTokenFrom(session);
        assertFalse(tokenman.isTokenValid(session, model));

        tokenman.addToken(session);
        tokenman.deleteTokenFrom(model);
        assertFalse(tokenman.isTokenValid(session, model));

        tokenman.addToken(model);
    }

}