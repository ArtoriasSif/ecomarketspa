package com.nebula.msvc_detalle_pedido.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "msvc-inventario", url = "http://localhost:8081/api/v1/inventario")
public interface InventarioClientRest {

}
