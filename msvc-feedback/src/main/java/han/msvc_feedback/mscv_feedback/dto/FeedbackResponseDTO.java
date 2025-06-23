package han.msvc_feedback.mscv_feedback.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class FeedbackResponseDTO {
    private LocalDateTime fechaFeedback;
    private String textoFeedback;
    private String nombreUsuario;
    private String nombreProducto;
}
