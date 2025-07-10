package com.northwind.repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Admin reporting features.
 * Provides statistical data for administrative dashboards,
 * such as top-selling products, total sales, and top customers.
 */
public interface iAdminReportRepository {

    /**
     * Returns the top-selling products by total quantity sold,
     * combining current and historical order details.
     *
     * @param maxResults the maximum number of results to return
     * @return a list of product names and total quantities sold
     */
    List<Object[]> findTopSellingProducts(int maxResults);

    /**
     * Calculates the total sales amount between two dates, including both
     * current and historical orders. Only orders with status 'COMPLETADO'
     * are included in the sum.
     *
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return the total sales as a Double
     */
    Double getTotalSalesBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieves the top customers by total number of orders placed,
     * combining current and historical orders.
     * Even if an order has been archived, it still contributes to
     * the customer's total activity.
     *
     * @param maxResults the maximum number of results to return
     * @return a list of customer full names and their total order count
     */
    List<Object[]> findTopCustomersByOrders(int maxResults);

}
