package com.soigo.romashkako.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductUpdateRequest {
    @Size(max = 255)
    private String name;
    @Size(max = 4096)
    private String description;
    @DecimalMin("0.00")
    private BigDecimal price;
}
