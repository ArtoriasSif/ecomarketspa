package com.nebulosa.msvc_inventario.controllers;


import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/inventario")
@Validated
public class InventoryController {
    //NO SE PUEDE PROBAR HASTA QUE TERMINEN MSVC-SUCURSAL
    @Autowired
    private InventoryService inventoryService;



    @GetMapping
    public ResponseEntity<Inventory> findByProductoAndSucursal(
            @RequestParam Long idProducto,
            @RequestParam Long idSucursal
    ) {
        Inventory inventario = inventoryService.findByProductoAndSucursal(idProducto, idSucursal);
        if (inventario == null) {
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
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @Validated @RequestBody Inventory inventory){
        return ResponseEntity
                .status(200)
                .body(inventoryService.updateQuantity(id, inventory.getCantidad()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id){
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
