package com.nebula.msvc_usuarios.controller;

import com.nebula.msvc_usuarios.assemblers.UsuarioModelAssembler;
import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/v2/usuarios")
@Validated
@Tag(name = "Usuario Controller V2", description = "Gestiona las operaciones relacionadas con los usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un usuario por ID con enlaces HATEOAS",
            description = "Permite buscar y recuperar los detalles de un usuario usando su ID, incluyendo enlaces auto descriptivos (HATEOAS)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class), // La implementación real será EntityModel<Usuario> pero esto es para la doc.
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Juan\", \"email\": \"juan@example.com\", \"_links\": {\"self\": {\"href\": \"http://localhost:8080/usuarios/1\"}}}"))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}")))
    })
    @Parameter(name = "id", description = "ID del usuario a buscar", required = true, example = "1")
    public ResponseEntity<EntityModel<Usuario>> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id); // Asumo que findById lanza una excepción si no encuentra el usuario, o devuelve null y la manejas aquí.

        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuario);
        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Recupera una lista de todos los usuarios registrados con enlaces auto descriptivos (HATEOAS)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"idUsuario\": 1, \"nombre\": \"Juan\", \"email\": \"juan@example.com\"}, {\"idUsuario\": 2, \"nombre\": \"Maria\", \"email\": \"maria@example.com\"}]")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron usuarios",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No hay usuarios registrados.\"}")
                    )
            )
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> findAll() {
        List<Usuario> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<EntityModel<Usuario>> usuarioResources = usuarios.stream()
                .map(usuarioModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(
                usuarioResources,
                linkTo(methodOn(UsuarioControllerV2.class).findAll()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema y retorna los datos con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"idUsuario\": 3, \"nombre\": \"Pedro\", \"email\": \"pedro@example.com\", \"direccionUsuario\": \"1111 av. xdent\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el email no puede estar vacío\"}")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> createUsuario(
            @Parameter(
                    description = "Datos del usuario a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"nombre\": \"Pedro\", \"email\": \"pedro@example.com\", \"direccionUsuario\": \"1111 av. xdent\"}")
                    )
            )
            @Validated @RequestBody Usuario usuario) {

        Usuario usuarioGuardado = usuarioService.save(usuario);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuarioGuardado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resource);
    }


    @Operation(
            summary = "Actualizar un usuario existente",
            description = "Modifica los detalles de un usuario existente usando su ID y retorna los datos actualizados con enlaces HATEOAS"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"idUsuario\": 1, \"nombre\": \"Juan Actualizado\", \"email\": \"juan.updated@example.com\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el nombre no puede estar vacío\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación de campos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"errors\": {\"telefonoUsuario\": \"El formato del telefono debe ser +56 9XXXXXXXX\"}}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-06-20T14:30:00Z\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Ha ocurrido un error inesperado en el servidor.\", \"path\": \"/api/v1/usuarios/1\"}")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(
                    description = "Datos del usuario a actualizar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateDTO.class),
                            examples = @ExampleObject(value = "{\"nombre\": \"Juan Actualizado\", \"email\": \"juan.updated@example.com\"}")
                    )
            )
            @Validated @RequestBody UsuarioUpdateDTO usuarioUpdateDTO
    ) {
        Usuario actualizado = usuarioService.updateUsuario(id, usuarioUpdateDTO);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(actualizado);

        return ResponseEntity.ok(resource);
    }


    @Operation(
            summary = "Eliminar un usuario por ID",
            description = "Elimina un usuario del sistema usando su ID y retorna un mensaje estructurado en formato JSON"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario eliminado exitosamente\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            Map<String, String> response = Map.of("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            Map<String, String> error = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
