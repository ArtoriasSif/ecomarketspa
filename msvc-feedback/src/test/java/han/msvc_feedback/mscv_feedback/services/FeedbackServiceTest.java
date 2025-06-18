package han.msvc_feedback.mscv_feedback.services;

import han.msvc_feedback.mscv_feedback.client.ProductClientRest;
import han.msvc_feedback.mscv_feedback.client.UsuarioClientRest;
import han.msvc_feedback.mscv_feedback.dto.FeedbackResponseDTO;
import han.msvc_feedback.mscv_feedback.model.Product;
import han.msvc_feedback.mscv_feedback.model.Usuario;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import han.msvc_feedback.mscv_feedback.service.FeedbackServiceImplements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

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
    private FeedbackServiceImplements feedbackServiceImplements;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFeedback() {
        // Arrange
        Long productId = 10L;
        Long usuarioId = 20L;

        Feedback feedback = new Feedback();
        feedback.setDateFeedback(LocalDateTime.now());
        feedback.setTextoFeedback("Producto excelente.");
        feedback.setProductIdFeedback(productId);
        feedback.setUsuarioIdFeedback(usuarioId);

        Product mockProduct = new Product();
        mockProduct.setIdProducto(productId);
        mockProduct.setNombreProducto("Producto Prueba");

        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(usuarioId);
        mockUsuario.setNombreDelUsuario("Juan Pérez");

        when(productClientRest.findByIdProducto(productId)).thenReturn(mockProduct);
        when(usuarioClientRest.findByIdUsuario(usuarioId)).thenReturn(mockUsuario);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponseDTO response = feedbackServiceImplements.createFeedback(feedback);

        // Assert
        assertNotNull(response);
        assertEquals("Producto excelente.", response.getTextoFeedback());
        assertEquals("Juan Pérez", response.getNombreUsuario());
        assertEquals("Producto Prueba", response.getNombreProducto());

        // Verificar que se llamaron los métodos esperados
        verify(productClientRest).findByIdProducto(productId);
        verify(usuarioClientRest).findByIdUsuario(usuarioId);
        verify(feedbackRepository).save(any(Feedback.class));
    }
}
