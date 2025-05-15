package com.nebula.msvc_pedidos.clients;


import com.nebula.msvc_pedidos.models.Sucursal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-sucursal", url = "http://localhost:8082/api/v1/sucursal")
public interface SucursalClientRest {
    @GetMapping("/id/{id}")
    Sucursal findByIdSucursal(@PathVariable("id") Long id);



}
