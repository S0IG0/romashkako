package com.soigo.romashkako.service.impl;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.exception.ValueLessThanZeroException;
import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.repository.ProductRepository;
import com.soigo.romashkako.repository.specification.ProductSpecification;
import com.soigo.romashkako.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static com.soigo.romashkako.utils.SortUtil.createSort;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final Availability defaultAvailability = Availability.OUT_OF_STOCK;
    private final BigDecimal defaultPrice = BigDecimal.ZERO;

    @Override
    public Page<Product> findAll(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String availability,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    ) {
        Sort sort = createSort(sortBy, reverse);
        Availability foundAvailability = availability != null ? Availability.valueOf(availability) : null;

        Specification<Product> spec = ProductSpecification.hasNameAndPriceAndAvailability(
                name,
                minPrice,
                maxPrice,
                foundAvailability
        );

        return productRepository.findAllByDeletedFalse(
                spec,
                PageRequest.of(
                        page,
                        size,
                        sort
                )
        );
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Продукт с id %s не найден", id))
        );
    }

    @Override
    public Product create(ProductCreateRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        setDefaultValues(product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, ProductUpdateRequest productRequest) {
        checkProductExists(id);
        Product product = modelMapper.map(productRequest, Product.class);
        Product productFound = productRepository.findById(id).orElseThrow();
        updateProductFields(productFound, product);
        return productRepository.save(productFound);
    }

    @Override
    public void delete(Long id) {
        checkProductExists(id);
        Product product = productRepository.findById(id).orElseThrow();
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void adjustCount(Long id, Long count) {
        checkProductExists(id);
        Product product = productRepository.findById(id).orElseThrow();
        Long oldCount = product.getCount();
        long result = oldCount + count;

        if (result < 0L) {
            log.error("Ошибка при изменение count в product, старое значение: {}, новое значение: {}", oldCount, result);
            throw new ValueLessThanZeroException(
                    "Значение кол-во продукта не может быть меньше 0",
                    Map.of(
                            "product.id", String.valueOf(id),
                            "product.count", String.format("не может быть %d, значение должно быть больше 0", result)
                    )
            );
        }

        product.setCount(result);
        updateAvailability(product);
        productRepository.save(product);
    }

    private void updateAvailability(Product product) {
        long count = product.getCount();
        if (count > 0L) {
            product.setAvailability(Availability.IN_STOCK);
        } else {
            product.setAvailability(Availability.OUT_OF_STOCK);
        }
    }


    private void checkProductExists(Long id) {
        if (!productRepository.existsByIdAndDeletedIsFalse(id)) {
            throw new EntityNotFoundException(String.format("Продукт с id %s не найден или помечен как удален", id));
        }
    }

    private void setDefaultValues(Product product) {
        if (product.getAvailability() == null) {
            product.setAvailability(defaultAvailability);
        }
        if (product.getPrice() == null) {
            product.setPrice(defaultPrice);
        }
        if (product.getCount() == null) {
            long defaultCount = 0L;
            product.setCount(defaultCount);
        }
    }

    private void updateProductFields(Product productFound, Product product) {
        if (product.getName() != null) {
            productFound.setName(product.getName());
        }
        if (product.getDescription() != null) {
            productFound.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            productFound.setPrice(product.getPrice());
        }
        if (product.getAvailability() != null) {
            productFound.setAvailability(product.getAvailability());
        }
    }
}
