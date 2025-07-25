package com.nebula.msvc_pedidos.controllers;

import com.nebula.msvc_pedidos.clients.DetallePedidoClientRest;
import com.nebula.msvc_pedidos.dtos.ErrorDTO;
import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.services.PedidoService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedido")
@Validated
@Tag(name = "Pedidos", description = "Operaciones CRUD de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallePedidoClientRest detallePedidoClientRest;


    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un pedido", description = "A través del id suministrado devuelve el pedido con esa id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Operacion existosa"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado, con el id suministrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    @Parameters(value = {
            @Parameter(name="id", description = "Este es el id unico del pedido", required = true)
    })
    public ResponseEntity<Pedido> findById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(pedidoService.findById(id));
    }

    //Listar pedido con los detalles de productos
    @GetMapping("/detalle/{id}")
    @Operation(
            summary = "Obtiene un pedido con sus detalles",
            description = "Devuelve un pedido junto con los detalles de productos, usuario y sucursal relacionados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado con sus detalles",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoConDetalleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado con el ID proporcionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<?> findByIdPedido(@PathVariable Long id) {
        try {
            PedidoConDetalleDTO detalles = pedidoService.findPedidoConDetalles(id);
            return ResponseEntity.ok(detalles);
        } catch (PedidoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(
            summary = "Lista todos los pedidos",
            description = "Devuelve una lista completa de todos los pedidos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )
            )
    })
    public ResponseEntity<List<Pedido>> findAll() {
        return ResponseEntity.status(200).body(pedidoService.findAll());
    }

    //Listar todos los pedidos con detalles
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
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoConDetalleDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen pedidos con detalles registrados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity <List<PedidoConDetalleDTO>> findAllPedidos() {
        return ResponseEntity.ok(pedidoService.findAllPedidoConDetalle());
    }

    //Crear la cabecera del pedido
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en la validación del usuario o la sucursal",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<PedidoResponseDTO> save(@RequestBody PedidoDTO pedidoDTO) {
        PedidoResponseDTO response = pedidoService.save(pedidoDTO);
        return ResponseEntity.status(201).body(response);
    }

    //Actualizar Cabecera id usuario y sucursal
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado con el ID indicado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se detectaron cambios en el pedido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return ResponseEntity.status(200).body(pedidoService.updatePedido(id, pedido));
    }

    //Determinado como ? dado que en Usuario se ucupara un metodo VOID
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el pedido con el ID proporcionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try{
            String mensaje = pedidoService.deletePedidoId(id);
            return ResponseEntity.status(200).body(mensaje);
        }catch (Exception ex){
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    //Deletar pedidos de la sucursal utilizado en cLient delete sucursal
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
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)
                    )
            )
    })
    public void deletePedidoSucursal(@PathVariable Long idSucursal) {
        pedidoService.deletePedidoId(idSucursal);
    }



}
