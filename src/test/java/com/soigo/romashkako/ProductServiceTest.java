package com.soigo.romashkako;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.repository.ProductRepository;
import com.soigo.romashkako.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setAvailability(Availability.IN_STOCK);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setAvailability(Availability.OUT_OF_STOCK);
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product1));

        Product product = productService.findById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.existsById(3L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productService.findById(3L));
        verify(productRepository, times(1)).existsById(3L);
    }

    @Test
    void testCreateProduct() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("New Product");
        request.setPrice(BigDecimal.valueOf(150));
        request.setAvailability("IN_STOCK");

        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(BigDecimal.valueOf(150));
        newProduct.setAvailability(Availability.IN_STOCK);

        when(modelMapper.map(request, Product.class)).thenReturn(newProduct);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        Product product = productService.create(request);

        assertNotNull(product);
        assertEquals("New Product", product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void testUpdateProduct() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Updated Product");
        request.setPrice(BigDecimal.valueOf(250));
        request.setAvailability("OUT_OF_STOCK");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(BigDecimal.valueOf(250));
        updatedProduct.setAvailability(Availability.OUT_OF_STOCK);

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product1));
        when(modelMapper.map(request, Product.class)).thenReturn(updatedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product product = productService.update(1L, request);

        assertNotNull(product);
        assertEquals("Updated Product", product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(3L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productService.delete(3L));
        verify(productRepository, times(1)).existsById(3L);
    }
}
