package com.soigo.romashkako.service;

import com.soigo.romashkako.dto.request.SaleCreateRequest;
import com.soigo.romashkako.dto.request.SaleUpdateRequest;
import com.soigo.romashkako.model.Sale;
import org.springframework.data.domain.Page;

public interface SaleService {
    Page<Sale> findAll(
            String name,
            Long productId,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    );
    Sale findById(Long id);
    void delete(Long id);
    Sale update(Long id, SaleUpdateRequest saleUpdateRequest);
    Sale create(SaleCreateRequest saleCreateRequest);
}
