package com.nebulosa.msvc_inventario.controllers;


import com.nebulosa.msvc_inventario.assemblers.InventoryEntityModelAssembler;
import com.nebulosa.msvc_inventario.assemblers.InventoryResponseDTOAssembler;
import com.nebulosa.msvc_inventario.dtos.ErrorDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryResponseDTO;
import com.nebulosa.msvc_inventario.dtos.QuantityUpdateDTO;
import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
    @RequestMapping("/api/v2/inventario")
    @Tag(name = "Inventario V2", description = "Operaciones con inventario con soporte HATEOAS")
    public class InventoryControllerV2 {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryEntityModelAssembler inventoryEntityModelAssembler;

    @Autowired
    private InventoryResponseDTOAssembler inventoryResponseDTOAssembler;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene inventario con nombre del producto y sucursal",
            description = "Devuelve el inventario con el nombre del producto y sucursal asociado, incluyendo enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameter(name = "id", description = "ID del inventario", required = true, example = "1")
    public ResponseEntity<EntityModel<InventoryResponseDTO>> findById(@PathVariable Long id) {
        InventoryResponseDTO dto = inventoryService.findById(id);
        EntityModel<InventoryResponseDTO> resource = inventoryResponseDTOAssembler.toModel(dto);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(
            summary = "Listar todo el inventario",
            description = "Devuelve una lista con todos los registros de inventario disponibles (entidad pura)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Inventory.class))
                    )
            )
    })
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/Detallado")
    @Operation(
            summary = "Obtiene todos los inventarios con nombres de producto y sucursal",
            description = "Devuelve la lista completa del inventario con el nombre del producto y sucursal asociado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario listado exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron inventarios",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<List<InventoryResponseDTO>> findAllWithDetails() {
        List<InventoryResponseDTO> inventarios = inventoryService.findAllWithDetails();
        if (inventarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/sucursal/{idSucursal}")
    @Operation(
            summary = "Buscar inventario por ID de sucursal",
            description = "Devuelve una lista del inventario en la sucursal indicada con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Inventory.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Inventory>>> findByIdSucursal(@PathVariable Long idSucursal) {
        List<Inventory> inventarios = inventoryService.findByIdSucursal(idSucursal);

        if (inventarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<Inventory>> recursos = inventarios.stream()
                .map(inventoryEntityModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Inventory>> collectionModel = CollectionModel.of(recursos,
                linkTo(methodOn(InventoryControllerV2.class).findByIdSucursal(idSucursal)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/sucursal/detallado/{idSucursal}")
    @Operation(
            summary = "Buscar inventario detallado por ID de sucursal (HATEOAS)",
            description = "Devuelve una lista del inventario detallado en la sucursal indicada con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario detallado encontrado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada o sin inventario",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<InventoryResponseDTO>>> findDetalleBySucursal(@PathVariable Long idSucursal) {
        List<InventoryResponseDTO> dtos = inventoryService.findDetalleBySucursal(idSucursal);

        if (dtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<InventoryResponseDTO>> recursos = dtos.stream()
                .map(inventoryResponseDTOAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<InventoryResponseDTO>> collectionModel = CollectionModel.of(
                recursos,
                linkTo(methodOn(InventoryControllerV2.class).findDetalleBySucursal(idSucursal)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }


    @PostMapping
    @Operation(
            summary = "Crear nuevo inventario (con enlaces HATEOAS)",
            description = "Crea un nuevo inventario en la sucursal para un producto, validando existencia previa. Devuelve DTO con enlaces."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Inventario creado correctamente",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o inventario duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto o sucursal no encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del inventario a crear",
            required = true,
            content = @Content(schema = @Schema(implementation = InventoryDTO.class))
    )
    public ResponseEntity<EntityModel<InventoryResponseDTO>> createInventory(@Valid @RequestBody InventoryDTO request) {
        InventoryResponseDTO response = inventoryService.save(request);

        EntityModel<InventoryResponseDTO> model = inventoryResponseDTOAssembler.toModel(response);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getIdInventario())
                .toUri();

        return ResponseEntity.created(location).body(model);
    }



    @Operation(
            summary = "Actualizar la cantidad del inventario",
            description = "Permite actualizar la cantidad de un producto en una sucursal. La cantidad puede ser positiva o negativa. Si el resultado es negativo, lanza excepción."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Inventory.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o cantidad inválida",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @PutMapping("/actualizar")
    public ResponseEntity<Inventory> updateQuantity(@RequestBody @Validated QuantityUpdateDTO dto) {
        Inventory updatedInventory = inventoryService.updateQuantity(dto.getProductoId(), dto.getSucursalId(), dto.getCantidad());
        return ResponseEntity.ok(updatedInventory);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Reiniciar cantidad de inventario a 0",
            description = "Establece la cantidad del inventario a cero para el ID de inventario proporcionado y retorna los datos actualizados con detalle del producto y sucursal"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameter(
            name = "id",
            description = "ID del inventario a actualizar (cantidad será 0)",
            required = true
    )
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Long id) {
        InventoryResponseDTO response = inventoryService.updateInventory(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar inventario por ID",
            description = "Elimina el inventario correspondiente al ID proporcionado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Inventario eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(mediaType = "text/plain")
            )
    })
    @Parameter(
            name = "id",
            description = "ID del inventario a eliminar",
            required = true,
            example = "1"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryById(@PathVariable Long id) {
        try {
            inventoryService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (InventoryException ex) {
            return ResponseEntity.status(404).build();
        }
    }


}