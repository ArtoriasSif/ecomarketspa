package com.nebulosa.msvc_inventario.dtos;

import lombok.Data;

@Data
public class InventoryDTO {
    private Long productoId;
    private Long sucursalId;
    private Long quantity;
}
