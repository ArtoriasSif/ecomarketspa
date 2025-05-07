package com.nebulosa.msvc_inventario.controllers;

import com.nebulosa.msvc_inventario.models.Producto;
import com.nebulosa.msvc_inventario.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.PrivilegedAction;
import java.util.List;

@Controller
@RequestMapping("/api/v1/inventario")
@Validated
public class InventarioControler {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos(){
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(productoService.findAllProducto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto>getProductoById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productoService.findByIdProducto(id));
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@Validated @RequestBody Producto producto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoService.save(producto));
    }

}
