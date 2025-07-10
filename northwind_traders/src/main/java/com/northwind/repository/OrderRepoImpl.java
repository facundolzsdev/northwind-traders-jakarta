package com.northwind.repository;

import com.northwind.model.entity.Order;
import com.northwind.model.enums.OrderStatus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

@RequestScoped
public class OrderRepoImpl implements iOrderRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Order> findAll() {
        return em.createQuery("FROM Order o", Order.class).getResultList();
    }

    @Override
    public Order findById(Integer id) {
        return em.find(Order.class, id);
    }

    @Override
    public void save(Order order) {
        if (order.getId() != null) {
            em.merge(order);
        } else {
            em.persist(order);
        }
    }

    @Override
    public void delete(Integer id) {
        Order order = findById(id);
        em.remove(order);
    }

    @Override
    public List<Order> findByCustomerId(Integer customerId) {
        TypedQuery<Order> query = em.createQuery(
                "SELECT DISTINCT o FROM Order o " +
                        "LEFT JOIN FETCH o.orderDetails " +
                        "WHERE o.customer.id = :customerId",
                Order.class
        );
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public Order findByCustomerIdAndOrderId(Integer customerId, Integer orderId) {
        try {
            return em.createQuery(
                            "SELECT o FROM Order o " +
                                    "JOIN FETCH o.customer c " +
                                    "JOIN FETCH o.orderDetails od " +
                                    "JOIN FETCH od.product " +
                                    "WHERE o.id = :orderId AND c.id = :customerId",
                            Order.class
                    )
                    .setParameter("orderId", orderId)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Order findByIdWithDetails(Integer id) {
        try {
            return em.createQuery(
                            "SELECT o FROM Order o " +
                                    "JOIN FETCH o.orderDetails od " +
                                    "JOIN FETCH od.product " +
                                    "WHERE o.id = :id", Order.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Order> findProcessedOrdersByCustomerId(Integer customerId) {
        return em.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "JOIN FETCH o.customer c " +
                                "JOIN FETCH o.orderDetails od " +
                                "JOIN FETCH od.product " +
                                "WHERE c.id = :customerId AND o.status <> :status",
                        Order.class
                )
                .setParameter("customerId", customerId)
                .setParameter("status", OrderStatus.PENDIENTE)
                .getResultList();
    }

    @Override
    public List<Order> findAllPendingWithDetails() {
        return em.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "JOIN FETCH o.customer c " +
                                "JOIN FETCH o.orderDetails od " +
                                "JOIN FETCH od.product p " +
                                "WHERE o.status = :status", Order.class)
                .setParameter("status", OrderStatus.PENDIENTE)
                .getResultList();
    }

}
