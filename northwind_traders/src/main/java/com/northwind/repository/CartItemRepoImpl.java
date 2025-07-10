package com.northwind.repository;

import com.northwind.model.entity.CartItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class CartItemRepoImpl implements iCartItemRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<CartItem> findByCartId(Integer cartId) {
        return em.createQuery("SELECT ci FROM CartItem ci JOIN FETCH ci.cart c WHERE c.id = :cartId", CartItem.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    @Override
    public CartItem findByCartIdAndProductId(Integer cartId, Integer productId) {
        try {
            return em.createQuery("SELECT ci FROM CartItem ci " +
                            "JOIN FETCH ci.cart c " +
                            "JOIN FETCH ci.product p " +
                            "WHERE c.id = :cartId AND p.id = :productId", CartItem.class)
                    .setParameter("cartId", cartId)
                    .setParameter("productId", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<CartItem> findAll() {
        return em.createQuery("SELECT ci FROM CartItem ci " +
                        "JOIN FETCH ci.cart c " +
                        "JOIN FETCH ci.product p", CartItem.class)
                .getResultList();
    }

    @Override
    public CartItem findById(Integer id) {
        return em.find(CartItem.class, id);
    }

    @Override
    public void save(CartItem cartItem) {
        if (cartItem.getId() != null) {
            em.merge(cartItem);
        } else {
            em.persist(cartItem);
        }
    }

    @Override
    public void delete(Integer id) {
        CartItem cartItem = findById(id);
        em.remove(cartItem);
    }

    @Override
    public List<CartItem> findByCustomerIdWithProducts(Integer customerId) {
        try {
            return em.createQuery("SELECT ci FROM CartItem ci " +
                            "JOIN FETCH ci.cart c " +
                            "JOIN FETCH ci.product p " +
                            "JOIN FETCH p.category " +
                            "JOIN FETCH c.customer cu " +
                            "WHERE cu.id = :customerId", CartItem.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
