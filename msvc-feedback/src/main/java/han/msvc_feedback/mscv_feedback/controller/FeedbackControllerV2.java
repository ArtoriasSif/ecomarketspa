package han.msvc_feedback.mscv_feedback.controller;

import han.msvc_feedback.mscv_feedback.assembler.FeedbackModelAssembler;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v2/feedback")
@Validated
@Tag(name = "Feedback ControllerV2", description = "Gestiona las operaciones relacionadas con el feedback de los usuarios sobre productos.")
public class FeedbackControllerV2 {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackModelAssembler assembler;


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Feedback>> findByIdFeedback(
            @Parameter(description = "ID del feedback a buscar", required = true) @PathVariable Long id) {

        Feedback feedback = feedbackService.findByIdFeedback(id);
        return ResponseEntity
                .ok(assembler.toModel(feedback));
    }


    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<Feedback>>> findAllFeedback() {
        List<Feedback> feedbacks = feedbackService.findAllFeedbackEntities();
        List<EntityModel<Feedback>> feedbackModels = feedbacks.stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(feedbackModels,
                        linkTo(methodOn(FeedbackControllerV2.class).findAllFeedback()).withSelfRel())
        );
    }


    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<CollectionModel<EntityModel<Feedback>>> findByAllFeedbackProduct(
            @Parameter(description = "ID del producto para el cual buscar feedback", required = true)
            @PathVariable Long idProducto) {

        List<Feedback> feedbacks = feedbackService.findAllFeedbackByProduct(idProducto);
        List<EntityModel<Feedback>> feedbackModels = feedbacks.stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(feedbackModels,
                        linkTo(methodOn(FeedbackControllerV2.class).findByAllFeedbackProduct(idProducto)).withSelfRel())
        );
    }

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

}
