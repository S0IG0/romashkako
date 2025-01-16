package com.soigo.romashkako.service;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.model.Product;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;


public interface ProductService {
    Page<Product> findAll(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String availability,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    );
    Product findById(Long id);
    Product create(ProductCreateRequest productRequest);
    Product update(Long id, ProductUpdateRequest productRequest);
    void delete(Long id);
    void adjustCount(Long id, Long count);
}
