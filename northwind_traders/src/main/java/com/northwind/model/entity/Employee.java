package com.northwind.model.entity;

import com.northwind.model.support.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Integer id;

    @Column(name = "EmployeeName")
    private String fullName;

    @Column(name = "EmployeeDNI")
    private String dni;

    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Active")
    private boolean active;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Embedded
    private Audit audit;

    @OneToMany(mappedBy = "employee")
    private List<Order> orders;

    public Employee() {
        this.active = true;
        this.orders = new ArrayList<>();
    }

}
