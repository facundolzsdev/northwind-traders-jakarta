package com.northwind.controller.employee;

import com.northwind.service.iEmployeeReportService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Managed bean for administrative stock level reports.
 * <p>
 * Provides categorized lists of products fields with low stock and those completely out of stock,
 * to assist in inventory monitoring and restocking decisions.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public class StockReportBean implements Serializable {

    private List<Object[]> lowStockProducts;
    private List<Object[]> outOfStockProducts;

    @Inject
    private iEmployeeReportService reportService;

    @PostConstruct
    public void init() {
        lowStockProducts = reportService.getLowStockProductSummaries();
        outOfStockProducts = reportService.getOutOfStockProductSummaries();
    }

}