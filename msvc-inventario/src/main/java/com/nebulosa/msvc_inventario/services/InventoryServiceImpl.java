package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;


    @Override
    public Inventory save(Inventory inventory) {

        if (inventoryRepository.findByIdProductoAndIdSucursal(inventory.getProductoId(), inventory.getSucursalId()).isPresent()) {
            throw new InventoryException("El inventario de la Sucursal:"+inventory.getSucursalId()+
                    " ya contiene el producto de la id "+inventory.getProductoId());
        }
        return inventoryRepository.save(inventory);

    }


}
