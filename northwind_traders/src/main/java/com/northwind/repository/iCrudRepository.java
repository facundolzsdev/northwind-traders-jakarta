package com.northwind.repository;

import java.util.List;

public interface iCrudRepository<T, ID> {

    List<T> findAll();

    T findById(ID id);

    void save(T t);

    void delete(ID id);
}
