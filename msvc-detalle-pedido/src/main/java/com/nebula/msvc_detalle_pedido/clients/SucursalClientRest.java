package com.nebula.msvc_detalle_pedido.clients;

import com.nebula.msvc_detalle_pedido.models.Sucursal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-sucursal", url = "http://localhost:8089/api/v1/sucursal")

public interface SucursalClientRest {
    @GetMapping("/id/{id}")
    Sucursal findByIdSucursal(@PathVariable("id") Long id);
}
