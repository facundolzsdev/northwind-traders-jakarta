package com.northwind.service;

import com.northwind.model.enums.OrderStatus;
import com.northwind.repository.iEmployeeReportRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class EmployeeReportServiceImpl implements iEmployeeReportService {

    @Inject
    private iEmployeeReportRepository reportRepo;

    @Override
    public List<Object[]> getLowStockProductSummaries() {
        return reportRepo.findLowStockProductSummaries();
    }

    @Override
    public List<Object[]> getOutOfStockProductSummaries() {
        return reportRepo.findOutOfStockProductSummaries();
    }

    @Override
    public long countOrdersByStatus(OrderStatus status) {
        return reportRepo.countOrdersByStatus(status);
    }

    @Override
    public long countHistoricalOrdersByStatus(OrderStatus status) {
        return reportRepo.countHistoricalOrdersByStatus(status);
    }
}
