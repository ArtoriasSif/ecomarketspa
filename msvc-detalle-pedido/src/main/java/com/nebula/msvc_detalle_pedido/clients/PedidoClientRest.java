package com.nebula.msvc_detalle_pedido.clients;

import com.nebula.msvc_detalle_pedido.models.Pedido;
import lombok.*;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "msvc-detalle-pedido", url = "http://localhost:8087//api/v1/pedido")
public interface PedidoClientRest {


    @GetMapping("/{id}")
    Pedido findById(@PathVariable Long id);

}
