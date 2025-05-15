package com.nebulosa.msvc_inventario.clients;

import com.nebulosa.msvc_inventario.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-products", url = "http://localhost:8083/api/v1/products")
public interface ProductoClientRest {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Product findByIdProducto(@PathVariable Long id);


}
