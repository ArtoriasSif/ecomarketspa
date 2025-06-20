package com.nebulosa.msvc_inventario.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Inventario del producto en una sucursal")
public class InventoryDTO {
    private Long productoId;
    private Long sucursalId;
    private Long quantity;
}
