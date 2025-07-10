package com.northwind.controller.profile;

import com.northwind.context.AuthenticatedUserContext;
import com.northwind.controller.base.BaseProfileBean;
import com.northwind.exceptions.CustomerServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.model.dto.CustomerProfileDTO;
import com.northwind.service.iCustomerService;
import com.northwind.util.validation.PersonalInfoValidator;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.util.logging.Logger;

/**
 * Managed bean responsible for managing the profile of a customer,
 * including loading and updating personal information.
 * <p>
 * Extends {@link BaseProfileBean} to reuse common profile operations.
 */
@Getter
@Setter
@Named("customerProfile")
@ViewScoped
public class CustomerProfileBean extends BaseProfileBean<Customer, CustomerProfileDTO> {

    @Inject
    private iCustomerService customerService;
    @Inject
    private AuthenticatedUserContext authUserContext;
    @Inject
    private PersonalInfoValidator validator;
    @Inject
    private Logger logger;

    @Override
    protected boolean validate() {
        return validator.validateCustomerProfile(dto);
    }

    @Override
    protected void persist(Customer customer) throws CustomerServiceException {
        customerService.saveCustomer(customer);
    }

    @Override
    protected Integer getEntityId() {
        return originalEntity != null ? originalEntity.getId() : null;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected void loadEntity() {
        customerService.getByUserId(authUserContext.getUserId())
                .ifPresentOrElse(this::initializeCustomer,
                        () -> handleLoadFailure("Customer not found for user ID: " + authUserContext.getUserId()));
    }

    @Override
    protected void applyChanges(CustomerProfileDTO dto, Customer customer) {
        customer.setFullName(dto.getFullName());
        customer.setAddress(dto.getAddress());
        customer.setCountry(dto.getCountry());
        customer.setCity(dto.getCity());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setPostalCode(dto.getPostalCode());
    }

    @Override
    protected CustomerProfileDTO mapToDTO(Customer customer) {
        return CustomerProfileDTO.builder()
                .fullName(customer.getFullName())
                .address(customer.getAddress())
                .country(customer.getCountry())
                .city(customer.getCity())
                .phoneNumber(customer.getPhoneNumber())
                .postalCode(customer.getPostalCode())
                .build();
    }

    private void initializeCustomer(Customer customer) {
        this.originalEntity = customer;
        this.dto = mapToDTO(customer);
    }
}