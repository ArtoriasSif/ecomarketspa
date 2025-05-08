package com.nebulosa.msvc_products.controllers;

import com.nebulosa.msvc_products.models.Product;
import com.nebulosa.msvc_products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProductos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAllProduct());
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

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProducto(@PathVariable Long id, @Validated @RequestBody Product producto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.updatePrice(id, producto.getPrecio()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){
        try{
            productService.deleteByIdProducto(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Producto eliminado exitosamente");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
