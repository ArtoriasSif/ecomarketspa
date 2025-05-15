package han.msvc_feedback.mscv_feedback.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usuario {

    private Long usuarioId;

    private String nombreUsuario;

    private String contraUsuario;

    private String nombreDelUsuario;

    private String correoUsuario;

    private String rutUsuario;

    private String direccionUsuario;

    private String telefonoUsuario;
}
