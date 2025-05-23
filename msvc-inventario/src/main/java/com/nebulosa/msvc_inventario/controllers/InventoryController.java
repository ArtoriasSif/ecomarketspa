package com.nebulosa.msvc_inventario.controllers;


import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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
    public ResponseEntity<Inventory> findByIdInvetory(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.findByIdInvetory(id));
    }


    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<Inventory>> findByIdSucursal(@PathVariable Long idSucursal) {
        List<Inventory> inventario = inventoryService.findByIdSucursal(idSucursal);
        if (inventario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventario);
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Validated @RequestBody Inventory inventory){
        return ResponseEntity
                .status(201)
                .body(inventoryService.save(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventoryQuantity(@PathVariable Long id,@Validated @RequestBody Inventory inventory){
        return ResponseEntity
                .status(200)
                .body(inventoryService.updateQuantity(id, inventory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventoryById(@PathVariable Long id){
        try{
            inventoryService.deleteById(id);
            return ResponseEntity.status(200)
                    .body("Inventario eliminado exitosamente");
        } catch (Exception ex){
            return ResponseEntity.status(404)
                    .body(ex.getMessage());
        }
    }

}
