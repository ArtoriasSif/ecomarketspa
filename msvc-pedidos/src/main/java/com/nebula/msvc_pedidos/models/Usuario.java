package com.nebula.msvc_pedidos.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    private Long idUsuario;

    private String nombreUsuario;

    private String contraUsuario;

    private String nombreDelUsuario;

    private String correoUsuario;

    private String rutUsuario;

    private String direccionUsuario;

    private String telefonoUsuario;
}
