package com.soigo.romashkako.dto.shared;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ObjectWithId {
    @NotNull
    @Min(1)
    private Long id;
}
