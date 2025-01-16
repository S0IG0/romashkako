package com.soigo.romashkako.service;

import com.soigo.romashkako.dto.request.DeliveryCreateRequest;
import com.soigo.romashkako.dto.request.DeliveryUpdateRequest;
import com.soigo.romashkako.model.Delivery;
import org.springframework.data.domain.Page;

public interface DeliveryService {
    Page<Delivery> findAll(
            String name,
            Long productId,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    );
    Delivery findById(Long id);
    void delete(Long id);
    Delivery update(Long id, DeliveryUpdateRequest deliveryRequest);
    Delivery create(DeliveryCreateRequest deliveryCreateRequest);
}
