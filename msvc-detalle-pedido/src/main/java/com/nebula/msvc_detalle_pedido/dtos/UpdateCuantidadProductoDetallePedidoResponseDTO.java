package com.nebula.msvc_detalle_pedido.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
@Schema(name = "UpdateCantidadProductoDetallePedidoResponseDTO",
        description = "DTO de respuesta para actualización de cantidad en detalle de pedido",
        example = "{\n" +
                "  \"idPedido\": 123,\n" +
                "  \"nombreUsuario\": \"Juan Perez\",\n" +
                "  \"nombreSucursal\": \"Sucursal Central\",\n" +
                "  \"nombreProducto\": \"Teclado Mecánico\",\n" +
                "  \"cantidad\": 5,\n" +
                "  \"precioUnitario\": 150.0,\n" +
                "  \"subTotal\": 750.0\n" +
                "}")
public class UpdateCuantidadProductoDetallePedidoResponseDTO {

    private Long idPedido;

    private String nombreUsuario;

    private String nombreSucursal;

    private String nombreProducto;

    private Long cantidad;

    private Double precioUnitario;

    private Double subTotal;

}
