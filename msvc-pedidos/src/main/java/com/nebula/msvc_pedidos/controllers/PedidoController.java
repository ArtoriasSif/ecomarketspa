package com.nebula.msvc_pedidos.controllers;

import com.nebula.msvc_pedidos.dtos.PedidoRequestDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/pedido")
@Validated
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> save(@RequestBody PedidoRequestDTO pedidoDTO) {
        PedidoResponseDTO response = pedidoService.save(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
