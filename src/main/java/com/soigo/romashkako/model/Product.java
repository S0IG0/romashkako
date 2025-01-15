package com.soigo.romashkako.model;

import lombok.*;


import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Availability availability;
}
