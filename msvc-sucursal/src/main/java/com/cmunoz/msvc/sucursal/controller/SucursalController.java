package com.cmunoz.msvc.sucursal.controller;


import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.services.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/v1/sucursal")
@Validated
@RestController
@Tag(name ="Sucursal", description = "Operaciones CRUD de Sucursal")
public class    SucursalController {


    @Autowired
    private SucursalService sucursalService;



    @Operation(summary = "Obtener todas las sucursales", description = "Recupera una lista de todas las sucursales disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"nombre\": \"Sucursal Central\", \"direccion\": \"Av. Principal 123\"}, {\"id\": 2, \"nombre\": \"Sucursal Sur\", \"direccion\": \"Calle Falsa 456\"}]"))),
            @ApiResponse(responseCode = "404", description = "No se encontraron sucursales",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No hay sucursales registradas.\"}")
                    ))
    })
    @GetMapping
    public ResponseEntity<List<Sucursal>> getAllSucursales() {
        return ResponseEntity
                .ok()
                .body(sucursalService.findAllSucursal());
    }


    @Operation(summary = "Obtener una sucursal por ID", description = "Recupera los detalles de una sucursal específica utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Sucursal Central\", \"direccion\": \"Av. Principal 123\"}"))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal no encontrada con ID: 1\"}")
                    ))
    })

    @GetMapping("/id/{id}")
    public ResponseEntity<Sucursal> getSucursalFindById(
            @Parameter(description = "ID de la sucursal a buscar", required = true, example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.findByIdSucursal(id));
    }

    @Operation(summary = "Obtener una sucursal por nombre", description = "Recupera los detalles de una sucursal específica utilizando su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Sucursal Central\", \"direccion\": \"Av. Principal 123\"}"))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal no encontrada con nombre: Sucursal Central\"}")
                    ))
    })

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Sucursal> getSucursalByNombreSucursal(
            @Parameter(description = "Nombre de la sucursal a buscar", required = true, example = "Sucursal Central") @PathVariable String nombre) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sucursalService.findByNombreSucursal(nombre));
    }

    @Operation(summary = "Crear una nueva sucursal", description = "Registra una nueva sucursal en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"id\": 3, \"nombre\": \"Sucursal Este\", \"direccion\": \"Calle Inventada 789\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos duplicados",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: El nombre de la sucursal no puede estar vacío\"}")))
    })

    @PostMapping()
    public ResponseEntity<Sucursal> saveSucursal(
            @Parameter(description = "Datos de la sucursal a crear", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"nombre\": \"Sucursal Este\", \"direccion\": \"Calle Inventada 789\"}")))
            @Validated @RequestBody Sucursal sucursal) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sucursalService.save(sucursal));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una sucursal existente",
            description = "Actualiza los detalles de una sucursal específica por su ID y devuelve los datos actualizados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursal actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"idSucursal\": 1, \"nombre\": \"Sucursal Central (Actualizada)\", \"direccion\": \"Nueva Dirección 123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada para actualizar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal no encontrada con ID: 1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o error de validación",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: La dirección no puede estar vacía\"}")
                    )
            )
    })
    public ResponseEntity<Sucursal> updateSucursal(
            @Parameter(description = "ID de la sucursal a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(
                    description = "Nuevos datos de la sucursal",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"nombre\": \"Sucursal Central (Actualizada)\", \"direccion\": \"Nueva Dirección 123\"}")
                    )
            )
            @Validated @RequestBody Sucursal sucursal) {

        Sucursal actualizada = sucursalService.updateByIdSucursal(id, sucursal);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sucursal por ID", description = "Elimina una sucursal del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal eliminada exitosamente",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Sucursal eliminada exitosamente"))),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada para eliminar",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Sucursal no encontrada con ID: 1")))
    })

    public ResponseEntity<String> deleteSucursal(
            @Parameter(description = "ID de la sucursal a eliminar", required = true, example = "1") @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalService.deleteByIdSucursal(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
