package com.soigo.romashkako.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String availability;
    private Long count;
    private Boolean deleted;
}
