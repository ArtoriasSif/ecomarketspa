package com.nebulosa.msvc_products.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventario {

    private Long idInventario;

    private Long idProducto; // ID del producto desde msvc-products

    private Long idSucursal; // ID de la sucursal desde msvc-sucursal

    private Long cantidad;

}
