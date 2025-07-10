package com.northwind.repository;

import com.northwind.model.entity.Cart;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class CartRepoImpl implements iCartRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Cart> findAll() {
        return em.createQuery("FROM Cart c", Cart.class).getResultList();
    }

    @Override
    public Cart findById(Integer id) {
        return em.find(Cart.class, id);
    }

    @Override
    public void save(Cart cart) {
        if (cart.getId() != null) {
            em.merge(cart);
        } else {
            em.persist(cart);
        }
    }

    @Override
    public void delete(Integer id) {
        Cart cart = findById(id);
        em.remove(cart);
    }

    @Override
    public int getCartItemCount(Integer cartId) {
        return em.createQuery("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id=:cartId", Long.class)
                .setParameter("cartId", cartId)
                .getSingleResult().intValue();
    }

    @Override
    public Cart findByCustomerIdWithItems(Integer customerId) {
        try {
            String jpql = "SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.customer.id = :customerId";
            return em.createQuery(jpql, Cart.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
