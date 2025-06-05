package com.nebula.msvc_detalle_pedido.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantidadProductoPedidoDTO {
    private Long pedidoId;
    private Long cantidad;
}
