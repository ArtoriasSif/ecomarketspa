package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.dtos.*;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalle")
@Validated
@Tag(name = "Detalles Pedidos ", description = "Operación CRUD de detalles pedidos")
public class DetallePedidoController {

    @Autowired
    DetallePedidoService detallePedidoService;

    @GetMapping("/{idPedido}")
    @Operation(
            summary = "Lista los detalles de un pedido",
            description = "Devuelve todos los detalles asociados al pedido con el ID suministrado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron detalles para el pedido")
    })
    @Parameter(name = "idPedido", description = "ID del pedido", required = true)
    public ResponseEntity<List<DetallePedido>> findByIdPedido(@PathVariable Long idPedido) {
        List<DetallePedido> detalles = detallePedidoService.findByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/dto/{idPedido}")
    @Operation(
            summary = "Lista los detalles de un pedido como DTO",
            description = "Devuelve todos los detalles asociados al pedido con el ID suministrado en formato DTO"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron detalles para el pedido")
    })
    @Parameter(name = "idPedido", description = "ID del pedido", required = true)
    public ResponseEntity<List<DetallePedidoResponseDTO>> findDtoByIdPedido(@PathVariable Long idPedido) {
        List<DetallePedidoResponseDTO> detallesDTO = detallePedidoService.findDetailsByIdPedido(idPedido);
        return ResponseEntity.ok(detallesDTO);
    }


    @GetMapping
    @Operation(
            summary = "Obtiene todos los detalles de pedidos",
            description = "Devuelve una lista completa de los detalles de pedidos registrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "No se encontraron detalles de pedidos")
    })
    public ResponseEntity<List<DetallePedido>> findAll() {
        return ResponseEntity.ok(detallePedidoService.findAll());
    }

    //Crea Update de los detalles de productos
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza la cantidad de un producto en el pedido",
            description = "Recibe el ID del detalle del pedido y un objeto con la nueva cantidad del producto para actualizar "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "404", description = "Detalle de pedido no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @Parameters(value = {
            @Parameter(name = "id", description = "ID del detalle de pedido, para actualizar la cantidad del producto", required = true)
    })
    public ResponseEntity<UpdateCuantidadProductoDetallePedidoResponseDTO> updatePedido(@PathVariable Long id, @RequestBody UpdateCuantidadProductoDetallePedidoDTO updateDTO) {
        return ResponseEntity.status(200).body(detallePedidoService.updateCantidadProductoPedido(id, updateDTO));
    }


    //Crea los detalles de pedido recebidos de postman
    @PostMapping
    @Operation(
            summary = "Crear detalles de pedido",
            description = "Recibe una lista de detalles de pedido desde Postman o cualquier cliente, y los guarda en la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Detalles de pedido creados exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud con datos inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity <List<DetallePedidoResponseDTO>> save
    (@Valid @RequestBody List<DetallePedidoRequestDTO> detallePedidosDTO){
        List<DetallePedidoResponseDTO> datelles = detallePedidoService.save(detallePedidosDTO);
        return ResponseEntity.status(201).body(datelles);
    }

    //Deleta todos los detalles de asociados al Id Pedido, utilizado por client en msvc-pedido
    @DeleteMapping("/{idPedido}")
    @Operation(
            summary = "Eliminar detalles de pedido por ID de pedido",
            description = "Elimina todos los detalles de pedido asociados al ID de pedido proporcionado. Este endpoint es utilizado por el cliente en el microservicio de pedidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Detalles de pedido eliminados correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron detalles asociados al ID de pedido",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameter(
            name = "idPedido",
            description = "ID del pedido cuyos detalles serán eliminados",
            required = true
    )
    public ResponseEntity<Void> delete(@PathVariable Long idPedido) {
        detallePedidoService.deleteDetallePedido(idPedido);
        return ResponseEntity.noContent().build();
    }

}
