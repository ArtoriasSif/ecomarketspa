package com.nebulosa.msvc_inventario.services;


import com.nebulosa.msvc_inventario.dtos.InventoryDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryResponseDTO;
import com.nebulosa.msvc_inventario.models.entities.Inventory;

import java.util.List;

public interface InventoryService {

    InventoryResponseDTO findById (Long id);
    List<Inventory> findAll();
    List<InventoryResponseDTO> findAllWithDetails();
    InventoryResponseDTO save(InventoryDTO dto);
    Inventory updateQuantity(Long productoId, Long sucursalId, Long quantity);
    void deleteById(Long id);
    List<Inventory> findByIdSucursal(Long idSucursal);
    List<InventoryResponseDTO> findDetalleBySucursal(Long idSucursal) ;
    Inventory findByIdProductoAndIdSucursal(Long productoId, Long sucursalId);
    InventoryResponseDTO updateInventory(Long id);
}
