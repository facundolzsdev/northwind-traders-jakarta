package com.northwind.service.factory;

import com.northwind.model.support.Audit;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.User;

import static com.northwind.util.general.InputSanitizer.*;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for creating Customer entities with proper data sanitization.
 * Ensures all string fields are sanitized before entity creation to prevent injection attacks.
 */
@ApplicationScoped
public class CustomerFactory {

    /**
     * Creates a new Customer entity from the provided data and associated User.
     * Applies sanitization to all relevant string fields and initializes audit information.
     *
     * @param customer the original customer data to base the new entity on.
     * @param user     the associated User entity for login credentials.
     * @return a new sanitized Customer entity ready for persistence.
     */
    public Customer createCustomer(Customer customer, User user) {
        Customer newCustomer = new Customer();
        newCustomer.setFullName(sanitizeText(customer.getFullName()));
        newCustomer.setCountry(sanitizeText(customer.getCountry()));
        newCustomer.setCity(sanitizeText(customer.getCity()));
        newCustomer.setPostalCode(customer.getPostalCode());
        newCustomer.setAddress(sanitizeAlphaNumericInput(customer.getAddress()));
        newCustomer.setPhoneNumber(customer.getPhoneNumber());
        newCustomer.setUser(user);
        newCustomer.setAudit(Audit.createDefaultAudit());
        return newCustomer;
    }

}
