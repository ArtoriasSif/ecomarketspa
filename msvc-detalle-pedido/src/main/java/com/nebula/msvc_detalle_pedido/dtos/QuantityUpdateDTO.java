package com.nebula.msvc_detalle_pedido.dtos;

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