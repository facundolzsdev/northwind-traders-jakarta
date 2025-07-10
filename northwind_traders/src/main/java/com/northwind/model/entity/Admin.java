package com.northwind.model.entity;

import com.northwind.model.support.Audit;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdminID")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Embedded
    private Audit audit;

}
