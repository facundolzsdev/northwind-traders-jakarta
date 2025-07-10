package com.northwind.service;

import com.northwind.model.entity.Cart;
import com.northwind.model.entity.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling cart-related business logic.
 */
public interface iCartService {

    Optional<Cart> getCartById(Integer id);

    List<Cart> getAllCarts();

    void saveCart(Cart cart);

    void deleteCart(Integer id);

    int getCartItemCount(Integer cartId);

    Cart getByCustomerIdWithItems(Integer customerId);

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    void addProductToCart(Integer cartId, Integer productId, short quantity);

    void removeProductFromCart(Integer cartId, Integer productId);

    /**
     * Retrieves the customer's cart or creates one if it doesn't exist.
     *
     * @param customer the customer to retrieve/create the cart for
     * @return the existing or newly created cart
     */
    Cart getOrCreateCartForCustomer(Customer customer);

    void clearCart(Integer id);

    void deleteCartByCustomerId(Integer customerId);

}
