package com.nebulosa.msvc_inventario.services;


import com.nebulosa.msvc_inventario.models.entities.Inventory;

import java.util.List;

public interface InventoryService {

    Inventory findByIdInvetory(Long id);
    List<Inventory> findAll();
    Inventory save (Inventory inventory);
    Inventory updateQuantity(Long idInventory , Inventory inventory);
    void deleteById(Long id);
    List<Inventory> findByIdSucursal(Long idSucursal);
}
