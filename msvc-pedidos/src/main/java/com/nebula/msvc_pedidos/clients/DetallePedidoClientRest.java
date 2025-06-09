package com.nebula.msvc_pedidos.clients;

import com.nebula.msvc_pedidos.models.DetallePedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-detalle-pedido", url = "http://localhost:8087/api/v1/detalle")
public interface DetallePedidoClientRest {

    @GetMapping("/{id}")
    List<DetallePedido> findByIdPedido(@PathVariable Long id);

    @DeleteMapping("/{idPedido}")
    void deleteByIdPedido(@PathVariable Long idPedido);

    @GetMapping
    List<DetallePedido> findAll();
}
