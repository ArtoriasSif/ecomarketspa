package com.nebula.msvc_detalle_pedido.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DetallePedidoRequestDTO", description = "DTO para solicitar la creación o consulta de detalle de pedido",
        example = "{\"idPedido\":123, \"idProducto\":456, \"cantidad\":2}")
public class DetallePedidoRequestDTO {
    private Long idPedido;
    private Long idProducto;
    private Long cantidad;
}
