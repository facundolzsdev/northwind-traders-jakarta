package com.northwind.service;

import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.factory.CustomerFactory;
import com.northwind.service.factory.UserFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CustomerSignUpService {

    @Inject
    private iUserService userService;
    @Inject
    private iCustomerService customerService;
    @Inject
    private iCartService cartService;
    @Inject
    private UserFactory userFactory;
    @Inject
    private CustomerFactory customerFactory;
    @Inject
    private Logger logger;

    public void registerCustomer(String username, String email, String password, Customer customer) {
        try {
            User user = userFactory.createUser(username, email, password);
            userService.registerUser(user, RoleType.CUSTOMER);

            Customer newCustomer = customerFactory.createCustomer(customer, user);
            customerService.saveCustomer(newCustomer);
            cartService.getOrCreateCartForCustomer(newCustomer);

            logger.log(Level.INFO, "Customer registered successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during customer registration: " + username, e);
            throw new SignUpServiceException("An unexpected error occurred during Customer registration.", e);
        }
    }

}
