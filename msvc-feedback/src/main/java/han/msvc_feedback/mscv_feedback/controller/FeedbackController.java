package han.msvc_feedback.mscv_feedback.controller;

import han.msvc_feedback.mscv_feedback.dto.FeedbackDTO;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.service.FeedbackService;
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
@RequestMapping("/api/v1/feedback")
@Validated
@Tag(name = "Feedback Controller", description = "Gestiona las operaciones relacionadas con el feedback de los usuarios sobre productos.")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    //CREATE
    @Operation(summary = "Crear un nuevo feedback", description = "Permite a un usuario enviar un nuevo feedback (comentario o calificación) para un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 5, \"comentario\": \"Excelente producto, muy recomendado!\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: La calificación debe estar entre 1 y 5\"}")))
    })
    @PostMapping()
    public ResponseEntity<FeedbackResponseDTO> createFeedback(
            @Parameter(description = "Objeto Feedback a crear. Requiere idUsuario, idProducto, calificacion y comentario (opcional)", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Feedback.class),
                            examples = @ExampleObject(value = "{\"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 5, \"comentario\": \"Excelente producto!\"}")))
            @Validated @RequestBody Feedback feedback) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.createFeedback(feedback));
    }

    //DELETE
    @Operation(summary = "Eliminar un feedback por ID", description = "Elimina un feedback específico del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback eliminado exitosamente",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Se ha eliminado el feedback correctamente."))),
            @ApiResponse(responseCode = "404", description = "Feedback no encontrado",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Feedback no encontrado con ID: 1")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByIdFeedback(
            @Parameter(description = "ID del feedback a eliminar", required = true) @PathVariable Long id) {
        try {
            feedbackService.deleteByIdFeedback(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se ha eliminado el feedback correctamente.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    //UPDATE
    @Operation(summary = "Actualizar un feedback existente", description = "Actualiza el comentario o la calificación de un feedback específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 4, \"comentario\": \"Buen producto, pero podría mejorar.\" }"))),
            @ApiResponse(responseCode = "404", description = "Feedback no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Feedback no encontrado con ID: 1\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: La calificación debe estar entre 1 y 5\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateByIdFeedback(
            @Parameter(description = "ID del feedback a actualizar", required = true) @PathVariable Long id,
            @Parameter(description = "Objeto FeedbackDTO con los datos actualizados (calificacion y/o comentario)", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDTO.class),
                            examples = @ExampleObject(value = "{\"calificacion\": 4, \"comentario\": \"Buen producto, pero podría mejorar.\" }")))
            @RequestBody FeedbackDTO feedbackDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.updateByIdFeedback(id, feedbackDTO));
    }

    //FIND BY ID
    @Operation(summary = "Obtener un feedback por ID", description = "Recupera los detalles de un feedback específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feedback encontrado exitosamente", // Changed from 201 to 200 for GET operations
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Feedback.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 5, \"comentario\": \"Excelente producto, muy recomendado!\"}"))),
            @ApiResponse(responseCode = "404", description = "Feedback no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Feedback no encontrado con ID: 1\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> findByIdFeedback(
            @Parameter(description = "ID del feedback a buscar", required = true) @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK) // Changed from CREATED to OK for GET operations
                .body(feedbackService.findByIdFeedback(id));
    }

    //FIND ALL PRODUCT como mensiono el profe
    @Operation(summary = "Obtener todos los feedback de un producto", description = "Recupera una lista de todos los feedback asociados a un producto específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de feedback del producto recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 5, \"comentario\": \"Excelente producto!\"}, {\"id\": 2, \"idUsuario\": 102, \"idProducto\": 201, \"calificacion\": 4, \"comentario\": \"Buen producto.\"}]")))
    })
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<FeedbackResponseDTO>> findByAllFeedbackProduct(
            @Parameter(description = "ID del producto para el cual buscar feedback", required = true) @PathVariable Long idProducto){
        return ResponseEntity
                .status(HttpStatus.OK) // Using HttpStatus.OK for clarity
                .body(feedbackService.findByAllFeedbackProduct(idProducto));
    }

    //FIND ALL
    @Operation(summary = "Obtener todos los feedback", description = "Recupera una lista de todos los feedback registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de feedback recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"idUsuario\": 101, \"idProducto\": 201, \"calificacion\": 5, \"comentario\": \"Excelente producto!\"}, {\"id\": 2, \"idUsuario\": 102, \"idProducto\": 202, \"calificacion\": 4, \"comentario\": \"Buen producto.\"}]")))
    })
    @GetMapping()
    public ResponseEntity<List<FeedbackResponseDTO>> findAllFeedback() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(feedbackService.findAllFeedback());
    }
}
