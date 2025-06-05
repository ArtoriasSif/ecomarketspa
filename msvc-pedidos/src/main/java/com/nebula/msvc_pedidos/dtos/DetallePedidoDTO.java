package com.nebula.msvc_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {

    private String nombreProducto;
    private Long cantidad;
    private Double precioUnitario;
    private Double subTotal;
}

