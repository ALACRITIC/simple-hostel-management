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

    public static final String DEFAULT_MODEL_TOKEN_NAME = "token";

    /**
     * Prefix used in session token name
     */
    public static final String SESSION_TOKEN_NAME_PREFIX = "token_";

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
     *
     * Several tokens from several actions can be used separately
     */
    private String sessionTokenName;

    /**
     * Name of token field in model, default one is 'token'
     */
    private String modelTokenName;

    public TokenManager(String tokenSuffix) {
        this.sessionTokenSuffix = tokenSuffix;
        this.modelTokenName = DEFAULT_MODEL_TOKEN_NAME;
        generateToken();
        this.sessionTokenName = generateSessionTokenName(tokenSuffix);
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
    public static String generateSessionTokenName(String sessionTokenSuffix) {
        return (SESSION_TOKEN_NAME_PREFIX + sessionTokenSuffix).trim().toLowerCase();
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
            return Long.valueOf(session.getAttribute(sessionTokenName).toString());
        } catch (Exception e) {
            logger.error("Error while getting token: ", e);
            return -1;
        }
    }

    public long getTokenFrom(Model model) {
        try {
            return Long.valueOf(model.asMap().get(modelTokenName).toString());
        } catch (Exception e) {
            logger.error("Error while getting token: ", e);
            return -1;
        }
    }

    /**
     * Check specified session token against specified value
     *
     * @param session
     * @param model
     * @return
     */
    public boolean isTokenValid(HttpSession session, Model model) {
        Long sessionToken = getTokenFrom(session);
        Long modelToken = getTokenFrom(model);
        return isTokenValid(sessionToken, modelToken);
    }

    public boolean isTokenValid(HttpSession session, Long toCheck) {
        Long sessionToken = getTokenFrom(session);
        return isTokenValid(sessionToken, toCheck);
    }

    public boolean isTokenValid(Long sessionToken, Long toCheckToken) {
        return sessionToken != null && toCheckToken != null &&
                sessionToken.equals(toCheckToken);
    }

    public void throwIfTokenInvalid(HttpSession session, Long toCheck) {
        if (isTokenValid(session, toCheck) == false) {
            logger.error("Invalid token: " + toCheck);
            throw new IllegalStateException("Invalid form, please update form and try again");
        }
    }

    /**
     * Delete token from specified http session
     *
     * @param session
     */
    public void deleteTokenFrom(HttpSession session) {

        if (session.getAttribute(sessionTokenName) == null) {
            logger.error("Token not found: " + session);
        } else {
            session.setAttribute(sessionTokenName, null);
        }

    }

    /**
     * Delete token from specified model
     *
     * @param model
     */
    public void deleteTokenFrom(Model model) {

        if (model.asMap() == null || model.asMap().get(modelTokenName) == null) {
            logger.error("Token not found: " + model);
        } else {
            model.asMap().put(modelTokenName, null);
        }

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
