package com.northwind.controller.base;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Abstract base bean for for user sign-up backing beans.
 * <p>
 * Encapsulates shared form fields and common logic used in multi-step
 * registration processes for different user types (e.g., Customer, Employee).
 * </p>
 *
 * <p>
 * Provides utility methods to navigate between registration steps and
 * validate input fields. Subclasses must implement the actual registration
 * logic in the {@link #register()} method.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public abstract class BaseSignUpBean implements Serializable {

    protected String email;
    protected String username;
    protected String password;
    protected String confirmPassword;
    protected boolean step1 = true;

    /**
     * Proceeds to the next step in the sign-up process.
     */
    public void nextStep() {
        this.step1 = false;
    }

    /**
     * Returns to the previous step in the sign-up process.
     */
    public void previousStep() {
        this.step1 = true;
    }

    /**
     * Validates that none of the provided fields are null or empty (after trimming).
     *
     * @param fields one or more string fields to validate
     * @return {@code true} if all fields are non-null and non-empty; {@code false} otherwise
     */
    protected boolean areFieldsValid(String... fields) {
        return Stream.of(fields).allMatch(field -> field != null && !field.trim().isEmpty());
    }

    public abstract void register();
}