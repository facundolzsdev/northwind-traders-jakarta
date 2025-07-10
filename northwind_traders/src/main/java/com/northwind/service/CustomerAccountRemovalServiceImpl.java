package com.northwind.service;

import com.northwind.exceptions.AccountDeletionException;
import com.northwind.exceptions.CustomerNotFoundException;
import com.northwind.model.entity.Order;
import com.northwind.model.enums.OrderArchivedReason;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CustomerAccountRemovalServiceImpl implements iCustomerAccountRemovalService {

    @Inject
    private iCustomerService customerService;
    @Inject
    private iUserService userService;
    @Inject
    private iCartService cartService;
    @Inject
    private iOrderService orderService;
    @Inject
    private iHistoricalOrderService historicalOrderService;
    @Inject
    private Logger logger;

    @Override
    public void deleteCustomerAccount(Integer customerId) {
        try {
            validateCustomerExists(customerId);
            archiveAndDeleteCustomerOrders(customerId);
            deleteCartAndLog(customerId);
            deleteCustomerAndLog(customerId);
        } catch (Exception e) {
            handleException("deleting customer account with ID: " + customerId, e, Level.SEVERE);
        }
    }

    private void validateCustomerExists(Integer customerId) {
        customerService.getCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("No Customer found with ID: " + customerId));
    }

    private void archiveAndDeleteCustomerOrders(Integer customerId) {
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        for (Order order : orders) {
            historicalOrderService.archiveOrder(order, order.getOrderDetails(),
                    OrderArchivedReason.CLIENTE_ELIMINADO);
            orderService.deleteOrderAsSystem(order.getId());
        }
    }

    private void deleteCartAndLog(Integer customerId) {
        cartService.deleteCartByCustomerId(customerId);
        logger.log(Level.INFO, "Cart deleted for customer ID: {0}", customerId);
    }

    private void deleteCustomerAndLog(Integer customerId) {
        customerService.deleteCustomer(customerId);
        logger.log(Level.INFO, "Customer and user deleted for customer ID: {0}", customerId);
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, AccountDeletionException.class, CustomerNotFoundException.class
        );
    }
}