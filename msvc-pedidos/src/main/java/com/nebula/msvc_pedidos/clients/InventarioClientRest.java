package com.nebula.msvc_pedidos.clients;



import com.nebula.msvc_pedidos.models.Inventario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

    @GetMapping
    Inventario findByIdProductoAndIdSucursal(
            @RequestParam("IdProducto") Long idProducto,
            @RequestParam("idSucursal") Long idSucursal
    );
}
