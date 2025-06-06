package com.cmunoz.msvc.sucursal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-pedidos", url = "http://localhost:8085/api/v1/pedido")
public interface PedidoClientRest {


    @DeleteMapping("/sucursal/{idSucursal}")
    void deletePedidoSucursal(@PathVariable Long idSucursal);

}
