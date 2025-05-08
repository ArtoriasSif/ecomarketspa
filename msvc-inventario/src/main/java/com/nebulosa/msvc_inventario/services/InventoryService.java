package com.nebulosa.msvc_inventario.services;


import com.nebulosa.msvc_inventario.models.entities.Inventory;

public interface InventoryService {

    Inventory save (Inventory inventory);
    Inventory updateQuantity(Long inventoryId , Long quantity);


}
