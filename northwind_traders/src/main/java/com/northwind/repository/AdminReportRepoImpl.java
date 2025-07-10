package com.northwind.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Note: Due to the system architecture where data is split between current
 * and historical tables (e.g., `orders` and `historical_orders`),
 * we use `UNION ALL` to combine results instead of `JOIN`s, which is not
 * applicable across these separate sources.
 */
@RequestScoped
public class AdminReportRepoImpl implements iAdminReportRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Object[]> findTopSellingProducts(int maxResults) {
        String nativeQuery = topSellingProductsQuery();
        return em.createNativeQuery(nativeQuery)
                .setMaxResults(maxResults)
                .getResultList();
    }

    @Override
    public Double getTotalSalesBetween(LocalDate start, LocalDate end) {
        String nativeQuery = totalSalesBetweenQuery();

        Object result = em.createNativeQuery(nativeQuery)
                .setParameter("start", start)
                .setParameter("end", end.plusDays(1))
                .getSingleResult();

        if (result == null) {
            return 0.0;
        }

        return ((Number) result).doubleValue();
    }

    @Override
    public List<Object[]> findTopCustomersByOrders(int maxResults) {
        String nativeQuery = topCustomersByOrdersQuery();
        return em.createNativeQuery(nativeQuery)
                .setMaxResults(maxResults)
                .getResultList();
    }

    private String topCustomersByOrdersQuery() {
        return """
                SELECT fullName, COUNT(*) AS total_orders
                FROM (
                    SELECT c.CustomerName AS fullName
                    FROM orders o
                    JOIN customers c ON o.CustomerID = c.CustomerID

                    UNION ALL

                    SELECT ho.CustomerFullName AS fullName
                    FROM historical_orders ho
                ) AS combined
                GROUP BY fullName
                ORDER BY total_orders DESC
                """;
    }

    private String topSellingProductsQuery() {
        return """
                SELECT combined.name, SUM(combined.quantity) AS total_quantity
                FROM (
                    SELECT p.ProductName AS name, od.ProductQuantity AS quantity
                    FROM order_details od
                    JOIN products p ON od.ProductID = p.ProductID

                    UNION ALL

                    SELECT p.ProductName AS name, hod.ProductQuantity AS quantity
                    FROM historical_order_details hod
                    JOIN products p ON hod.ProductID = p.ProductID
                ) AS combined
                GROUP BY combined.name
                ORDER BY total_quantity DESC
                """;
    }

    private String totalSalesBetweenQuery() {
        return """
                SELECT SUM(total) FROM (
                    SELECT o.TotalAmount AS total
                    FROM orders o
                    WHERE o.OrderStatus = 'COMPLETADO'
                    AND o.OrderDate >= :start AND o.OrderDate < :end

                    UNION ALL

                    SELECT ho.TotalAmount AS total
                    FROM historical_orders ho
                    WHERE ho.OrderStatus = 'COMPLETADO'
                    AND ho.OrderDate >= :start AND ho.OrderDate < :end
                ) AS combined
                """;
    }
}

