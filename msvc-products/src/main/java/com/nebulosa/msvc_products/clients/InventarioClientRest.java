package com.nebulosa.msvc_products.clients;

import com.nebulosa.msvc_products.models.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

    @GetMapping
    List<Inventario> findAll();

    @DeleteMapping("/{id}")
    void deleteInventoryById(@PathVariable Long id);
}
