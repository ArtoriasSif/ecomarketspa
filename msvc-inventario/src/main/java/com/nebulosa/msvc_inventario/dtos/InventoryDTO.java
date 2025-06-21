package com.nebulosa.msvc_inventario.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Datos necesarios para crear un nuevo inventario")
public class InventoryDTO {
    @Schema(description = "ID del producto", example = "1001", required = true)
    private Long productoId;
    @Schema(description = "ID de la sucursal", example = "2001", required = true)
    private Long sucursalId;
    @Schema(description = "Cantidad de productos", example = "25", required = true)
    private Long quantity;
}
