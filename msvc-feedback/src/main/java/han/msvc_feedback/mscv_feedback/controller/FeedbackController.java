package han.msvc_feedback.mscv_feedback.controller;

import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.service.FeedbackService;
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
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping()
    public ResponseEntity<Feedback> save(@Validated @RequestBody Feedback feedback) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.save(feedback));
    }

    @GetMapping()
    public ResponseEntity<List<Feedback>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(feedbackService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedbackService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try{feedbackService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Se ha eliminado el feedback correctamente");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    }
}
