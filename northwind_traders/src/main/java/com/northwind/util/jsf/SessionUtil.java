package com.northwind.util.jsf;

import com.northwind.model.entity.User;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;

import java.util.logging.Logger;

/**
 * Utility class for storing and retrieving user-related session attributes.
 * <p>
 * Centralizes session access logic for clarity and reusability.
 * </p>
 */
@UtilityClass
public class SessionUtil {

    private static final Logger logger = Logger.getLogger(SessionUtil.class.getName());

    // Session attribute keys
    private static final String ATTR_USER_ID = "userId";
    private static final String ATTR_USERNAME = "username";
    private static final String ATTR_ROLE = "role";
    private static final String ATTR_USER_TYPE = "userType";
    private static final String ATTR_LOGGED_USER = "loggedUser";

    private static HttpSession getSession(boolean create) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            logger.severe("FacesContext is null. Cannot access session.");
            return null;
        }

        ExternalContext ec = context.getExternalContext();
        return (HttpSession) ec.getSession(create);
    }

    /**
     * Stores the given user's key data in session attributes.
     *
     * @param user The authenticated user
     */
    public static void storeUserSession(User user) {
        HttpSession session = getSession(true);
        if (session == null) {
            logger.severe("Session is null. Cannot store user.");
            return;
        }
        session.setAttribute(ATTR_USER_ID, user.getId());
        session.setAttribute(ATTR_USERNAME, user.getUsername());
        session.setAttribute(ATTR_ROLE, user.getRole().getName());
        session.setAttribute(ATTR_USER_TYPE, user.getRole().getName());
        session.setAttribute(ATTR_LOGGED_USER, user);
    }

    /**
     * Retrieves the logged user's ID from the session.
     *
     * @return The user ID or null if not found
     */
    public static Integer getUserIdFromSession() {
        HttpSession session = getSession(false);
        if (session == null) return null;
        Object userId = session.getAttribute(ATTR_USER_ID);
        return (userId instanceof Integer) ? (Integer) userId : null;
    }

    /**
     * Retrieves the full User object stored in the session.
     *
     * @return The logged-in User, or null if not available
     */
    public static User getLoggedUserFromSession() {
        HttpSession session = getSession(false);
        if (session == null) return null;
        Object user = session.getAttribute(ATTR_LOGGED_USER);
        return (user instanceof User) ? (User) user : null;
    }
}
