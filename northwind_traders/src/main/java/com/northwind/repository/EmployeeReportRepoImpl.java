package com.northwind.repository;

import com.northwind.model.enums.OrderStatus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@RequestScoped
public class EmployeeReportRepoImpl implements iEmployeeReportRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Object[]> findLowStockProductSummaries() {
        return em.createQuery("""
                SELECT p.name, c.name, p.stock
                FROM Product p
                JOIN p.category c
                WHERE p.stock > 0 AND p.stock < 10
                """, Object[].class).getResultList();
    }

    @Override
    public List<Object[]> findOutOfStockProductSummaries() {
        return em.createQuery("""
                SELECT p.name, c.name
                FROM Product p
                JOIN p.category c
                WHERE p.stock = 0 OR p.active = false
                """, Object[].class).getResultList();
    }

    @Override
    public long countOrdersByStatus(OrderStatus status) {
        return em.createQuery("""
                SELECT COUNT(o) FROM Order o WHERE o.status = :status
                """, Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    @Override
    public long countHistoricalOrdersByStatus(OrderStatus status) {
        return em.createQuery("""
                SELECT COUNT(o) FROM HistoricalOrder o WHERE o.status = :status
                """, Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }
}
