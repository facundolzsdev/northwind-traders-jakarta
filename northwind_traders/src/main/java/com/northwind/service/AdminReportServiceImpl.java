package com.northwind.service;

import com.northwind.repository.iAdminReportRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class AdminReportServiceImpl implements iAdminReportService {

    @Inject
    private iAdminReportRepository reportRepo;

    @Override
    public List<Object[]> getTopSellingProducts(int limit) {
        return reportRepo.findTopSellingProducts(limit);
    }

    @Override
    public Double getTotalSalesInRange(LocalDate start, LocalDate end) {
        return reportRepo.getTotalSalesBetween(start, end);
    }

    @Override
    public List<Object[]> getTopCustomersByOrders(int limit) {
        return reportRepo.findTopCustomersByOrders(limit);
    }
}
