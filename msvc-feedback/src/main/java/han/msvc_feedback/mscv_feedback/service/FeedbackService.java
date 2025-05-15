package han.msvc_feedback.mscv_feedback.service;

import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface FeedbackService {

    FeedbackResponseDTO save(Feedback feedback);
    Feedback findById(Long id);
    List<FeedbackResponseDTO> findAll();
    void deleteById(Long id);
}
