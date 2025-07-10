package com.northwind.service;

import com.northwind.model.enums.OrderStatus;

import java.util.List;

/**
 * Service interface for generating employee-level reports.
 */
public interface iEmployeeReportService {

    List<Object[]> getLowStockProductSummaries();

    List<Object[]> getOutOfStockProductSummaries();

    long countOrdersByStatus(OrderStatus status);

    long countHistoricalOrdersByStatus(OrderStatus status);
}
