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
@Schema(name = "UpdateCantidadProductoDetallePedidoDTO", description = "DTO para actualizar la cantidad de un producto en el detalle del pedido")

public class UpdateCuantidadProductoDetallePedidoDTO {

    private Long cantidad;
}
