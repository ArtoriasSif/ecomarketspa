package com.nebula.msvc_detalle_pedido.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-products", url = "http://localhost:8083/api/v1/products")
public interface ProductoClientRest {

    @GetMapping("/{id}")
    Producto findByIdProducto(@PathVariable("id") Long id);

}