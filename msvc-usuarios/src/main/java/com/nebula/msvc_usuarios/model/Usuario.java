package com.nebula.msvc_usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name ="usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario",nullable = false)
    private Long idUsuario;

    @Column(name="nombre_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo nombre de usuario no puede estar vacio.")
    private String nombreUsuario;

    @Column(name="contrasenna_usuario",nullable = false)
    @NotBlank(message = "El campo contraseña no puede estar vacio.")
    private String contraUsuario;

    @Column(name="nombre_del_usuario",nullable = false)
    @NotBlank(message = "El campo nombre del usuario no puede estar vacio.")
    private String nombreDelUsuario;

    @Column(name="correo_usuario",nullable = false,unique = true)
    @Email(message = "El campo email tiene que tener un formato valido.")
    @NotBlank(message = "El campo correo no puede estar vacio.")
    private String correoUsuario;

    @Column(name="rut_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo rut no puede estar vacio.")
    @Pattern(regexp= "\\d{1,8}-[\\dKk]",message="El formato del rut debe ser XXXXXXXX-X")
    private String rutUsuario;

    @Column(name="direccion_usuario",nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio.")
    @Pattern(regexp = "([1-9][0-9]{0,3})\\s+[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo direccion tiene que llevar el siguiente formato 'XXXX LETRAS'")
    private String direccionUsuario;

    @Column(name="telefono_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo numero telefono no puede estar vacio.")
    @Pattern(regexp = "\\+56 9\\d{1,8}", message = "El formato del telefono debe ser +56 9XXXXXXXX")
    private String telefonoUsuario;
}
