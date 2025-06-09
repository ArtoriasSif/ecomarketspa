package han.msvc_feedback.mscv_feedback.controller;

import han.msvc_feedback.mscv_feedback.dto.FeedbackDTO;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1/feedback")
@Validated
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    //CREATE
    @PostMapping()
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@Validated @RequestBody Feedback feedback) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.createFeedback(feedback));
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByIdFeedback(@PathVariable Long id) {
        try{feedbackService.deleteByIdFeedback(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se ha eliminado el feedback correctamente.");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
        }
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateByIdFeedback(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.updateByIdFeedback(id, feedbackDTO));
    }

    //FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> findByIdFeedback(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.findByIdFeedback(id));
    }

    //FIND ALL PRODUCT como mensiono el profe
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<FeedbackResponseDTO>> findByAllFeedbackProduct (@PathVariable Long idProducto){
        return ResponseEntity
                .status(200)
                .body(feedbackService.findByAllFeedbackProduct(idProducto));
    }

    //FIND ALL
    @GetMapping()
    public ResponseEntity<List<FeedbackResponseDTO>> findAllFeedback() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(feedbackService.findAllFeedback());
    }
}
