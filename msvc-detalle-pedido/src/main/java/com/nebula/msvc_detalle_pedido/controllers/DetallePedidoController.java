package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.dtos.UpdateQuantidadProductoPedidoDTO;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalle")
@Validated
@Tag(name = "Pedidos ", description = "Operacion CRUD de pedidos")
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

    @GetMapping
    @Operation(
            summary = "Obtiene todos los pedidos", description = "Una Array me devolve una lista de las cabeceras de los pedidos"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,  description = "Operacion exitosa"),
            @ApiResponse(responseCode = "404" ,  description = "Pedidos no encontrado")
    })

    @Parameters(
            @Parameter(name = "id", description = "Este es el id unico del pedido" , required = true)
    )


    public ResponseEntity<List<DetallePedido>> findAll() {
        return ResponseEntity.ok(detallePedidoService.findAll());
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
