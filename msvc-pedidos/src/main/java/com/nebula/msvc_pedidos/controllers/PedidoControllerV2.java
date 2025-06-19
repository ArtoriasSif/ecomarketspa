package com.nebula.msvc_pedidos.controllers;

import com.nebula.msvc_pedidos.assemblers.PedidoConDetalleModelAssembler;
import com.nebula.msvc_pedidos.assemblers.PedidoModelAssembler;
import com.nebula.msvc_pedidos.clients.SucursalClientRest;
import com.nebula.msvc_pedidos.dtos.ErrorDTO;
import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.Sucursal;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.services.PedidoService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pedido")
@Validated
@Tag(name = "Pedidos V2", description = "Operaciones CRUD de pedidos hateoas")
public class PedidoControllerV2 {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private SucursalClientRest sucursalClientRest;

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    private PedidoConDetalleModelAssembler pedidoConDetalleModelAssembler;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un pedido (V2)",
            description = "Devuelve un pedido en formato HATEOAS a partir del ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado con el ID suministrado",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(name = "id", description = "ID único del pedido", required = true)
    })
    public ResponseEntity<EntityModel<Pedido>> findById(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        EntityModel<Pedido> recurso = pedidoModelAssembler.toModel(pedido);
        return ResponseEntity.ok(recurso);
    }


    @GetMapping("/detalle/{id}")
    @Operation(
            summary = "Obtiene un pedido con sus detalles (V2)",
            description = "Devuelve un pedido junto con los detalles de productos, usuario y sucursal relacionados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado con sus detalles",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = PedidoConDetalleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado con el ID proporcionado",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(name="id", description = "Este es el id unico de pedido", required = true)
    })
    public ResponseEntity<EntityModel<PedidoConDetalleDTO>> findByIdPedido(@PathVariable Long id) {
        try {
            PedidoConDetalleDTO detalles = pedidoService.findPedidoConDetalles(id);
            EntityModel<PedidoConDetalleDTO> recurso = pedidoConDetalleModelAssembler.toModel(detalles);
            return ResponseEntity.ok(recurso);
        } catch (PedidoException e) {
            return ResponseEntity.status(404).build();
        }
    }


    @GetMapping
    @Operation(
            summary = "Lista todos los pedidos",
            description = "Devuelve una lista completa de todos los pedidos registrados en el sistema con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos obtenida correctamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> findAll() {
        List<Pedido> pedidos = pedidoService.findAll();

        List<EntityModel<Pedido>> pedidosModel = pedidos.stream()
                .map(pedidoModelAssembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(pedidosModel,
                        linkTo(methodOn(PedidoControllerV2.class).findAll()).withSelfRel())
        );
    }

    @GetMapping("/detalle")
    @Operation(
            summary = "Lista todos los pedidos con sus detalles",
            description = "Devuelve una lista de todos los pedidos, incluyendo los productos, cantidades, precios y totales asociados a cada uno"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos con detalles obtenida correctamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PedidoConDetalleDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen pedidos con detalles registrados",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<PedidoConDetalleDTO>>> findAllPedidosConDetalle() {
        List<PedidoConDetalleDTO> pedidos = pedidoService.findAllPedidoConDetalle();

        List<EntityModel<PedidoConDetalleDTO>> pedidosModel = pedidos.stream()
                .map(pedidoConDetalleModelAssembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(pedidosModel,
                        linkTo(methodOn(PedidoControllerV2.class).findAllPedidosConDetalle()).withSelfRel())
        );
    }

    @PostMapping
    @Operation(
            summary = "Crea la cabecera de un pedido",
            description = "Registra un nuevo pedido en base a un usuario y una sucursal válidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado exitosamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en la validación del usuario o la sucursal",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "pedido a crear",
            content = @Content(
                    mediaType = MediaTypes.HAL_JSON_VALUE,
                    schema = @Schema(implementation = Pedido.class)
            )
    )
    public ResponseEntity<PedidoResponseDTO> save(@RequestBody PedidoDTO pedidoDTO) {
        try {
            PedidoResponseDTO response = pedidoService.save(pedidoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PedidoException e) {
            return ResponseEntity.badRequest().body(new PedidoResponseDTO(null, null, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un pedido",
            description = "Actualiza la cabecera del pedido con un nuevo usuario y/o sucursal si los datos son distintos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido actualizado correctamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado con el ID indicado",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se detectaron cambios en el pedido",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<?> updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido actualizado = pedidoService.updatePedido(id, pedido);
            return ResponseEntity.ok(actualizado);
        } catch (PedidoException e) {
            return ResponseEntity.status(
                    e.getMessage().contains("no encontrado") ? 404 : 400
            ).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un pedido y sus detalles",
            description = "Elimina completamente un pedido de la base de datos junto con todos los detalles asociados al pedido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido y sus detalles eliminados correctamente",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el pedido con el ID proporcionado",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {//PUEDE SER QUE TENGA QUE ADD EN CONTROLLER V1
        String resultado = pedidoService.deletePedidoId(id);

        if ("Pedido eliminado".equals(resultado)) {
            return ResponseEntity.noContent().build();  // 204 No Content, sin cuerpo
        } else {
            Map<String, Object> error = Map.of(
                    "status", 404,
                    "message", resultado,
                    "timestamp", LocalDateTime.now().toString()
            );
            return ResponseEntity.status(404).body(error);
        }
    }

    @DeleteMapping("/sucursal/{idSucursal}")
    @Operation(
            summary = "Elimina todos los pedidos de una sucursal",
            description = "Elimina todos los pedidos junto con sus detalles relacionados a una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos y detalles eliminados exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la sucursal o no hay pedidos asociados",
                    content = @Content(
                            mediaType = MediaTypes.HAL_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<String> deletePedidoSucursal(@PathVariable Long idSucursal) {//PUEDE SER QUE TENGA QUE ADD EN CONTROLLER V1
        try {
            // Validar que la sucursal exista, llamando al servicio que usa la clase Sucursal (POJO)
            Sucursal sucursal = sucursalClientRest.findByIdSucursal(idSucursal);
            if (sucursal == null) {
                return ResponseEntity.status(404).body("No se encontró la sucursal con id: " + idSucursal);
            }

            // Si existe, proceder a eliminar pedidos y detalles
            pedidoService.deletarPedidosConDetalles(idSucursal);

            return ResponseEntity.ok("Pedidos y detalles de la sucursal " + idSucursal + " eliminados exitosamente");
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.status(404).body("Sucursal no encontrada con ID: " + idSucursal);
        } catch (PedidoException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno: " + ex.getMessage());
        }

    }



}






