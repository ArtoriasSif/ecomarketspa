package com.nebulosa.msvc_inventario.services;


import com.nebulosa.msvc_inventario.models.entities.Inventory;

import java.util.List;

public interface InventoryService {

    Inventory save (Inventory inventory);
    Inventory updateQuantity(Long idInventory , Long quantity);
    void deleteById(Long id);
    Inventory findByProductoAndSucursal(Long productoId, Long idSucursal);
    List<Inventory> findByIdSucursal(Long idSucursal);
}
