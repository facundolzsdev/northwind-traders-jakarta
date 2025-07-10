package com.northwind.controller.signup;

import com.northwind.controller.base.BaseSignUpBean;
import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Admin;
import com.northwind.service.AdminSignUpService;
import com.northwind.util.constants.messages.AdminMessages;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.SignUpMessages;
import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.jsf.NavigationUtil;
import com.northwind.util.constants.ViewPaths;
import com.northwind.util.validation.AccountInfoValidator;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean for the admin registration process.
 * <p>
 * Manages the registration form for new admins.
 * Relies on dedicated validator and service components to ensure clean separation of concerns.
 * </p>
 *
 * @see BaseSignUpBean
 * @see AdminSignUpService
 * @see AccountInfoValidator
 */
@Getter
@Setter
@Named("adminSignUp")
@ViewScoped
public class AdminSignUpBean extends BaseSignUpBean implements Serializable {

    private Admin admin;

    @Inject
    private AdminSignUpService signUpService;
    @Inject
    private AccountInfoValidator accountInfoValidator;
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        admin = new Admin();
    }

    /**
     * Completes the registration process for the admin.
     * <p>
     * Performs validation of the account fields, delegates persistence to the service layer,
     * provides user feedback through growl messages, and redirects to the admin dashboard upon success.
     * Gracefully handles both validation failures and unexpected application errors.
     * </p>
     */
    @Override
    public void register() {
        try {
            if (accountInfoValidator.validateNewAccountData(email, username, password, confirmPassword)) {
                return;
            }

            signUpService.registerAdmin(username, email, password, admin);
            GrowlUtil.successAndKeep(
                    GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(AdminMessages.SUCCESS_ADMIN_REGISTRATION, username)
            );

            NavigationUtil.redirectToPageSafely(ViewPaths.Admin.DASHBOARD);
        } catch (SignUpServiceException e) {
            logger.log(Level.SEVERE, "Admin registration failed: " + username, e);
            GrowlUtil.error(GrowlTitles.ERROR_OPERATION_FAILED, SignUpMessages.GENERIC_ERROR_SIGNUP);
        }
    }
}

