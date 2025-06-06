package han.msvc_feedback.mscv_feedback.service;

import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackDTO;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface FeedbackService {

    FeedbackResponseDTO createFeedback(Feedback feedback);
    Feedback findByIdFeedback(Long id);
    List<FeedbackResponseDTO> findAllFeedback();
    void deleteByIdFeedback(Long id);
    FeedbackResponseDTO updateByIdFeedback(Long id, FeedbackDTO feedbackDTO);
    List<FeedbackResponseDTO> findByAllFeedbackProduct (Long idProducto);
}
