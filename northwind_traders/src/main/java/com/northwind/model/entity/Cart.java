package com.northwind.model.entity;

import com.northwind.model.support.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Integer id;

    @Embedded
    private Audit audit;

    @OneToOne
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }
}
