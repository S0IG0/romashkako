package com.soigo.romashkako.controller;

import com.soigo.romashkako.dto.request.SaleCreateRequest;
import com.soigo.romashkako.dto.request.SaleUpdateRequest;
import com.soigo.romashkako.dto.response.SaleResponse;
import com.soigo.romashkako.model.Sale;
import com.soigo.romashkako.service.SaleService;
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
@RequestMapping("sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getSales(
            @RequestParam(required = false) @Size(min = 1, max = 255) String name,
            @RequestParam(required = false) @Min(1) Long productId,
            @RequestParam(required = false) @DecimalMin("0.00") BigDecimal minCost,
            @RequestParam(required = false) @DecimalMin("0.00") BigDecimal maxCost,
            @RequestParam(required = false) @Pattern(regexp = "^(createdAt|updatedAt)$", message = "Значение должно быть 'createdAt' или 'updatedAt'") String sortBy,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "false") @Pattern(regexp = "^(true|false)$", message = "Значение должно быть 'true' или 'false'") String reverse
    ) {

        Page<Sale> sales = saleService.findAll(
                name,
                productId,
                minCost,
                maxCost,
                page,
                size,
                sortBy,
                Boolean.valueOf(reverse)
        );

        return ResponseEntity.ok(
                sales.map(sale -> modelMapper.map(sale, SaleResponse.class))
        );
    }


    @PatchMapping("{id}")
    public ResponseEntity<SaleResponse> updateSale(@PathVariable Long id, @Validated @RequestBody SaleUpdateRequest product) {
        return ResponseEntity.ok(
                modelMapper.map(saleService.update(id, product), SaleResponse.class)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<SaleResponse> getSale(@PathVariable Long id) {
        return ResponseEntity.ok(
                modelMapper.map(saleService.findById(id), SaleResponse.class)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Validated @RequestBody SaleCreateRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                modelMapper.map(saleService.create(product), SaleResponse.class)
        );
    }
}
