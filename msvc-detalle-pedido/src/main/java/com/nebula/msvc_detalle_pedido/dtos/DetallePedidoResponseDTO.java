package com.nebula.msvc_detalle_pedido.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Detalle de pedido response DTO",
        example = "[{\"idDetallePedido\":1,\"idPedido\":2,\"nombreUsuario\":\"Juan\",\"nombreSucursal\":\"Central\",\"nombreProducto\":\"Mouse\",\"cantidad\":3,\"precioUnitario\":15.5,\"subTotal\":46.5}]"
)
public class DetallePedidoResponseDTO {

    private Long idDetallePedido;

    private Long idPedido;

    private String nombreUsuario;

    private String nombreSucursal;

    private String nombreProducto;

    private Long cantidad;

    private Double precioUnitario;

    private Double subTotal;
}