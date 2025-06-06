package com.nebulosa.msvc_inventario.controllers;


import com.nebulosa.msvc_inventario.dtos.QuantityUpdateDTO;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin ("http://localhost:5500")
@RestController
@RequestMapping("/api/v1/inventario")
@Validated
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }


    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<Inventory>> findByIdSucursal(@PathVariable Long idSucursal) {
        List<Inventory> inventario = inventoryService.findByIdSucursal(idSucursal);
        // No retornar 404 si la lista está vacía
        return ResponseEntity.ok(inventario);
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Validated @RequestBody Inventory inventory){
        return ResponseEntity
                .status(201)
                .body(inventoryService.save(inventory));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> updateQuantity(@RequestBody QuantityUpdateDTO dto ){
        return ResponseEntity
                .ok(inventoryService.updateQuantity(dto.getProductoId(), dto.getSucursalId(), dto.getCantidad()));
    }

    @PutMapping("/{id}")
    public void updateInventory(@PathVariable Long id){
        inventoryService.updateInventory(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventoryById(@PathVariable Long id) {
        try {
            inventoryService.deleteById(id);
            return ResponseEntity.status(200)
                    .body("Inventario eliminado exitosamente");
        } catch (Exception ex) {
            return ResponseEntity.status(404)
                    .body(ex.getMessage());
        }
    }
}
