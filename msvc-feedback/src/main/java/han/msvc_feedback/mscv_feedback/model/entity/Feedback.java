package han.msvc_feedback.mscv_feedback.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name ="feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Entidad que representa un Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id",nullable = false)
    private Long feedbackId;

    @Column(name="feedback_fecha",nullable = false)
    private LocalDateTime dateFeedback;

    @Column(name="feedback_texto",nullable = false)
    @NotBlank(message = "El campo feedback no puede estar vacio.")
    @Schema(description = "Texto del feedback", example = "Me gusta el producto")
    private String textoFeedback;

    @Column(name="feedback_usuario",nullable = false)
    @NotNull(message = "El campo usuario no puede estar vacio.")
    @Schema(description = "Id del usuario que realiza el feedback", example = "1")

    private Long usuarioIdFeedback;

    @Column(name="feedback_product",nullable = false)
    @NotNull(message = "El campo producto no puede estar vacio.")
    @Schema(description = "Id del producto que se le realiza el feedback", example = "1")
    private Long productIdFeedback;

    @PrePersist
    public void prePersist() {
        this.dateFeedback = LocalDateTime.now();
    }

    public Feedback(String dateFeedback, String textoFeedback, Long usuarioIdFeedback, Long productIdFeedback) {
        this.dateFeedback = LocalDateTime.parse(dateFeedback);
        this.textoFeedback = textoFeedback;
        this.usuarioIdFeedback = usuarioIdFeedback;
        this.productIdFeedback = productIdFeedback;
    }
}
