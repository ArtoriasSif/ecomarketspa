package com.nebula.msvc_usuarios.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v2/usuarios")
@Validated
@Tag(name = "Usuario Controller V2", description = "Gestiona las operaciones relacionadas con los usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener un usuario por ID", description = "Permite buscar y recuperar los detalles de un usuario usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Juan\", \"email\": \"juan@example.com\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@Parameter(description = "ID del usuario a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.findById(id));
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"nombre\": \"Juan\", \"email\": \"juan@example.com\"}, {\"id\": 2, \"nombre\": \"Maria\", \"email\": \"maria@example.com\"}]")))
    })
    @GetMapping()
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"id\": 3, \"nombre\": \"Pedro\", \"email\": \"pedro@example.com\",\"direccionUsuario\": \"1111 av. xdent\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el email no puede estar vacío\"}")))
    })
    @PostMapping()
    public ResponseEntity<Usuario> createUsuario(@Parameter(description = "Datos del usuario a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Usuario.class),
                    examples = @ExampleObject(value = "{\"nombre\": \"Pedro\", \"email\": \"pedro@example.com\"}")))
                                                 @Validated @RequestBody Usuario usuario) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.save(usuario));
    }


    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los detalles de un usuario existente usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Juan Actualizado\", \"email\": \"juan.updated@example.com\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Usuario no encontrado con ID: 1\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el nombre no puede estar vacío\"}"))),
            @ApiResponse(responseCode = "400", description = "Error de validación de campos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"errors\": {\"telefonoUsuario\": \"El formato del telefono debe ser +56 9XXXXXXXX\"}}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class), // Optional: If you have a generic error response DTO
                            examples = @ExampleObject(value = "{\"timestamp\": \"2025-06-20T14:30:00Z\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Ha ocurrido un error inesperado en el servidor.\", \"path\": \"/api/v1/your-endpoint\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Long id,
                                                 @Parameter(description = "Datos del usuario a actualizar", required = true,
                                                         content = @Content(mediaType = "application/json",
                                                                 schema = @Schema(implementation = UsuarioUpdateDTO.class),
                                                                 examples = @ExampleObject(value = "{\"nombre\": \"Juan Actualizado\", \"email\": \"juan.updated@example.com\"}")))
                                                 @Validated @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.updateUsuario(id, usuarioUpdateDTO));
    }


    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Usuario eliminado exitosamente"))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Usuario no encontrado con ID: 1")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuario eliminado exitosamente");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
