package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.dtos.UpdateQuantidadProductoPedidoDTO;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1/detalle")
@Validated
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;

    //Lista los detalles de productos que tiene como parametro Id Pedido
    @GetMapping("/{idPedido}")
    public ResponseEntity<List<DetallePedido>> findByIdPedido(@PathVariable Long idPedido) {
        List<DetallePedido> detalles = detallePedidoService.findByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    //Crea Update de los detalles de productos
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePedido(@PathVariable Long id, @RequestBody UpdateQuantidadProductoPedidoDTO updateDTO) {
        return ResponseEntity.status(200).body(detallePedidoService.updateCatidadProductoPedido(id, updateDTO));
    }

    //Crea los detalles de pedido recebidos de postman
    @PostMapping
    public ResponseEntity <List<DetallePedido>> save (@RequestBody List<DetallePedido> detallePedidos){
        List<DetallePedido> datelles = detallePedidoService.save(detallePedidos);
        return ResponseEntity.status(201).body(datelles);
    }

    @GetMapping // Este método manejará GET a /api/v1/detalle
    public ResponseEntity<List<DetallePedido>> findAllDetalles() {
        return ResponseEntity.ok(detallePedidoService.findAll()); // Asumiendo que tienes un método findAll
    }

    //Deleta todos los detalles de asociados al Id Pedido, utilizado por client en msvc-pedido
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
