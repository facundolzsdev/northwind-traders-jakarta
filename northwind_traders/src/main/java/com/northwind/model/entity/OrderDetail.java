package com.northwind.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderDetailID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Column(name = "ProductQuantity")
    private short productQuantity;

    @Column(name = "ProductPrice", precision = 10, scale = 2)
    private BigDecimal productPrice;

    public OrderDetail(Order order, Product product, short productQuantity, BigDecimal productPrice) {
        this.order = order;
        this.product = product;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }
}
