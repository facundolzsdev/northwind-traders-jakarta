package com.northwind.service;

import com.northwind.model.entity.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling customer-related business logic.
 */
public interface iCustomerService {

    Optional<Customer> getCustomerById(Integer id);

    List<Customer> getAllCustomers();

    void saveCustomer(Customer customer);

    void deleteCustomer(Integer id);

    Optional<Customer> getByUserId(Integer userId);

    Optional<Customer> getByIdWithUser(Integer id);

    Optional<Customer> getCustomerWithUserByUserId(Integer userId);
}
