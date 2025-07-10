package com.northwind.service;

import com.northwind.exceptions.PurchaseServiceException;
import com.northwind.model.entity.Cart;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.service.factory.OrderFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PurchaseServiceImpl implements iPurchaseService {

    @Inject
    private iCustomerService customerService;
    @Inject
    private iCartService cartService;
    @Inject
    private iOrderService orderService;
    @Inject
    private OrderFactory orderFactory;

    @Override
    public Order purchaseCartForUser(Customer customer) {
        try {
            Cart cart = cartService.getOrCreateCartForCustomer(customer);

            validateCart(cart);

            Order order = createAndSaveOrderFromCart(customer, cart);

            cartService.clearCart(cart.getId());

            return order;
        } catch (Exception e) {
            throw new PurchaseServiceException("Error during cart purchase for customer ID " + customer.getId(), e);
        }
    }

    private void validateCart(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new PurchaseServiceException("Cannot process purchase: cart is empty.");
        }
    }

    private Order createAndSaveOrderFromCart(Customer customer, Cart cart) {
        List<OrderDetail> details = cart.getItems().stream()
                .map(item -> new OrderDetail(null, item.getProduct(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList());

        return orderService.persistNewOrderForCustomer(customer.getId(), details);
    }

}
