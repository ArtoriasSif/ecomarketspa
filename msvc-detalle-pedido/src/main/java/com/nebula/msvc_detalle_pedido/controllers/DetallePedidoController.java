package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/detalle")
@Validated
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;

    @GetMapping("/{idPedido}")
    public ResponseEntity<List<DetallePedido>> findByIdPedido(@PathVariable Long idPedido) {
        List<DetallePedido> detalles = detallePedidoService.findByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    @PostMapping
    public ResponseEntity <List<DetallePedido>> save (@RequestBody List<DetallePedido> detallePedidos){
        List<DetallePedido> datelles = detallePedidoService.save(detallePedidos);
        return ResponseEntity.status(201).body(datelles);
    }


    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> delete (@PathVariable Long idPedido) {
        try{
            detallePedidoService.deleteDetallePedido(idPedido);
            return ResponseEntity.noContent().build();
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

}
