package com.soigo.romashkako.controller;

import com.soigo.romashkako.dto.request.DeliveryCreateRequest;
import com.soigo.romashkako.dto.request.DeliveryUpdateRequest;
import com.soigo.romashkako.dto.response.DeliveryResponse;
import com.soigo.romashkako.model.Delivery;
import com.soigo.romashkako.service.DeliveryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<Page<DeliveryResponse>> getDeliveries(
            @RequestParam(required = false) @Size(min = 1, max = 255) String name,
            @RequestParam(required = false) @Min(1) Long productId,
            @RequestParam(required = false) @Pattern(regexp = "^(createdAt|updatedAt)$", message = "Значение должно быть 'createdAt' или 'updatedAt'") String sortBy,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "false") @Pattern(regexp = "^(true|false)$", message = "Значение должно быть 'true' или 'false'") String reverse
    ) {

        Page<Delivery> deliveries = deliveryService.findAll(
                name,
                productId,
                page,
                size,
                sortBy,
                Boolean.valueOf(reverse)
        );

        return ResponseEntity.ok(
                deliveries.map(delivery -> modelMapper.map(delivery, DeliveryResponse.class))
        );
    }


    @PatchMapping("{id}")
    public ResponseEntity<DeliveryResponse> updateProduct(@PathVariable Long id, @Validated @RequestBody DeliveryUpdateRequest product) {
        return ResponseEntity.ok(
                modelMapper.map(deliveryService.update(id, product), DeliveryResponse.class)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<DeliveryResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                modelMapper.map(deliveryService.findById(id), DeliveryResponse.class)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> createProduct(@Validated @RequestBody DeliveryCreateRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                modelMapper.map(deliveryService.create(product), DeliveryResponse.class)
        );
    }
}
