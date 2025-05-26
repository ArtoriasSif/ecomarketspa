package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/v1/detalle-pedido")
@Validated
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;

    @PostMapping
    public ResponseEntity <List<DetallePedido>> save (@ResponseBody List<DetallePedido> detallePedidos){
        List<DetallePedido> datelles = detallePedidoService.save(detallePedidos);
        return ResponseEntity.status(201).body(datelles);
    }

}
