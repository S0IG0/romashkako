package com.soigo.romashkako.controller;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.dto.response.ProductResponse;
import com.soigo.romashkako.model.Availability;
import com.soigo.romashkako.model.Product;
import com.soigo.romashkako.service.ProductService;
import com.soigo.romashkako.validation.annotation.EnumValue;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) @Size(min = 1, max = 255) String name,
            @RequestParam(required = false) @DecimalMin("0.00") BigDecimal minPrice,
            @RequestParam(required = false) @DecimalMin("0.00") BigDecimal maxPrice,
            @RequestParam(required = false) @EnumValue(enumClass = Availability.class) String availability,
            @RequestParam(required = false) @Pattern(regexp = "^(name|price)$", message = "Значение должно быть 'name' или 'price'") String sortBy,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "false") @Pattern(regexp = "^(true|false)$", message = "Значение должно быть 'true' или 'false'") String reverse
    ) {

        Page<Product> products = productService.findAll(
                name,
                minPrice,
                maxPrice,
                availability,
                page,
                size,
                sortBy,
                Boolean.valueOf(reverse)
        );

        return ResponseEntity.ok(
                products.map(productResponse -> modelMapper.map(productResponse, ProductResponse.class))
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                modelMapper.map(productService.findById(id), ProductResponse.class)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Validated @RequestBody ProductCreateRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                modelMapper.map(productService.create(product), ProductResponse.class)
        );
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Validated @RequestBody ProductUpdateRequest product) {
        return ResponseEntity.ok(
                modelMapper.map(productService.update(id, product), ProductResponse.class)
        );
    }
}
