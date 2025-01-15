package com.soigo.romashkako.service.impl;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.repository.ProductLocalRepository;
import com.soigo.romashkako.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductLocalRepository productRepository;
    private final ModelMapper modelMapper;
    private final Availability defaultAvailability = Availability.OUT_OF_STOCK;
    private final BigDecimal defaultPrice = BigDecimal.ZERO;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        checkProductExists(id);
        return productRepository.findById(id);
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
        Product productFound = productRepository.findById(id);
        updateProductFields(productFound, product);
        return productRepository.save(productFound);
    }

    @Override
    public void delete(Long id) {
        checkProductExists(id);
        productRepository.deleteById(id);
    }

    private void checkProductExists(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Продукт с id %s не найден", id));
        }
    }

    private void setDefaultValues(Product product) {
        if (product.getAvailability() == null) {
            product.setAvailability(defaultAvailability);
        }
        if (product.getPrice() == null) {
            product.setPrice(defaultPrice);
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
