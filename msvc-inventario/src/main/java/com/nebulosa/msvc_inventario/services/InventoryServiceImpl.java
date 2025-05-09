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
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductoClientRest productoClientRest;

    @Autowired
    private SucursalClientRest sucursalClientRest;

    @Transactional
    @Override
    public Inventory save(Inventory inventory) {
        try {
            productoClientRest.findByIdProducto(inventory.getProductoId());
        } catch (FeignException ex) {
            throw new InventoryException("No se encontró el producto con id: " + inventory.getProductoId());
        }
        try {
            sucursalClientRest.findByIdSucursal(inventory.getSucursalId());
        } catch (FeignException ex) {
            throw new InventoryException("No se encontró la sucursal con id: " + inventory.getSucursalId());
        }
        if (inventoryRepository.findByProductoIdAndSucursalId(inventory.getProductoId(), inventory.getSucursalId()).isPresent()) {
            throw new InventoryException("La sucursal " + inventory.getSucursalId() +
                    " ya contiene el producto " + inventory.getProductoId() +
                    ". Actualice el inventario con ID: " + inventory.getInventarioId());
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (inventoryRepository.findById(id).isPresent()) {
            inventoryRepository.deleteById(id);
        } else {
            throw new InventoryException("No se encontro el inventario con id: " + id);
        }
    }
}

