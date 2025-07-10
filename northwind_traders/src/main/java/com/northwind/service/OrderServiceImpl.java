package com.northwind.service;

import com.northwind.exceptions.CustomerNotFoundException;
import com.northwind.exceptions.OrderNotFoundException;
import com.northwind.exceptions.OrderServiceException;
import com.northwind.model.entity.*;
import com.northwind.model.enums.OrderStatus;
import com.northwind.repository.iOrderRepository;
import com.northwind.service.factory.OrderFactory;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class OrderServiceImpl implements iOrderService {

    @Inject
    private iOrderRepository orderRepo;
    @Inject
    private OrderFactory orderFactory;
    @Inject
    private iCustomerService customerService;
    @Inject
    private iProductService productService;
    @Inject
    private Logger logger;

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        return Optional.ofNullable(orderRepo.findById(id));
    }

    @Override
    public void saveOrder(Order order, List<OrderDetail> details) {
        try {
            associateOrderDetails(order, details);
            orderRepo.save(order);
        } catch (Exception e) {
            handleException("saving order", e, Level.SEVERE);
        }
    }

    @Override
    public void deleteOrder(Integer id) {
        Order order = orderRepo.findById(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        orderRepo.delete(id);
        logger.log(Level.INFO, "Order deleted with ID: {0}", id);
    }

    @Override
    public List<Order> getOrdersByCustomer(Integer customerId) {
        return orderRepo.findByCustomerId(customerId);
    }

    @Override
    public Optional<Order> getByIdWithDetails(Integer id) {
        return Optional.ofNullable(orderRepo.findByIdWithDetails(id));
    }

    @Override
    public Optional<Order> getByCustomerIdAndOrderId(Integer customerId, Integer orderId) {
        try {
            Order order = orderRepo.findByCustomerIdAndOrderId(customerId, orderId);
            return Optional.ofNullable(order);
        } catch (Exception e) {
            handleException("retrieving customer's order with details", e, Level.SEVERE);
            throw new OrderServiceException("Error retrieving the order.");
        }
    }

    @Override
    public List<Order> getProcessedOrdersByCustomerId(Integer customerId) {
        return orderRepo.findProcessedOrdersByCustomerId(customerId);
    }

    @Override
    public List<Order> getAllPendingWithDetails() {
        return orderRepo.findAllPendingWithDetails();
    }

    @Override
    public Order persistNewOrderForCustomer(Integer customerId, List<OrderDetail> details) {
        try {
            Customer customer = customerService.getCustomerById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("No Customer found with ID: " + customerId));
            Order order = orderFactory.createOrder(customer);

            for (OrderDetail detail : details) {
                detail.setOrder(order);
            }

            order.setOrderDetails(details);
            order.calculateTotal();

            orderRepo.save(order);
            return order;
        } catch (Exception e) {
            throw new OrderServiceException("Error creating order for customer " + customerId, e);
        }
    }

    @Override
    public void cancelOwnOrderByCustomer(Integer orderId, Integer customerId)
            throws OrderServiceException {
        try {
            Order order = getByCustomerIdAndOrderId(customerId, orderId)
                    .orElseThrow(() -> new OrderNotFoundException(
                            "Order with ID " + orderId + " does not exist or does not belong to the customer."));

            orderRepo.delete(order.getId());
        } catch (Exception e) {
            handleException("canceling the customer's order", e, Level.WARNING);
            throw new OrderServiceException("Error canceling the order.");
        }
    }

    @Override
    public void approveOrder(Integer orderId, Employee employee) {
        try {
            Order order = getOrderById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
            order.setStatus(OrderStatus.COMPLETADO);

            decrementStockForPurchasedProducts(order.getOrderDetails());
            order.setEmployee(employee);
            orderRepo.save(order);
        } catch (Exception e) {
            throw new OrderServiceException("Error completing order with ID: " + orderId, e);
        }
    }

    @Override
    public void rejectOrder(Integer orderId, Employee employee) {
        try {
            Order order = getOrderById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
            order.setStatus(OrderStatus.RECHAZADO);
            order.setEmployee(employee);
            orderRepo.save(order);
        } catch (Exception e) {
            throw new OrderServiceException("Error rejecting order with ID: " + orderId, e);
        }
    }

    @Override
    public void deleteOrderAsSystem(Integer orderId) throws OrderServiceException {
        try {
            Order order = getByIdWithDetails(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            orderRepo.delete(order.getId());
        } catch (Exception e) {
            handleException("deleting the order with details", e, Level.SEVERE);
            throw new OrderServiceException("Error deleting the order.");
        }
    }

    private void associateOrderDetails(Order order, List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            detail.setOrder(order);
        }
        order.setOrderDetails(details);
    }

    private void decrementStockForPurchasedProducts(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            Product product = productService.requireProductById(detail.getProduct().getId());
            updateStockAndAvailability(product, detail.getProductQuantity());
        }
    }

    private void updateStockAndAvailability(Product product, short purchasedQty) {
        if (product.getStock() < purchasedQty) {
            throw new OrderServiceException("Insufficient stock for product: " + product.getName());
        }

        short newStock = (short) (product.getStock() - purchasedQty);
        product.setStock(newStock);

        if (newStock == 0) {
            product.setActive(false);
            logger.log(Level.INFO, "Product ID {0} ({1}) has been deactivated due to zero stock.",
                    new Object[]{product.getId(), product.getName()});
        }

        productService.saveProduct(product);
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, OrderServiceException.class, null);
    }

}
