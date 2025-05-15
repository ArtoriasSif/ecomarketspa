package com.nebulosa.msvc_inventario.mapper;

import com.nebulosa.msvc_inventario.dtos.InventoryDTO;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDTO toDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setProductoId(inventory.getIdProducto());
        dto.setSucursalId(inventory.getIdSucursal());
        dto.setQuantity(inventory.getCantidad());
        return dto;
    }

    public Inventory toEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setIdProducto(dto.getProductoId());
        inventory.setIdSucursal(dto.getSucursalId());
        inventory.setCantidad(dto.getQuantity());
        return inventory;
    }

}
