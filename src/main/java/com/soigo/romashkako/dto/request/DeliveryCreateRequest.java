package com.soigo.romashkako.dto.request;

import com.soigo.romashkako.dto.shared.ObjectWithId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeliveryCreateRequest {
    @Size(max = 255)
    @NotBlank
    private String name;
    @Min(1)
    @NotNull
    private Long count;
    @NotNull
    @Valid
    private ObjectWithId product;
}
