package han.msvc_feedback.mscv_feedback.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackAssemblerDTO extends RepresentationModel<FeedbackAssemblerDTO> {

    @JsonIgnore
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private int calificacion;
    private String comentario;

}
