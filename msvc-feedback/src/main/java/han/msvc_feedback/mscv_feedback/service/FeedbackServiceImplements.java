package han.msvc_feedback.mscv_feedback.service;

import feign.FeignException;
import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.client.UsuarioClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackDTO;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.exception.FeedbackException;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.Usuario;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    //CREATE
    @Transactional
    @Override
    public FeedbackResponseDTO createFeedback(Feedback feedback) {
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

        return new FeedbackResponseDTO(
                feedback.getDateFeedback(),
                feedback.getTextoFeedback(),
                usuario.getNombreDelUsuario(),
                product.getNombreProducto()
        );

    }

    //DELETE BY ID
    @Transactional
    @Override
    public void deleteByIdFeedback(Long id) {
        if(feedbackRepository.findById(id).isPresent()) {
            feedbackRepository.deleteById(id);
        }else {
            throw new FeedbackException("El feedback con la id "+id+" no existe.");
        }
    }

    //UPDATE BY ID
    @Transactional
    @Override
    public FeedbackResponseDTO updateByIdFeedback(Long id, FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackException("El feedback con la id "+id+" no existe.")
        );
        try {productClientRest.findByIdProducto(feedback.getProductIdFeedback());
        }catch (FeignException ex) {
            throw new FeedbackException("El producto con la id "+feedback.getProductIdFeedback()+" no fue encontrado.");
        }

        try {usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());
        }catch (FeignException ex) {
            throw new FeedbackException("El usuario con la id "+feedback.getUsuarioIdFeedback()+" no fue encontrado.");
        }
        feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackException("El feedback con la id "+id+" no existe.")
        );
        Usuario usuario = usuarioClientRest.findByIdUsuario(feedback.getUsuarioIdFeedback());
        Product producto = productClientRest.findByIdProducto(feedback.getProductIdFeedback());
        feedback.setDateFeedback(LocalDateTime.now());
        feedback.setTextoFeedback(feedbackDTO.getTextoFeedback());
        feedbackRepository.save(feedback);

        return new FeedbackResponseDTO(
                feedback.getDateFeedback(),
                feedback.getTextoFeedback(),
                usuario.getNombreDelUsuario(),
                producto.getNombreProducto()
        );
    }

    //FIND BY ID
    @Transactional
    @Override
    public Feedback findByIdFeedback(Long id) {
        return feedbackRepository.findById(id).orElseThrow(
                () -> new FeedbackException("El feedback con ese id no existe."));
    }

    //FIND ALL
    @Transactional
    @Override
    public List<FeedbackResponseDTO> findAllFeedback() {
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
}
