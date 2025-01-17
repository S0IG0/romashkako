package com.soigo.romashkako.service.impl;

import com.soigo.romashkako.dto.request.DeliveryCreateRequest;
import com.soigo.romashkako.dto.request.DeliveryUpdateRequest;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.exception.NotSupportChange;
import com.soigo.romashkako.model.Delivery;
import com.soigo.romashkako.repository.DeliveryRepository;
import com.soigo.romashkako.repository.specification.DeliverySpecification;
import com.soigo.romashkako.service.DeliveryService;
import com.soigo.romashkako.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.soigo.romashkako.utils.SortUtil.createSort;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final ProductService productService;
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Delivery> findAll(
            String name,
            Long productId,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    ) {
        Sort sort = createSort(sortBy, reverse);
        Specification<Delivery> spec = DeliverySpecification.hasNameAndProductId(name, productId);
        return deliveryRepository.findAll(
                spec,
                PageRequest.of(
                        page,
                        size,
                        sort
                )
        );
    }

    @Transactional
    @Override
    public Delivery create(DeliveryCreateRequest deliveryCreateRequest) {
        Delivery delivery = modelMapper.map(deliveryCreateRequest, Delivery.class);

        try {
            // Добавляем к продукту кол-во
            productService.adjustCount(
                    delivery.getProduct().getId(),
                    delivery.getCount()
            );
        } catch (EntityNotFoundException e) {
            throw new NotSupportChange(
                    "Создание поставки для удаленного или несуществующего товара запрещено",
                    Map.of(
                            "product", e.getMessage()
                    )
            );
        }

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery update(Long id, DeliveryUpdateRequest deliveryRequest) {
        checkDeliveryExists(id);
        Delivery deliveryNew = modelMapper.map(deliveryRequest, Delivery.class);
        Delivery deliveryOld = deliveryRepository.findById(id).orElseThrow();

        if (deliveryNew.getName() != null) {
            deliveryOld.setName(deliveryNew.getName());
        }

        return deliveryRepository.save(deliveryOld);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        checkDeliveryExists(id);
        Delivery deliveryFound = deliveryRepository.findById(id).orElseThrow();

        try {
            // Убавляем у продукта кол-во
            productService.adjustCount(
                    deliveryFound.getProduct().getId(),
                    -deliveryFound.getCount()
            );
        } catch (EntityNotFoundException e) {
            throw new NotSupportChange(
                    "Удаление поставки для удаленного или несуществующего товара запрещено",
                    Map.of(
                            "product", e.getMessage()
                    )
            );
        }

        deliveryRepository.deleteById(id);
    }

    @Override
    public Delivery findById(Long id) {
        checkDeliveryExists(id);
        return deliveryRepository.findById(id).orElseThrow();
    }


    private void checkDeliveryExists(Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Поставка товара с id %s не найден", id));
        }
    }
}
