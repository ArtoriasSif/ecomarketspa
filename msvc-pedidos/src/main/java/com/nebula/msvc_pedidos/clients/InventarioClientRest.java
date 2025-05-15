package com.nebula.msvc_pedidos.clients;



import com.nebula.msvc_pedidos.models.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

    @GetMapping("/sucursal/{idSucursal}")
    List<Inventario> findByIdSucursal(@PathVariable("idSucursal") Long idSucursal);

    @GetMapping
    Inventario findByIdProductoAndIdSucursal(
            @RequestParam("IdProducto") Long idProducto,
            @RequestParam("idSucursal") Long idSucursal
    );


    @PutMapping("/{id}")
    Inventario updateInventoryQuantity(@PathVariable("id") Long id, @RequestParam("quantity") Long quantity);
}
