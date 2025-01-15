package com.soigo.romashkako.controller;

import com.soigo.romashkako.dto.request.ProductCreateRequest;
import com.soigo.romashkako.dto.request.ProductUpdateRequest;
import com.soigo.romashkako.dto.response.ProductResponse;
import com.soigo.romashkako.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(
                productService.findAll()
                        .stream()
                        .map(product -> modelMapper.map(product, ProductResponse.class))
                        .toList()
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
