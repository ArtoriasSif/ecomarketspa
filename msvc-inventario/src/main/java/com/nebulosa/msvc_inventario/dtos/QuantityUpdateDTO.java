package com.nebulosa.msvc_inventario.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuantityUpdateDTO {
    private Long productoId;
    private Long sucursalId;
    private Long cantidad;
}
