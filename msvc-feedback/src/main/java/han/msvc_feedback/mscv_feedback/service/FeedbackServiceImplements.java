package han.msvc_feedback.mscv_feedback.service;

import feign.FeignException;
import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.client.UsuarioClientRest;
import han.msvc_feedback.mscv_feedback.exception.FeedbackException;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImplements implements FeedbackService{

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ProductClientRest productClientRest;

    @Autowired
    private UsuarioClientRest usuarioClientRest;

    public Feedback save(Feedback feedback) {
        try {productClientRest.findByIdProducto(feedback.getProductIdFeedback());
        }catch (FeignException ex) {
            throw new FeedbackException("El producto con la id "+feedback.getProductIdFeedback()+" no fue encontrado.");
        }
        try {usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());

        }catch (FeignException ex) {
            throw new FeedbackException("El usuario con la id "+feedback.getUsuarioIdFeedback()+" no fue encontrado.");
        }
        return feedbackRepository.save(feedback);
    }

    public Feedback findById(Long id) {
        return feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackException("El feedback con ese id no existe."));
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public void deleteById(Long id) {
        if(feedbackRepository.findById(id).isPresent()) {
            feedbackRepository.deleteById(id);
        }else {
            throw new FeedbackException("El feedback con la id "+id+" no existe.");
        }
    }
}
