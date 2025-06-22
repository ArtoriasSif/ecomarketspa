package han.msvc_feedback.mscv_feedback.services;

import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.client.UsuarioClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackDTO;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.exception.FeedbackException;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.Usuario;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import han.msvc_feedback.mscv_feedback.service.FeedbackServiceImplements;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ProductClientRest productClientRest;

    @Mock
    private UsuarioClientRest usuarioClientRest;

    @InjectMocks
    private FeedbackServiceImplements feedbackService;

    private Faker faker;
    private Feedback feedback;
    private Usuario usuario;
    private Product producto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker(new Locale("es", "CL"));

        feedback = new Feedback();
        feedback.setFeedbackId(1L);
        feedback.setUsuarioIdFeedback(1L);
        feedback.setProductIdFeedback(2L);
        feedback.setTextoFeedback(faker.lorem().sentence());
        feedback.setDateFeedback(LocalDateTime.now());

        usuario = new Usuario();
        usuario.setNombreDelUsuario(faker.name().fullName());

        producto = new Product();
        producto.setNombreProducto(faker.commerce().productName());
    }

    @Test
    public void testCreateFeedback() {
        when(productClientRest.findByIdProducto(anyLong())).thenReturn(producto);
        when(usuarioClientRest.findByIdUsuario(anyLong())).thenReturn(usuario);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackResponseDTO response = feedbackService.createFeedback(feedback);

        assertEquals(feedback.getTextoFeedback(), response.getTextoFeedback());
        assertEquals(usuario.getNombreDelUsuario(), response.getNombreUsuario());
        assertEquals(producto.getNombreProducto(), response.getNombreProducto());
    }

    @Test
    public void testFindByIdFeedback() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        Feedback result = feedbackService.findByIdFeedback(1L);

        assertNotNull(result);
        assertEquals(feedback.getFeedbackId(), result.getFeedbackId());
    }

    @Test
    public void testFindAllFeedback() {
        when(feedbackRepository.findAll()).thenReturn(List.of(feedback));
        when(usuarioClientRest.findByIdUsuario(anyLong())).thenReturn(usuario);
        when(productClientRest.findByIdProducto(anyLong())).thenReturn(producto);

        List<FeedbackResponseDTO> result = feedbackService.findAllFeedback();

        assertEquals(1, result.size());
        assertEquals(feedback.getTextoFeedback(), result.get(0).getTextoFeedback());
    }

    @Test
    public void testUpdateFeedback() {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setTextoFeedback("Texto actualizado");

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(usuarioClientRest.findByIdUsuario(anyLong())).thenReturn(usuario);
        when(productClientRest.findByIdProducto(anyLong())).thenReturn(producto);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackResponseDTO updated = feedbackService.updateByIdFeedback(1L, feedbackDTO);

        assertEquals("Texto actualizado", updated.getTextoFeedback());
    }

    @Test
    public void testDeleteFeedbackSuccess() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        assertDoesNotThrow(() -> feedbackService.deleteByIdFeedback(1L));

        verify(feedbackRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteFeedbackNotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FeedbackException.class, () -> feedbackService.deleteByIdFeedback(1L));
    }

    @Test
    public void testFindAllFeedbackByProduct() {
        when(feedbackRepository.findAllByProductIdFeedback(2L)).thenReturn(List.of(feedback));
        when(usuarioClientRest.findByIdUsuario(anyLong())).thenReturn(usuario);
        when(productClientRest.findByIdProducto(anyLong())).thenReturn(producto);

        List<FeedbackResponseDTO> result = feedbackService.findByAllFeedbackProduct(2L);

        assertEquals(1, result.size());
        assertEquals(feedback.getTextoFeedback(), result.get(0).getTextoFeedback());
    }
}
