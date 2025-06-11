package com.cmunoz.msvc.sucursal.controller;


import com.cmunoz.msvc.sucursal.dto.SucursalDTO;
import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.services.SucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
public class SucursalController {

    //Falta agregar PUT
    @Autowired
    private SucursalService sucursalService;



    @GetMapping
    @Operation(summary = "Obtiene todas las sucursales", description = "Devuelve un list de sucursales en el body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class)
                    )
            )
    })
    public ResponseEntity<List<Sucursal>> getAllSucursales() {
        return ResponseEntity
                .ok()
                .body(sucursalService.findAllSucursal());
    }


    @GetMapping("/id/{id}")
    @Operation(summary = "Obtiene una sucursal buscando por su ID", description = "Devuelve un body con una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SucursalDTO.class)
                            )
            )

    })
    @Parameters(value = {
            @Parameter(name = "id_sucursal", description = "ID de Sucursal")
    })
    public ResponseEntity<Sucursal> getSucursalFindById(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.findByIdSucursal(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtine una sucursal buscando por su nombre", description = "Devuelve un body con una sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SucursalDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(name = "nombre_sucursal", description = "Nombre de Sucursal")
    })
    public ResponseEntity<Sucursal> getSucursalByNombreSucursal( @PathVariable String nombre) {
        return ResponseEntity
                .status(200)
                .body(sucursalService.findByNombreSucursal(nombre));
    }

    @PostMapping()
    @Operation(summary = "Crea una nueva sucursal", description = "Crea una nueva sucursal recibiendo un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "405", description = "Sucursal no creada, ID ya usada"
                    )
    })
    @Parameters(value = {
            @Parameter(name = "id_sucursal", description = "ID de Sucursal")
    })
    public ResponseEntity<String> saveSucursal(@Validated @RequestBody Sucursal sucursal) {
        return ResponseEntity
                .status(201)
                .body(sucursalService.save(sucursal));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sucursal", description = "Actualiza una sucursal recibiendo un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no actulizada, Id no encontrada")
    })
    @Parameters(value = {
            @Parameter(name = "id_sucursal", description = "ID de Sucursal")
    })
    public ResponseEntity<String> updateSucursal(@PathVariable Long id, @Validated @RequestBody Sucursal sucursal) {
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalService.updateByIdSucursal(id, sucursal));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PutMapping("/{id}")
    @Operation(summary = "Borrar una sucursal", description = "Borrar una sucursal desde su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion Exitorsa"),
            @ApiResponse(responseCode = "404", description = "Sucursal no Borrada, Id no encontrada")
    })
    @Parameters(value = {
            @Parameter(name = "id_sucursal", description = "ID de Sucursal")
    })
    public ResponseEntity<String> deleteSucursal(@PathVariable Long id) {
        try{

            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalService.deleteByIdSucursal(id));

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
