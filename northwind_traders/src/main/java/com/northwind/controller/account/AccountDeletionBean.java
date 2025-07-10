package com.northwind.controller.account;

import com.northwind.context.AdminContext;
import com.northwind.context.AuthenticatedUserContext;
import com.northwind.context.CustomerContext;
import com.northwind.service.iAdminService;
import com.northwind.service.iCustomerAccountRemovalService;
import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.jsf.NavigationUtil;

import static com.northwind.util.exception.BeanExceptionUtil.*;

import com.northwind.util.constants.messages.DataUpdateMessages;
import com.northwind.util.constants.messages.GrowlTitles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean responsible for handling account deletion requests
 * for authenticated customers and administrators.
 */
@Named("accountDeletion")
@RequestScoped
public class AccountDeletionBean implements Serializable {

    @Inject
    private AuthenticatedUserContext authUserContext;
    @Inject
    private iCustomerAccountRemovalService customerAcctDelService;
    @Inject
    private iAdminService adminService;
    @Inject
    private CustomerContext customerContext;
    @Inject
    private AdminContext adminContext;
    @Inject
    private Logger logger;

    /**
     * Deletes the currently authenticated account based on its role (customer or admin),
     * then invalidates the session and redirects the user to the login page.
     *
     * <p>In case of an error, a message is logged and a generic error message is shown to the user.</p>
     */
    public void deleteAuthenticatedAccount() {
        try {
            if (authUserContext.isCustomer()) {
                Integer customerId = customerContext.requireCustomer().getId();
                customerAcctDelService.deleteCustomerAccount(customerId);
            } else if (authUserContext.isAdmin()) {
                Integer adminId = adminContext.requireAdmin().getId();
                adminService.deleteAdmin(adminId);
            }

            GrowlUtil.successAndKeep(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    DataUpdateMessages.ACCOUNT_DELETION_SUCCESS);

            NavigationUtil.invalidateSessionAndRedirectToLogin();
        } catch (Exception e) {
            handle(logger, "deleting user account", e,
                    DataUpdateMessages.ACCOUNT_DELETION_FAILURE, Level.SEVERE);
        }
    }

}
