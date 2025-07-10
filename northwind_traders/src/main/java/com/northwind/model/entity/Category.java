package com.northwind.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Byte id;

    @Column(name = "CategoryName")
    private String name;

    @Column(name = "Description")
    private String description;

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }
}
