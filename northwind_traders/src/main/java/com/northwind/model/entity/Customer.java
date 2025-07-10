package com.northwind.model.entity;

import com.northwind.model.support.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer id;

    @Column(name = "CustomerName")
    private String fullName;

    @Column(name = "Country")
    private String country;

    @Column(name = "City")
    private String city;

    @Column(name = "PostalCode")
    private String postalCode;

    @Column(name = "Address")
    private String address;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartID", nullable = false)
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Embedded
    private Audit audit;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    public Customer() {
        this.orders = new ArrayList<>();
    }

}
