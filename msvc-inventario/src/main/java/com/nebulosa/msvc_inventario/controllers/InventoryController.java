package com.nebulosa.msvc_inventario.controllers;


import com.nebulosa.msvc_inventario.dtos.ErrorDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryResponseDTO;
import com.nebulosa.msvc_inventario.dtos.QuantityUpdateDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
@Validated
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene inventario con nombre del producto y sucursal",
            description = "Devuelve el inventario con el nombre del producto y sucursal asociado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(schema = @Schema(implementation = InventoryDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inventario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameter(name = "id", description = "ID del inventario", required = true, example = "1")
    public ResponseEntity<InventoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }


    @GetMapping
    @Operation(
            summary = "Listar todo el inventario",
            description = "Devuelve una lista con todos los registros de inventario disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventoryDTO.class)))
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
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class)))
            )
    })
    public ResponseEntity<List<InventoryResponseDTO>> findAllWithDetails() {
        List<InventoryResponseDTO> inventarios = inventoryService.findAllWithDetails();
        return ResponseEntity.ok(inventarios);
    }


    @GetMapping("/sucursal/{idSucursal}")
    @Operation(
            summary = "Buscar inventario por ID de sucursal",
            description = "Devuelve una lista del inventario en la sucursal indicada, incluyendo nombres del producto y sucursal"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario encontrado",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @Parameter(
            name = "idSucursal",
            description = "ID de la sucursal para buscar inventario",
            required = true
    )
    public ResponseEntity<List<Inventory>> findByIdSucursal(@PathVariable Long idSucursal) {
        List<Inventory> inventario = inventoryService.findByIdSucursal(idSucursal);
        // No retornar 404 si la lista está vacía
        return ResponseEntity.ok(inventario);
    }


    @GetMapping("/sucursal/detallado/{idSucursal}")
    @Operation(
            summary = "Obtener inventario detallado por sucursal",
            description = "Devuelve el inventario con nombres de producto y sucursal para una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario detallado encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada o sin inventario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameter(
            name = "idSucursal",
            description = "ID de la sucursal para consultar inventario detallado",
            required = true,
            example = "2001"
    )
    public ResponseEntity<List<InventoryResponseDTO>> findDetalleBySucursal(@PathVariable Long idSucursal) {
        List<InventoryResponseDTO> dtos = inventoryService.findDetalleBySucursal(idSucursal);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(
            summary = "Crear nuevo inventario",
            description = "Crea un nuevo registro de inventario para un producto en una sucursal. Valida la existencia del producto y la sucursal antes de guardar."
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
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryDTO request) {
        InventoryResponseDTO response = inventoryService.save(request);

        // Se puede incluir el header Location si quieres seguir prácticas RESTful
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getIdInventario())
                .toUri();

        return ResponseEntity.created(location).body(response);
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
    public ResponseEntity<?> updateQuantity(@RequestBody QuantityUpdateDTO dto ){
        return ResponseEntity
                .ok(inventoryService.updateQuantity(dto.getProductoId(), dto.getSucursalId(), dto.getCantidad()));
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
        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Eliminar inventario por ID",
            description = "Elimina el inventario correspondiente al ID proporcionado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inventario eliminado exitosamente",
                    content = @Content(mediaType = "text/plain")
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
    public ResponseEntity<?> deleteInventoryById(@PathVariable Long id) {
        try {
            inventoryService.deleteById(id);
            return ResponseEntity.status(200)
                    .body("Inventario eliminado exitosamente");
        } catch (Exception ex) {
            return ResponseEntity.status(404)
                    .body(ex.getMessage());
        }
    }
}
