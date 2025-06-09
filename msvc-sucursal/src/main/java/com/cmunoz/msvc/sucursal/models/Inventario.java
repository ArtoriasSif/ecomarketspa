package com.cmunoz.msvc.sucursal.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    private Long idInventario;
    private Long idProducto;
    private Long idSucursal;
    private Long cantidad;
}
