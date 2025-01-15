package com.soigo.romashkako.repository.impl;

import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.repository.ProductLocalRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductLocalRepositoryImpl implements ProductLocalRepository {

    private static final Map<Long, Product> products = new HashMap<>();
    private static Long nextId = 1L;

    @Override
    public List<Product> findAll() {
        return products
                .values()
                .stream()
                .toList();
    }

    @Override
    public Product findById(Long id) {
        return products.get(id);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(nextId++);
        }

        products.put(product.getId(), product);

        return product;
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return products.containsKey(id);
    }
}
