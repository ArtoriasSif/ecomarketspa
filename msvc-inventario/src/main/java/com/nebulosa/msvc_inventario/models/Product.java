package com.nebulosa.msvc_inventario.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long productoId;

    private String nombreProducto;

    private Double precioProducto;
}
