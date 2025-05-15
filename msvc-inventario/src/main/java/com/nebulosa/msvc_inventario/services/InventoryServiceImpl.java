package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.clients.ProductoClientRest;
import com.nebulosa.msvc_inventario.clients.SucursalClientRest;
import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import feign.FeignException;
import feign.FeignException.NotFound;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Inventory findByProductoAndSucursal(Long idProducto, Long idSucursal) {
        return inventoryRepository.findByIdProductoAndIdSucursal(idProducto, idSucursal)
                .orElseThrow(() -> new InventoryException("No se encontró inventario con productoId " + idProducto + " y sucursalId " + idSucursal));
    }

    public List<Inventory> findByIdSucursal(Long idSucursal) {
        return inventoryRepository.findByIdSucursal(idSucursal);
    }

    @Transactional
    @Override
    public Inventory save(Inventory inventory) {
        try {
            productoClientRest.findByIdProducto(inventory.getIdProducto());
        } catch (NotFound ex) {
            throw new InventoryException("No se encontró el producto con id: " + inventory.getIdProducto());
        }

        try {
            sucursalClientRest.findByIdSucursal(inventory.getIdSucursal());
        } catch (NotFound ex) {
            throw new InventoryException("No se encontró la sucursal con id: " + inventory.getIdSucursal());
        }

        inventoryRepository.findByIdProductoAndIdSucursal(inventory.getIdProducto(), inventory.getIdSucursal())
                .ifPresent(existingInventory -> {
                    throw new InventoryException("Ya existe un inventario para el producto " +
                            inventory.getIdProducto() + " en la sucursal " + inventory.getIdSucursal() +
                            ". Actualice el inventario con ID: " + existingInventory.getIdInventario());
                });

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

