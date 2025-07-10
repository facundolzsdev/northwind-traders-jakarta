package com.northwind.controller.admin;

import com.northwind.service.iAdminReportService;
import com.northwind.util.jsf.GrowlUtil;

import static com.northwind.util.constants.messages.DateMessages.*;
import static com.northwind.util.constants.messages.GrowlTitles.*;

import com.northwind.util.validation.DateValidator;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Managed Bean for generating administrative reports.
 * <p>
 * Initializes with default date ranges and item limits, and includes validation
 * for date range inputs before generating corresponding metrics.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public class AdminReportBean implements Serializable {

    private List<Object[]> topSellingProducts;
    private List<Object[]> topCustomers;
    private Double totalSales;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer topCustomersLimit;
    private Integer topProductsLimit;

    @Inject
    private iAdminReportService adminReportService;
    @Inject
    private DateValidator dateValidator;

    @PostConstruct
    public void init() {
        setDefaultValues();
    }

    public void loadTopSellingProducts() {
        topSellingProducts = adminReportService.getTopSellingProducts(topProductsLimit);
    }

    public void loadTopCustomers() {
        topCustomers = adminReportService.getTopCustomersByOrders(topCustomersLimit);
    }

    public void searchTotalSalesInRange() {
        if (!isValidDateRange()) {
            return;
        }

        totalSales = adminReportService.getTotalSalesInRange(startDate, endDate);
    }

    private void setDefaultValues() {
        this.topCustomersLimit = 5;
        this.topProductsLimit = 5;
        this.totalSales = 0.0;

        this.startDate = LocalDate.now().minusMonths(1);
        this.endDate = LocalDate.now();
    }

    private boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            GrowlUtil.warning(WARN_TITLE, ERROR_MUST_SELECT_BOTH_DATES);
            return false;
        }

        if (dateValidator.areDatesEqual(startDate, endDate)) {
            GrowlUtil.warning(WARN_TITLE, ERROR_DATES_CANNOT_BE_EQUAL);
            return false;
        }

        if (dateValidator.isEndDateBeforeStartDate(startDate, endDate)) {
            GrowlUtil.warning(WARN_TITLE, ERROR_END_DATE_BEFORE_START_DATE);
            return false;
        }

        return true;
    }
}
