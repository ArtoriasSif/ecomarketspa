package com.nebula.msvc_detalle_pedido.controllers;

import com.nebula.msvc_detalle_pedido.assemblers.DetallePedidoModelAssembler;
import com.nebula.msvc_detalle_pedido.assemblers.UpdateCantidadDetallePedidoModelAssembler;
import com.nebula.msvc_detalle_pedido.dtos.*;
import com.nebula.msvc_detalle_pedido.services.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v2/detalle")
@Validated
@Tag(name = "Detalles Pedidos V2", description = "Operaciones HATEOAS sobre detalles de pedidos")
@RequiredArgsConstructor
public class DetallePedidoControllerV2 {

    private final DetallePedidoService detallePedidoService;
    private final DetallePedidoModelAssembler detallePedidoModelAssembler;
    private final UpdateCantidadDetallePedidoModelAssembler updateCantidadDetallePedidoModelAssembler;

    @GetMapping("/dto/{idPedido}")
    @Operation(
            summary = "Obtiene los detalles de un pedido (V2)",
            description = "Devuelve los detalles de un pedido en formato HAL con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalles encontrados correctamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DetallePedidoResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron detalles para el pedido",
                    content = @Content(schema = @Schema(implementation = Error.class))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<DetallePedidoResponseDTO>>> findDetallesByIdPedido(
            @PathVariable @NotNull Long idPedido) {

        List<DetallePedidoResponseDTO> detalles = detallePedidoService.findDetailsByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<DetallePedidoResponseDTO>> recursos = detalles.stream()
                .map(detallePedidoModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DetallePedidoResponseDTO>> collectionModel =
                CollectionModel.of(recursos,
                        linkTo(methodOn(DetallePedidoControllerV2.class).findDetallesByIdPedido(idPedido)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }



    @GetMapping
    @Operation(
            summary = "Obtiene todos los detalles de pedidos (V2)",
            description = "Devuelve una lista completa de los detalles de pedidos registrados en formato HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = DetallePedidoResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron detalles de pedidos"
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<DetallePedidoResponseDTO>>> findAll() {
        List<DetallePedidoResponseDTO> detalles = detallePedidoService.findAllDetallesDTO();
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<DetallePedidoResponseDTO>> recursos = detalles.stream()
                .map(detallePedidoModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<DetallePedidoResponseDTO>> collectionModel = CollectionModel.of(
                recursos,
                linkTo(methodOn(DetallePedidoControllerV2.class).findAll()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping
    @Operation(
            summary = "Guarda una lista de detalles de pedido",
            description = "Crea detalles de pedido, actualiza inventario y retorna respuesta con enlaces HATEOAS"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Detalles creados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    })
    public ResponseEntity<CollectionModel<EntityModel<DetallePedidoResponseDTO>>> save(
            @RequestBody List<DetallePedidoRequestDTO> detallePedidosDto
    ) {
        List<DetallePedidoResponseDTO> guardados = detallePedidoService.save(detallePedidosDto);

        List<EntityModel<DetallePedidoResponseDTO>> modelos = guardados.stream()
                .map(detallePedidoModelAssembler::toModel)
                .toList();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CollectionModel.of(modelos));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualiza la cantidad de un producto en el pedido", description = "Actualiza la cantidad del detalle de pedido y ajusta inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "404", description = "Detalle de pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<EntityModel<UpdateCuantidadProductoDetallePedidoResponseDTO>> updatePedido(
            @Parameter(description = "ID del detalle de pedido", required = true)
            @PathVariable Long id,
            @RequestBody UpdateCuantidadProductoDetallePedidoDTO updateDTO) {

        UpdateCuantidadProductoDetallePedidoResponseDTO response = detallePedidoService.updateCantidadProductoPedido(id, updateDTO);

        EntityModel<UpdateCuantidadProductoDetallePedidoResponseDTO> recurso =
                updateCantidadDetallePedidoModelAssembler.toModel(response);

        return ResponseEntity.ok(recurso);
    }
}



