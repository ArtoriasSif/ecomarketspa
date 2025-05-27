package com.nebula.msvc_detalle_pedido.clients;

import com.nebula.msvc_detalle_pedido.dtos.QuantityUpdateDTO;
import com.nebula.msvc_detalle_pedido.models.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

    @GetMapping("/sucursal/{idSucursal}")
    List<Inventario> findByIdSucursal(@PathVariable("idSucursal") Long idSucursal);

    @PutMapping("/actualizar")
    void updateQuantity (QuantityUpdateDTO dto);

}
