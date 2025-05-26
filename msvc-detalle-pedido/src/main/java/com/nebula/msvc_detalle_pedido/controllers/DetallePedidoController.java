package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.models.DetallePedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/detalle-pedido")
@Validated
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;

    @PostMapping
    public ResponseEntity<DetallePedido> save (@ResponseBody DetallePedido detallePedido){
        DetallePedido datelles = detallePedidoService.save(detallePedido);
        return ResponseEntity.status(201).body(datelles);
    }

}
