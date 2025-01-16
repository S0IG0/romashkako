package com.soigo.romashkako;

import com.soigo.romashkako.controller.ProductController;
import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.dto.response.ProductResponse;
import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;
    private ProductResponse productResponse1;
    private ProductResponse productResponse2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setAvailability(Availability.IN_STOCK);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setAvailability(Availability.OUT_OF_STOCK);

        productResponse1 = new ProductResponse();
        productResponse1.setId(1L);
        productResponse1.setName("Product 1");
        productResponse1.setDescription("Description 1");
        productResponse1.setPrice(BigDecimal.valueOf(100));
        productResponse1.setAvailability("IN_STOCK");

        productResponse2 = new ProductResponse();
        productResponse2.setId(2L);
        productResponse2.setName("Product 2");
        productResponse2.setDescription("Description 2");
        productResponse2.setPrice(BigDecimal.valueOf(200));
        productResponse2.setAvailability("OUT_OF_STOCK");
    }

    @Test
    void testGetProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);

        when(productService.findAll(null, null, null, null, 0, 10, null, false)).thenReturn(productPage);
        when(modelMapper.map(product1, ProductResponse.class)).thenReturn(productResponse1);
        when(modelMapper.map(product2, ProductResponse.class)).thenReturn(productResponse2);

        ResponseEntity<Page<ProductResponse>> response = productController.getProducts(null, null, null, null, null, 0, 10, "false");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).getTotalElements());
        verify(productService, times(1)).findAll(null, null, null, null, 0, 10, null, false);
    }

    @Test
    void testGetProduct() {
        when(productService.findById(1L)).thenReturn(product1);
        when(modelMapper.map(product1, ProductResponse.class)).thenReturn(productResponse1);

        ResponseEntity<ProductResponse> response = productController.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(response.getBody()).getId());
        verify(productService, times(1)).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).delete(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).delete(1L);
    }

    @Test
    void testCreateProduct() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("New Product");
        request.setDescription("New Description");
        request.setPrice(BigDecimal.valueOf(150));
        request.setAvailability("IN_STOCK");

        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setPrice(BigDecimal.valueOf(150));
        newProduct.setAvailability(Availability.IN_STOCK);

        ProductResponse newProductResponse = new ProductResponse();
        newProductResponse.setId(1L);
        newProductResponse.setName("New Product");
        newProductResponse.setDescription("New Description");
        newProductResponse.setPrice(BigDecimal.valueOf(150));
        newProductResponse.setAvailability("IN_STOCK");

        when(productService.create(request)).thenReturn(newProduct);
        when(modelMapper.map(newProduct, ProductResponse.class)).thenReturn(newProductResponse);

        ResponseEntity<ProductResponse> response = productController.createProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Product", Objects.requireNonNull(response.getBody()).getName());
        verify(productService, times(1)).create(request);
    }

    @Test
    void testUpdateProduct() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Updated Product");
        request.setDescription("Updated Description");
        request.setPrice(BigDecimal.valueOf(250));
        request.setAvailability("OUT_OF_STOCK");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.valueOf(250));
        updatedProduct.setAvailability(Availability.OUT_OF_STOCK);

        ProductResponse updatedProductResponse = new ProductResponse();
        updatedProductResponse.setId(1L);
        updatedProductResponse.setName("Updated Product");
        updatedProductResponse.setDescription("Updated Description");
        updatedProductResponse.setPrice(BigDecimal.valueOf(250));
        updatedProductResponse.setAvailability("OUT_OF_STOCK");

        when(productService.update(1L, request)).thenReturn(updatedProduct);
        when(modelMapper.map(updatedProduct, ProductResponse.class)).thenReturn(updatedProductResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Product", Objects.requireNonNull(response.getBody()).getName());
        verify(productService, times(1)).update(1L, request);
    }
}
