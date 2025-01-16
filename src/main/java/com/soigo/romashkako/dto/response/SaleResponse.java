package com.soigo.romashkako.dto.response;

import com.soigo.romashkako.dto.shared.ObjectWithId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SaleResponse {
    private Long id;
    private String name;
    private Long count;
    private ObjectWithId product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
