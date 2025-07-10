package com.northwind.util.validation;

import com.northwind.model.dto.UserAccountProfileDTO;
import com.northwind.service.iUserService;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.UserAccountMessages;
import com.northwind.util.jsf.GrowlUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Utility class that validates user account information including emails, usernames and passwords.
 * <p>
 * Handles both registration and update scenarios with conditional validation.
 * </p>
 */
@ApplicationScoped
public class AccountInfoValidator {

    @Inject
    private iUserService userService;

    /**
     * Validates complete account profile during updates.
     * Only validates fields that were actually modified.
     *
     * @param account DTO containing current and original values
     * @return true if all modified fields are valid
     */
    public boolean validateAccountProfile(UserAccountProfileDTO account) {
        if (account == null) {
            return false;
        }

        if (!hasChanges(account)) {
            return true;
        }

        return validateAccountDataForUpdate(
                account.getEmail(),
                account.getOriginalEmail(),
                account.getUsername(),
                account.getOriginalUsername(),
                account.getPassword(),
                account.getConfirmPassword()
        );
    }

    /**
     * Validates email for new account registration.
     * Checks both format and availability.
     */
    public boolean validateNewEmail(String email) {
        return validateEmailFormat(email) &&
                isEmailAvailable(email, null);
    }

    /**
     * Validates username for new account registration.
     * Checks both format and availability.
     */
    public boolean validateNewUsername(String username) {
        return validateUsernameFormat(username) &&
                isUsernameAvailable(username, null);
    }

    /**
     * Validates complete new account data (registration flow).
     */
    public boolean validateNewAccountData(String email, String username,
                                          String password, String confirmPassword) {
        return !validateNewEmail(email) ||
                !validateNewUsername(username) ||
                !validatePassword(password) ||
                !validatePasswordConfirmation(password, confirmPassword);
    }

    /**
     * Validates account data during updates with conditional checks.
     */
    public boolean validateAccountDataForUpdate(String email, String originalEmail,
                                                String username, String originalUsername,
                                                String password, String confirmPassword) {
        return validateEmailForUpdate(email, originalEmail) &&
                validateUsernameForUpdate(username, originalUsername) &&
                validatePasswordIfPresent(password, confirmPassword);
    }

    /**
     * Validates email format and availability (if changed).
     */
    public boolean validateEmailForUpdate(String email, String originalEmail) {
        return validateEmailFormat(email) &&
                isEmailAvailable(email, originalEmail);
    }

    /**
     * Validates username format and availability (if changed).
     */
    public boolean validateUsernameForUpdate(String username, String originalUsername) {
        return validateUsernameFormat(username) &&
                isUsernameAvailable(username, originalUsername);
    }

    /**
     * Validates password meets complexity requirements.
     */
    public boolean validatePassword(String password) {
        return validateField(password, ValidationPatterns.PASSWORD,
                UserAccountMessages.INVALID_PASSWORD);
    }

    /**
     * Validates password confirmation matches.
     */
    public boolean validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, UserAccountMessages.PASSWORDS_DO_NOT_MATCH);
            return false;
        }
        return true;
    }

    /**
     * Checks if any account field has been modified.
     */
    private boolean hasChanges(UserAccountProfileDTO account) {
        return !account.getEmail().equals(account.getOriginalEmail()) ||
                !account.getUsername().equals(account.getOriginalUsername()) ||
                (account.getPassword() != null && !account.getPassword().isEmpty());
    }

    /**
     * Validates email format only.
     */
    private boolean validateEmailFormat(String email) {
        return validateField(email, ValidationPatterns.EMAIL,
                UserAccountMessages.INVALID_EMAIL_ADDRESS);
    }

    /**
     * Validates username format only.
     */
    private boolean validateUsernameFormat(String username) {
        return validateField(username, ValidationPatterns.USERNAME,
                UserAccountMessages.INVALID_USERNAME);
    }

    /**
     * Validates password only if provided.
     */
    private boolean validatePasswordIfPresent(String password, String confirmation) {
        if (password == null || password.trim().isEmpty()) {
            return true;
        }
        return validatePassword(password) &&
                validatePasswordConfirmation(password, confirmation);
    }

    /**
     * Checks email availability if different from original.
     */
    private boolean isEmailAvailable(String email, String originalEmail) {
        if (Objects.equals(email, originalEmail)) {
            return true;
        }
        return isFieldTaken(email, userService::emailExists,
                UserAccountMessages.EMAIL_ALREADY_EXISTS);
    }

    /**
     * Checks username availability if different from original.
     */
    private boolean isUsernameAvailable(String username, String originalUsername) {
        if (Objects.equals(username, originalUsername)) {
            return true;
        }
        return isFieldTaken(username, userService::usernameExists,
                UserAccountMessages.USERNAME_ALREADY_EXISTS);
    }

    /**
     * Generic field validation against pattern.
     */
    private boolean validateField(String value, Pattern pattern, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, GrowlTitles.WARN_INCOMPLETE_FORM_FIELDS);
            return false;
        }
        if (!pattern.matcher(value).matches()) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Checks if a field value is already taken.
     */
    private boolean isFieldTaken(String field, Function<String, Boolean> existsFunction,
                                 String errorMessage) {
        if (existsFunction.apply(field)) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, errorMessage);
            return false;
        }
        return true;
    }
}