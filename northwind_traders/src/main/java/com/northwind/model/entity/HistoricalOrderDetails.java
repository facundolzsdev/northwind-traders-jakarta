package com.northwind.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "historical_order_details")
public class HistoricalOrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoricalOrderDetailID")
    private Integer historicalOrderDetailID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HistoricalOrderID", nullable = false)
    private HistoricalOrder historicalOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Column(name = "ProductQuantity")
    private short productQuantity;

    @Column(name = "ProductPrice", precision = 10, scale = 2)
    private BigDecimal productPrice;

}
