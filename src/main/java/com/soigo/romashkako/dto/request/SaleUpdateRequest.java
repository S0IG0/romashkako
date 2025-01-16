package com.soigo.romashkako.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleUpdateRequest {
    @Size(max = 255)
    private String name;
}
