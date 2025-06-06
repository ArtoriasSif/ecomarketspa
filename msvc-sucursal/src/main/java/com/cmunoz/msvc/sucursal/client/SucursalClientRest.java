package com.cmunoz.msvc.sucursal.client;


import com.cmunoz.msvc.sucursal.models.Entitys.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


import java.util.List;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface SucursalClientRest {

    @GetMapping("/sucursal/{idSucursal}")
    List<Inventario> findByIdSucursal(@PathVariable Long idSucursal);

    @DeleteMapping("/{id}")
    void deleteInventoryById(@PathVariable Long id);

    @DeleteMapping("/sucursal/{idSucursal}")
    void deletePedidoSucursal(@PathVariable Long idSucursal);

    @PutMapping("/{id}")
    void updateInventory(@PathVariable Long id);
}
