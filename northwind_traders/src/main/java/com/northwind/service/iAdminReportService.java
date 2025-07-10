package com.northwind.service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for generating administrative reports.
 */
public interface iAdminReportService {

    List<Object[]> getTopSellingProducts(int limit);

    Double getTotalSalesInRange(LocalDate start, LocalDate end);

    List<Object[]> getTopCustomersByOrders(int limit);

}
