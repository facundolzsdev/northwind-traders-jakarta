package com.northwind.repository;

import com.northwind.model.enums.OrderStatus;

import java.util.List;

/**
 * Repository interface for employee reporting features.
 * <p>
 * Provides access to inventory and order processing statistics
 * to support operational decision-making by employees.
 * </p>
 */
public interface iEmployeeReportRepository {

    /**
     * Retrieves a summary of products that are currently low on stock.
     * <p>
     * Each result contains the product name, category name, and current stock level.
     * </p>
     *
     * @return a list of Object arrays containing product data with low stock levels
     */
    List<Object[]> findLowStockProductSummaries();

    /**
     * Retrieves a summary of products that are completely out of stock.
     * <p>
     * Each result contains the product name and category name.
     * </p>
     *
     * @return a list of Object arrays containing product data with zero stock
     */
    List<Object[]> findOutOfStockProductSummaries();

    /**
     * Counts the number of current orders with the specified status.
     *
     * @param status the order status to filter by (e.g., COMPLETADO, RECHAZADO)
     * @return the number of current orders matching the given status
     */
    long countOrdersByStatus(OrderStatus status);

    /**
     * Counts the number of historical orders with the specified status.
     * <p>
     * Historical orders refer to archived records moved from the main order table.
     * </p>
     *
     * @param status the order status to filter by (e.g., COMPLETADO, RECHAZADO)
     * @return the number of historical orders matching the given status
     */
    long countHistoricalOrdersByStatus(OrderStatus status);
}
