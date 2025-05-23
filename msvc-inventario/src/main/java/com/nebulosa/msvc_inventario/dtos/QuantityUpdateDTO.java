package com.nebulosa.msvc_inventario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityUpdateDTO {
    private Long productoId;
    private Long sucursalId;
    private Long cantidad;
}
