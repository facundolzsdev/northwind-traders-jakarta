package com.northwind.controller.signup;

import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.controller.base.BaseSignUpBean;
import com.northwind.service.CustomerSignUpService;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.SignUpMessages;
import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.jsf.NavigationUtil;
import com.northwind.util.validation.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Managed bean for the customer registration process.
 * <p>
 * Manages the multi-step form for customer sign-up, including personal data
 * and account credentials. Delegates validation and persistence logic to the
 * appropriate service and validator components.
 * </p>
 *
 * @see BaseSignUpBean
 * @see CustomerSignUpService
 * @see PersonalInfoValidator
 * @see AccountInfoValidator
 */
@Getter
@Setter
@Named("customerSignUp")
@ViewScoped
public class CustomerSignUpBean extends BaseSignUpBean implements Serializable {

    private Customer customer; // Holds the personal information of the customer being registered.

    @Inject
    private CustomerSignUpService signUpService;
    @Inject
    private PersonalInfoValidator personalInfoValidator;
    @Inject
    private AccountInfoValidator accountInfoValidator;
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        customer = new Customer();
    }

    @Override
    public void nextStep() {
        if (!personalInfoValidator.validateCustomerFields(customer)) {
            return;
        }
        super.nextStep();
    }

    /**
     * Performs the full registration process for the customer.
     * <p>
     * Validates account credentials, delegates persistence to the service layer,
     * shows a success message and redirects to the login page on success.
     * Handles validation errors and unexpected failures gracefully.
     * </p>
     */
    @Override
    public void register() {
        try {
            if (accountInfoValidator.validateNewAccountData(email, username, password, confirmPassword)) {
                return;
            }

            signUpService.registerCustomer(username, email, password, customer);

            GrowlUtil.successAndKeep(
                    GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(SignUpMessages.SUCCESS_REGISTRATION, username)
            );

            NavigationUtil.redirectToLogin();
        } catch (SignUpServiceException e) {
            logger.log(Level.SEVERE, "Customer registration failed: " + username, e);
            GrowlUtil.error(GrowlTitles.ERROR_OPERATION_FAILED, SignUpMessages.GENERIC_ERROR_SIGNUP);
        }
    }

}
