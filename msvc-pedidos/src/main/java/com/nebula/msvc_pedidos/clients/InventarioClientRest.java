package com.nebula.msvc_pedidos.clients;



import com.nebula.msvc_pedidos.dtos.QuantityUpdateDTO;
import com.nebula.msvc_pedidos.models.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

    @GetMapping("/sucursal/{idSucursal}")
    List<Inventario> findByIdSucursal(@PathVariable("idSucursal") Long idSucursal);


    @PutMapping("/actualizar")
    void updateQuantity(@RequestBody QuantityUpdateDTO dto);
}
