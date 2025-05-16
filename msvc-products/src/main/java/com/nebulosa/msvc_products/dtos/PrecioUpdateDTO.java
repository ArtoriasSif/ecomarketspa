package com.nebulosa.msvc_products.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrecioUpdateDTO {
    @NotNull(message = "El precio no puede ser nulo")
    private Double precio;
}
