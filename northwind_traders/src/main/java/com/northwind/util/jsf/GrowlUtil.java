package com.northwind.util.jsf;

import jakarta.faces.context.FacesContext;
import lombok.experimental.UtilityClass;

/**
 * Utility class for displaying growl messages in JSF views.
 * <p>
 * Provides convenient methods to show success, warning, and error messages,
 * including support for flash scope when redirection is involved.
 * </p>
 */
@UtilityClass
public class GrowlUtil {

    public static void success(String summary, String detail) {
        FacesMessageUtil.addInfoMessage(summary, detail);
    }

    public static void warning(String summary, String detail) {
        FacesMessageUtil.addWarnMessage(summary, detail);
    }

    public static void error(String summary, String detail) {
        FacesMessageUtil.addErrorMessage(summary, detail);
    }

    public static void fromException(String context, Exception e) {
        FacesMessageUtil.addErrorMessage(context, e.getMessage());
    }

    /**
     * Displays a success message and keeps it across a redirect using flash scope.
     * <p>
     * Useful when a message should persist after navigation.
     * </p>
     */
    public static void successAndKeep(String summary, String detail) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        success(summary, detail);
    }
}