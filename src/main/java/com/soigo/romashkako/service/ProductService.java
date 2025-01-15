package com.soigo.romashkako.service;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product create(ProductCreateRequest productRequest);
    Product update(Long id, ProductUpdateRequest productRequest);
    void delete(Long id);
}
