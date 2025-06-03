package com.nebula.msvc_pedidos.controllers;

import com.nebula.msvc_pedidos.clients.DetallePedidoClientRest;
import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedido")
@Validated
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoClientRest detallePedidoClientRest;


    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(pedidoService.findById(id));
    }



    @PostMapping
    public ResponseEntity<PedidoResponseDTO> save(@RequestBody PedidoDTO pedidoDTO) {
        PedidoResponseDTO response = pedidoService.save(pedidoDTO);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> findByIdPedido(@PathVariable Long id) {
        try {
            PedidoConDetalleDTO detalles = pedidoService.findPedidoConDetalles(id);
            return ResponseEntity.ok(detalles);
        } catch (PedidoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    //Determinado como ? dado que en Usuario se ucupara un metodo VOID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try{
            String mensaje = pedidoService.deletePedidoId(id);
            return ResponseEntity.status(200).body(mensaje);
        }catch (Exception ex){
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }


}
