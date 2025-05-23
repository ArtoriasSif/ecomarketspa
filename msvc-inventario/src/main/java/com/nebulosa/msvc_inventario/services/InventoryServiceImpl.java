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
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Transactional
    @Override
    public Inventory findByIdInvetory (Long id){
        return inventoryRepository.findById(id).orElseThrow(
                () -> new InventoryException("No se encontró el inventario con id: " + id)
        );
    }

    public List<Inventory> findByIdSucursal(Long idSucursal) {
        return inventoryRepository.findByIdSucursal(idSucursal);
    }

    @Transactional
    @Override
    public Inventory save(Inventory inventory) {
        //Validar que existe producto
        try {
            productoClientRest.findByIdProducto(inventory.getIdProducto());
        } catch (NotFound ex) {
            throw new InventoryException("No se encontró el producto con id: " + inventory.getIdProducto());
        }
        //Validar que exite la sucursal
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
    public Inventory updateQuantity(Long inventoryId, Inventory inventory) {
        return  inventoryRepository.findById(inventoryId).map(i -> {
            if (i.getCantidad() + inventory.getCantidad() < 0) {
                throw new InventoryException("La cantidad a actualizar no puede ser negativa." +
                        " Cantidad actual: " + i.getCantidad());
            }
            i.setCantidad(i.getCantidad() + inventory.getCantidad());
            return inventoryRepository.save(i);
        }).orElseThrow(
                () -> new InventoryException("No se encontró el inventario con id: " + inventoryId)
        );
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (inventoryRepository.findById(id).isPresent()) {
            Inventory inventory = inventoryRepository.findById(id).get();
            if(inventory.getCantidad() == 0){
                inventoryRepository.deleteById(id);
            }else {
                throw new InventoryException("No se puede eliminar el inventario con id: " + id +
                        " porque tiene el producto "+productoClientRest.findByIdProducto(inventory.getIdProducto()).getNombreProducto()+
                        " una cantidad mayor que 0. ");
            }
        } else {
            throw new InventoryException("No se encontró el inventario con id: " + id);
        }
    }
}

