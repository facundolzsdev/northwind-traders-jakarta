package com.northwind.model.entity;

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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID", nullable = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = true)
    private Employee employee;

    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @Column(name = "OrderStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "TotalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    public Order() {
        this.orderDate = LocalDateTime.now();
        this.orderDetails = new ArrayList<>();
    }

    public void calculateTotal() {
        this.totalAmount = orderDetails.stream()
                .map(detail -> detail.getProductPrice().multiply(BigDecimal.valueOf(detail.getProductQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
