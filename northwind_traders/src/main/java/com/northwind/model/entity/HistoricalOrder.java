package com.northwind.model.entity;

import com.northwind.model.enums.OrderArchivedReason;
import com.northwind.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "historical_orders")
public class HistoricalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoricalOrderID")
    private Integer historicalOrderID;

    @Column(name = "OrderID")
    private Integer orderID;

    @Column(name = "EmployeeID")
    private Integer employeeID;

    @Column(name = "CustomerFullName")
    private String customerFullName;

    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @Column(name = "OrderStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "TotalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "ArchivedDate")
    private LocalDateTime archivedDate;

    @Column(name = "ArchivedReason", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderArchivedReason archivedReason;

    @OneToMany(mappedBy = "historicalOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricalOrderDetails> historicalOrderDetails;

    public HistoricalOrder() {
        this.historicalOrderDetails = new ArrayList<>();
    }

}
