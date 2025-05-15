package han.msvc_feedback.mscv_feedback.service;

import feign.FeignException;
import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.client.UsuarioClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.exception.FeedbackException;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.Usuario;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImplements implements FeedbackService{

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ProductClientRest productClientRest;

    @Autowired
    private UsuarioClientRest usuarioClientRest;

    public FeedbackResponseDTO save(Feedback feedback) {
        try {productClientRest.findByIdProducto(feedback.getProductIdFeedback());
        }catch (FeignException ex) {
            throw new FeedbackException("El producto con la id "+feedback.getProductIdFeedback()+" no fue encontrado.");
        }
        try {usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());

        }catch (FeignException ex) {
            throw new FeedbackException("El usuario con la id "+feedback.getUsuarioIdFeedback()+" no fue encontrado.");
        }
        feedbackRepository.save(feedback);
        Usuario usuario = usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());
        Product product = productClientRest.findByIdProducto(feedback.getProductIdFeedback());
        FeedbackResponseDTO feedbackResponseDTO = new FeedbackResponseDTO(
                feedback.getDateFeedback(),
                feedback.getTextoFeedback(),
                usuario.getNombreDelUsuario(),
                product.getNombreProducto()
        );
        return feedbackResponseDTO;
    }

    public Feedback findById(Long id) {
        return feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackException("El feedback con ese id no existe."));
    }

    public List<FeedbackResponseDTO> findAll() {
        List<Feedback> feedbacks = feedbackRepository.findAll();

        List<FeedbackResponseDTO> listaResponseDTO = feedbacks.stream().map(feedback -> {
            Usuario usuario = usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());
            Product producto = productClientRest.findByIdProducto(feedback.getProductIdFeedback());

            return new FeedbackResponseDTO(
                    feedback.getDateFeedback(),
                    feedback.getTextoFeedback(),
                    usuario.getNombreDelUsuario(),
                    producto.getNombreProducto()
            );
        }).collect(Collectors.toList());
        return listaResponseDTO;
    }

    public void deleteById(Long id) {
        if(feedbackRepository.findById(id).isPresent()) {
            feedbackRepository.deleteById(id);
        }else {
            throw new FeedbackException("El feedback con la id "+id+" no existe.");
        }
    }
}
