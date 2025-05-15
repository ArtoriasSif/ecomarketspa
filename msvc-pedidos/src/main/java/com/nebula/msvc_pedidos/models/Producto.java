package com.nebula.msvc_pedidos.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Producto {

    private Long idProducto;

    private String nombreProducto;

    private Double precio;

}
