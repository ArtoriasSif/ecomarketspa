package com.nebula.msvc_pedidos.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class DetallePedido {

    private Long idDetallePedido;

    private Long idPedido;

    private Long idProducto;

    private Long cantidad;

    private Double subTotal;
}
