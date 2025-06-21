package com.nebula.msvc_usuarios.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    @JsonIgnore
    private Long idUsuario;
    private String contraUsuario;
    private String nombreDelUsuario;
    private String correoUsuario;
    private String direccionUsuario;
    private String telefonoUsuario;
}
