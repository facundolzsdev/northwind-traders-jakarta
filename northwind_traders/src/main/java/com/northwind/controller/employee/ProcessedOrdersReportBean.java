package com.northwind.controller.employee;

import com.northwind.model.enums.OrderStatus;
import com.northwind.service.iEmployeeReportService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

import lombok.*;
import org.primefaces.model.chart.PieChartModel;

/**
 * Managed bean for generating a summary chart of processed orders.
 * <p>
 * Aggregates approved and rejected order counts from both current and historical records,
 * and prepares a pie chart model to visualize the data for employee reporting.
 * </p>
 */
@Getter
@Setter
@Named("processedOrdersBean")
@ViewScoped
public class ProcessedOrdersReportBean implements Serializable {

    private PieChartModel pieModel;
    private long approvedFromOrders;
    private long rejectedFromOrders;
    private long approvedFromHistory;
    private long rejectedFromHistory;

    @Inject
    private iEmployeeReportService reportService;

    @PostConstruct
    public void init() {
        loadOrderCounts();
        initPieModel();
    }

    private void loadOrderCounts() {
        approvedFromOrders = reportService.countOrdersByStatus(OrderStatus.COMPLETADO);
        rejectedFromOrders = reportService.countOrdersByStatus(OrderStatus.RECHAZADO);
        approvedFromHistory = reportService.countHistoricalOrdersByStatus(OrderStatus.COMPLETADO);
        rejectedFromHistory = reportService.countHistoricalOrdersByStatus(OrderStatus.RECHAZADO);
    }

    private void initPieModel() {
        pieModel = new PieChartModel();
        pieModel.set("Aprobadas (" + getTotalApprovedOrders() + ")", getTotalApprovedOrders());
        pieModel.set("Rechazadas (" + getTotalRejectedOrders() + ")", getTotalRejectedOrders());
        pieModel.setLegendPosition("w");
        pieModel.setShowDataLabels(true);
        pieModel.setSeriesColors("2ecc71, e74c3c");
    }

    /**
     * Calculates the total number of approved orders across both current and historical sources.
     *
     * @return the total number of approved orders
     */
    private long getTotalApprovedOrders() {
        return approvedFromOrders + approvedFromHistory;
    }

    /**
     * Calculates the total number of rejected orders across both current and historical sources.
     *
     * @return the total number of rejected orders
     */
    private long getTotalRejectedOrders() {
        return rejectedFromOrders + rejectedFromHistory;
    }
}
