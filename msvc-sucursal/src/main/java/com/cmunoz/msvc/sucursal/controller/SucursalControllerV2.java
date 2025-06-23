package com.cmunoz.msvc.sucursal.controller;


import com.cmunoz.msvc.sucursal.assemblers.SucursalModelAssembler;
import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.services.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@RequestMapping("/api/v2/sucursal")
@Validated
@RestController
@Tag(name ="Sucursal V2", description = "Operaciones CRUD de Sucursal")
public class SucursalControllerV2 {


    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private SucursalModelAssembler sucursalModelAssembler;


    @GetMapping
    @Operation(
            summary = "Obtiene todas las sucursales con enlaces HATEOAS",
            description = "Devuelve una lista de todas las sucursales disponibles con enlaces auto descriptivos (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de sucursales recuperada exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Sucursal.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron sucursales",
                    content = @Content(schema = @Schema(example = "{\"message\": \"No hay sucursales registradas.\"}"))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Sucursal>>> getAllSucursales() {
        List<Sucursal> sucursales = sucursalService.findAllSucursal();

        if (sucursales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<EntityModel<Sucursal>> sucursalesModel = sucursales.stream()
                .map(sucursalModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Sucursal>> collectionModel = CollectionModel.of(
                sucursalesModel,
                linkTo(methodOn(SucursalControllerV2.class).getAllSucursales()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene una sucursal con enlaces HATEOAS",
            description = "Devuelve los detalles de una sucursal con enlaces auto descriptivos (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursal encontrada",
                    content = @Content(schema = @Schema(implementation = Sucursal.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(schema = @Schema(example = "{\"message\": \"Sucursal no encontrada con ID: 1\"}"))
            )
    })
    @Parameter(name = "id", description = "ID de la sucursal", required = true, example = "1")
    public ResponseEntity<EntityModel<Sucursal>> getSucursalFindById(@PathVariable Long id) {
        Sucursal sucursal = sucursalService.findByIdSucursal(id);
        EntityModel<Sucursal> resource = sucursalModelAssembler.toModel(sucursal);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(
            summary = "Obtiene una sucursal por nombre con enlaces HATEOAS",
            description = "Recupera los detalles de una sucursal específica utilizando su nombre, incluyendo enlaces auto descriptivos (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucursal encontrada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"idSucursal\": 1, \"nombre\": \"Sucursal Central\", \"direccion\": \"Av. Principal 123\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal no encontrada con nombre: Sucursal Central\"}")
                    )
            )
    })
    public ResponseEntity<EntityModel<Sucursal>> getSucursalByNombreSucursal(
            @Parameter(description = "Nombre de la sucursal a buscar", required = true, example = "Sucursal Central")
            @PathVariable String nombre) {

        Sucursal sucursal = sucursalService.findByNombreSucursal(nombre);

        EntityModel<Sucursal> resource = sucursalModelAssembler.toModel(sucursal);

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    @Operation(
            summary = "Crea una nueva sucursal con enlaces HATEOAS",
            description = "Registra una nueva sucursal en el sistema y devuelve sus detalles con enlaces auto descriptivos (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sucursal creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"idSucursal\": 3, \"nombre\": \"Sucursal Este\", \"direccion\": \"Calle Inventada 789\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida o datos duplicados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: El nombre de la sucursal no puede estar vacío\"}")
                    )
            )
    })
    public ResponseEntity<EntityModel<Sucursal>> saveSucursal(
            @Parameter(
                    description = "Datos de la sucursal a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sucursal.class),
                            examples = @ExampleObject(value = "{\"nombre\": \"Sucursal Este\", \"direccion\": \"Calle Inventada 789\"}")
                    )
            )
            @Validated @RequestBody Sucursal sucursal) {

        Sucursal nuevaSucursal = sucursalService.save(sucursal);
        EntityModel<Sucursal> resource = sucursalModelAssembler.toModel(nuevaSucursal);

        return ResponseEntity
                .created(linkTo(methodOn(SucursalControllerV2.class).getSucursalFindById(nuevaSucursal.getIdSucursal())).toUri())
                .body(resource);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza una sucursal existente con enlaces HATEOAS",
            description = "Actualiza los detalles de una sucursal específica por su ID y devuelve los datos actualizados con enlaces auto descriptivos (HATEOAS)"
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
    public ResponseEntity<EntityModel<Sucursal>> updateSucursal(
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
        try {
            Sucursal sucursalActualizada = sucursalService.updateByIdSucursal(id, sucursal);
            EntityModel<Sucursal> resource = sucursalModelAssembler.toModel(sucursalActualizada);
            return ResponseEntity.ok(resource);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // o retornar mensaje en JSON si usas ControllerAdvice
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una sucursal por ID",
            description = "Elimina una sucursal del sistema utilizando su ID y retorna un mensaje estructurado en formato JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Sucursal eliminada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal eliminada exitosamente\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sucursal no encontrada para eliminar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Sucursal no encontrada con ID: 1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Servicio dependiente no disponible",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No se pudo contactar al servicio de inventario. Inténtelo más tarde.\"}")
                    )
            )
    })
    public ResponseEntity<Map<String, String>> deleteSucursal(
            @Parameter(description = "ID de la sucursal a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            sucursalService.deleteByIdSucursal(id);
            Map<String, String> response = Map.of("message", "Sucursal eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            Map<String, String> error = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        // NO captures aquí feign.RetryableException directamente: se hace globalmente ↓
    }}
