package com.nebula.msvc_pedidos.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoConDetalleDTO {
    @JsonIgnore
    private Long idPedido;

    @Schema(description = "Nombre Usuario", example = "Solaire de Astora")
    private String nombreUsuario;
    @Schema(description = "Rut usuario", example = "22333444-5")
    private String rutUsuario;
    @Schema(description = "Nombre Sucursal", example = "Firelink Shrine ")
    private String nombreSucursal;
    @ArraySchema(
            arraySchema = @Schema(description = "Lista de productos detallados del pedido"),
            schema = @Schema(implementation = DetallePedidoDTO.class)
    )
    private List<DetallePedidoDTO> detalles;
    @Schema(description = "Total del pedido (suma de subtotales)", example = "14500.50")
    private Double total;
}
