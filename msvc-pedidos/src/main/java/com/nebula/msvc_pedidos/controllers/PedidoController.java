package com.nebula.msvc_pedidos.controllers;

import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/pedido")
@Validated
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(pedidoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> save(@RequestBody PedidoDTO pedidoDTO) {
        PedidoResponseDTO response = pedidoService.save(pedidoDTO);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity <List<PedidoResponseDTO>> findAll() {
        return ResponseEntity.status(200).body(pedidoService.findAll());
    }


}
