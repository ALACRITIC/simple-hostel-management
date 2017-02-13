package org.remipassmoilesel.bookme.utils;

import org.remipassmoilesel.bookme.reservations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
 * Created by remipassmoilesel on 11/02/17.
 */
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    /**
     * Prefix used in session token name
     */
    private static final String SESSION_TOKEN_NAME_PREFIX = "form-token_";

    /**
     * Suffix used to distinguish session tokens
     */
    private final String sessionTokenSuffix;

    /**
     * Token value, must be unique
     */
    private long token;

    /**
     * Unique name of token in session
     */
    private String sessionTokenName;

    /**
     * Name of token field in model, default one is 'token'
     */
    private String modelTokenName;

    public TokenManager(String tokenSuffix) {
        this.sessionTokenSuffix = tokenSuffix;
        this.modelTokenName = "token";
        generateToken();
        generateSessionTokenName();
    }

    /**
     * Generate a unique token value
     */
    public void generateToken() {
        this.token = System.currentTimeMillis();
    }

    /**
     * Generate a unique name for session token
     */
    private void generateSessionTokenName() {
        this.sessionTokenName = (SESSION_TOKEN_NAME_PREFIX + sessionTokenSuffix).trim().toLowerCase();
    }

    /**
     * Add token value to provided model. Value can be accessed later with getModelTokenName() value.
     *
     * @param model
     */
    public void addToken(Model model) {
        model.addAttribute(modelTokenName, token);
    }

    /**
     * Add token value to provided http session. Value of token can be accessed later with getSessionTokenName() value.
     *
     * @param session
     */
    public void addToken(HttpSession session) {
        session.setAttribute(sessionTokenName, token);
    }

    /**
     * Return current token value
     *
     * @return
     */
    public long getToken() {
        return token;
    }

    /**
     * Return current value of token from provided session, or -1 if no value is found
     *
     * @param session
     * @return
     */
    public long getTokenFrom(HttpSession session) {
        try {
            return (long) session.getAttribute(sessionTokenName);
        } catch (Exception e) {
            logger.error("Error while getting token: ", e);
            return -1;
        }
    }

    /**
     * Check specified session token against specified value
     *
     * @param session
     * @param toCheck
     * @return
     */
    public boolean isTokenValid(HttpSession session, Long toCheck) {
        Long sessionToken = getTokenFrom(session);
        return sessionToken != null && sessionToken.equals(Long.valueOf(toCheck));
    }

    /**
     * Delete token from specified http session
     *
     * @param session
     */
    public void deleteTokenFrom(HttpSession session) {

        if (session.getAttribute(sessionTokenName) == null) {
            throw new RuntimeException("Token not found: " + session);
        }

        session.setAttribute(sessionTokenName, null);
    }

    public String getModelTokenName() {
        return modelTokenName;
    }

    public void setModelTokenName(String modelTokenName) {
        this.modelTokenName = modelTokenName;
    }

    public String getSessionTokenName() {
        return sessionTokenName;
    }

    public void setSessionTokenName(String sessionTokenName) {
        this.sessionTokenName = sessionTokenName;
    }
}
