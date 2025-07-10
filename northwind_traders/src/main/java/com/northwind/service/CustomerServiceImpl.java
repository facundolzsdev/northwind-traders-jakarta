package com.northwind.service;

import com.northwind.exceptions.CustomerNotFoundException;
import com.northwind.exceptions.CustomerServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.User;
import com.northwind.repository.iCustomerRepository;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CustomerServiceImpl implements iCustomerService {

    @Inject
    private iCustomerRepository customerRepo;
    @Inject
    private iUserService userService;
    @Inject
    private Logger logger;

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return Optional.ofNullable(customerRepo.findById(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public void saveCustomer(Customer customer) {
        try {
            customerRepo.save(customer);
            logger.log(Level.INFO, "Customer saved with ID: {0}", customer.getId());
        } catch (Exception e) {
            handleException("saving customer: " + customer.getFullName(), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteCustomer(Integer id) {
        try {
            Customer customer = getByIdWithUser(id)
                    .orElseThrow(() -> new CustomerServiceException("Customer not found. ID: " + id));
            User user = customer.getUser();

            customerRepo.delete(id);
            userService.deleteUser(user.getId());
            logger.info("Customer and associated user deleted — Customer ID: " + customer.getId() +
                    ", User ID: " + customer.getUser().getId());
        } catch (Exception e) {
            handleException("deleting customer with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Optional<Customer> getByUserId(Integer userId) {
        return Optional.ofNullable(customerRepo.findByUserId(userId));
    }

    @Override
    public Optional<Customer> getByIdWithUser(Integer id) {
        return Optional.ofNullable(customerRepo.findByIdWithUser(id));
    }

    @Override
    public Optional<Customer> getCustomerWithUserByUserId(Integer userId) {
        return Optional.ofNullable(customerRepo.findCustomerWithUserByUserId(userId));
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, CustomerServiceException.class, CustomerNotFoundException.class
        );
    }

}
