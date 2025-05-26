package com.nebula.msvc_detalle_pedido.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Inventario {

    private Long idInventario;

    private Long idProducto; // ID del producto desde msvc-products

    private Long idSucursal; // ID de la sucursal desde msvc-sucursal

    private Long cantidad;
}