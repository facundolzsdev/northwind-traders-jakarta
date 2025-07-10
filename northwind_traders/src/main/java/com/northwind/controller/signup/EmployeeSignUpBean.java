package com.northwind.controller.signup;

import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.controller.base.BaseSignUpBean;
import com.northwind.service.EmployeeSignUpService;
import com.northwind.util.constants.ViewPaths;
import com.northwind.util.constants.messages.AdminMessages;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean for the employee registration process.
 * <p>
 * Manages the multi-step registration form for new employees, including personal
 * and account information. Relies on dedicated validator and service components
 * to ensure clean separation of concerns.
 * </p>
 *
 * @see BaseSignUpBean
 * @see EmployeeSignUpService
 * @see PersonalInfoValidator
 * @see AccountInfoValidator
 */
@Getter
@Setter
@Named("employeeSignUp")
@ViewScoped
public class EmployeeSignUpBean extends BaseSignUpBean implements Serializable {

    private Employee employee; // Holds the personal information of the employee being registered.

    @Inject
    private EmployeeSignUpService signUpService;
    @Inject
    private PersonalInfoValidator personalInfoValidator;
    @Inject
    private AccountInfoValidator accountInfoValidator;
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        employee = new Employee();
    }

    @Override
    public void nextStep() {
        if (!personalInfoValidator.validateEmployeeFields(employee)) {
            return;
        }
        super.nextStep();
    }

    /**
     * Completes the registration process for the employee.
     * <p>
     * Performs account field validation, calls the service layer for persistence,
     * shows feedback to the user, and redirects to the login page on success.
     * Handles both validation and unexpected application errors gracefully.
     * </p>
     */
    @Override
    public void register() {
        try {
            if (accountInfoValidator.validateNewAccountData(email, username, password, confirmPassword)) {
                return;
            }

            signUpService.registerEmployee(username, email, password, employee);
            GrowlUtil.successAndKeep(
                    GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(AdminMessages.SUCCESS_EMPLOYEE_REGISTRATION, username)
            );

            NavigationUtil.redirectToPageSafely(ViewPaths.Admin.DASHBOARD);
        } catch (SignUpServiceException e) {
            logger.log(Level.SEVERE, "Employee registration failed: " + username, e);
            GrowlUtil.error(GrowlTitles.ERROR_OPERATION_FAILED, SignUpMessages.GENERIC_ERROR_SIGNUP);
        }
    }
}
