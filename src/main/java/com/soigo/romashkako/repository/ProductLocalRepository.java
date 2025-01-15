package com.soigo.romashkako.repository;

import com.soigo.romashkako.model.Product;

import java.util.List;


public interface ProductLocalRepository {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
    boolean existsById(Long id);
}
