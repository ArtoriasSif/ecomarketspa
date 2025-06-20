package com.nebulosa.msvc_inventario.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Respuesta universal de inventario con nombres de producto y sucursal")
public class InventoryResponseDTO {

    @Schema(description = "ID del inventario", example = "1")
    private Long idInventario;

    @Schema(description = "ID del producto", example = "1001")
    private Long idProducto;

    @Schema(description = "Nombre del producto", example = "Espada de Astora")
    private String nombreProducto;

    @Schema(description = "ID de la sucursal", example = "2001")
    private Long idSucursal;

    @Schema(description = "Nombre de la sucursal", example = "Firelink Shrine")
    private String nombreSucursal;

    @Schema(description = "Cantidad de productos en inventario", example = "25")
    private Long cantidad;


}
