package com.nebula.msvc_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetalleDTO {
    private String nombreProducto;
    private double precioUnitario;
    private int cantidad;
    private double subtotal;
}
