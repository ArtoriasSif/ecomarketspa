package com.nebulosa.msvc_inventario.clients;

import com.nebulosa.msvc_inventario.models.Sucursal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-sucursal", url = "http://localhost:8082/api/v1/sucursal")
public interface SucursalClientRest {
    @GetMapping
    List<Sucursal> findAll();

    @GetMapping("/id/{id}")
    Sucursal findByIdSucursal(@PathVariable Long id);
}
