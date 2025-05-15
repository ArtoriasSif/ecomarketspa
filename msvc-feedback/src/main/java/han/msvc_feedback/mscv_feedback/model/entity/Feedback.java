package han.msvc_feedback.mscv_feedback.model.entity;

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
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id",nullable = false)
    private Long feedbackId;

    @Column(name="feedback_fecha",nullable = false)
    private LocalDateTime dateFeedback;

    @Column(name="feedback_texto",nullable = false)
    @NotBlank(message = "El campo feedback no puede estar vacio.")
    private String textoFeedback;

    @Column(name="feedback_usuario",nullable = false)
    @NotNull(message = "El campo usuario no puede estar vacio.")
    private Long usuarioIdFeedback;

    @Column(name="feedback_product",nullable = false)
    @NotNull(message = "El campo producto no puede estar vacio.")
    private Long productIdFeedback;

    @PrePersist
    public void prePersist() {
        this.dateFeedback = LocalDateTime.now();
    }
}
