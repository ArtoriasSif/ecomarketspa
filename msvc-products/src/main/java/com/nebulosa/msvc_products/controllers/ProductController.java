package com.nebulosa.msvc_products.controllers;

import com.nebulosa.msvc_products.dtos.PrecioUpdateDTO;
import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.models.entities.Product;
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
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findByIdProducto(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Product> getProductosByNombre(@PathVariable String nombre){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.findByNombreProducto(nombre));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(@Validated @RequestBody Product producto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updatePrecioProducto(
            @PathVariable Long id,
            @Validated @RequestBody PrecioUpdateDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.updatePrice(id, dto.getPrecio()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.deleteByIdProducto(id));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    //Endpoint de las clases clients en otros msvc ??
    @GetMapping("/productoDTO")
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductosDTO(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAllProductDTO());
    }

}
