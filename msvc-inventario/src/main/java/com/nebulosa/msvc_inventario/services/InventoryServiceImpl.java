package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.clients.ProductoClientRest;
import com.nebulosa.msvc_inventario.clients.SucursalClientRest;
import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import com.nebulosa.msvc_inventario.models.Product;
import com.nebulosa.msvc_inventario.models.Sucursal;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductoClientRest productoClientRest;

    @Autowired
    private SucursalClientRest sucursalClientRest;


    @Override
    public Inventory save(Inventory inventory) {
        try {
            Product product = this.productoClientRest.findByIdProducto(inventory.getProductoId());
            Sucursal sucursal = this.sucursalClientRest.findByIdSucursal(inventory.getSucursalId());
            if (inventoryRepository.findByIdProductoAndIdSucursal(inventory.getProductoId(), inventory.getSucursalId()).isPresent()) {
                throw new InventoryException("El inventario de la Sucursal:" + inventory.getSucursalId() +
                        " ya contiene el producto de la id " + inventory.getProductoId() + " actualize " +
                        "el inventario de la id : " + inventory.getInventarioId());
            }
            return inventoryRepository.save(inventory);
        }catch (FeignException ex){ //revisar!!
            throw new InventoryException("No se encontro el producto o la sucursal con id: " +
                    "" + inventory.getProductoId() + " " + inventory.getSucursalId());
        }
    }

    @Override
    public Inventory updateQuantity(Long inventoryId, Long quantity) {
        return  inventoryRepository.findById(inventoryId).map(i -> {
            if (i.getCantidad() + quantity < 0) {
                throw new InventoryException("La cantidad a actualizar no puede ser negativa." +
                        " Cantidad actual: " + i.getCantidad());
            }
            i.setCantidad(i.getCantidad() + quantity);
            return inventoryRepository.save(i);
        }).orElseThrow(
                () -> new InventoryException("No se encontro el inventario con id: " + inventoryId)
        );
    }
}

