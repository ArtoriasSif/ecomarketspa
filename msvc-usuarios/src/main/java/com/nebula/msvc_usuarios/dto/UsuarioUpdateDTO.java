package com.nebula.msvc_usuarios.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    private String contraUsuario;
    private String nombreDelUsuario;
    private String correoUsuario;
    private String direccionUsuario;
    private String telefonoUsuario;
}
