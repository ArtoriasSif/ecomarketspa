package com.nebulosa.msvc_inventario.services;


import com.nebulosa.msvc_inventario.models.entities.Inventory;

import java.util.List;

public interface InventoryService {

    Inventory findById(Long id);
    List<Inventory> findAll();
    Inventory save (Inventory inventory);
    Inventory updateQuantity(Long productoId, Long sucursalId, Long quantity);
    void deleteById(Long id);
    List<Inventory> findByIdSucursal(Long idSucursal);
    Inventory findByIdProductoAndIdSucursal(Long productoId, Long sucursalId);
    void updateInventory(Long id);
}
