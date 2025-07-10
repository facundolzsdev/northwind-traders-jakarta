package com.northwind.util.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.experimental.UtilityClass;

/**
 * Utility class for displaying JSF with different severity levels.
 * <p>
 * Provides static methods for showing info, warning, and error messages in the UI.
 * </p>
 */
@UtilityClass
public class FacesMessageUtil {

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void addInfoMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    public void addWarnMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    }

    public void addErrorMessage(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }
}
