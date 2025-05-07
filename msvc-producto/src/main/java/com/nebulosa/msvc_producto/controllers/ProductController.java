package com.nebulosa.msvc_producto.controllers;

import com.nebulosa.msvc_producto.models.Product;
import com.nebulosa.msvc_producto.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/inventario")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProductos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAllProducto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product>getProductoById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findByIdProducto(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProducto(@Validated @RequestBody Product producto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(producto));
    }
}
