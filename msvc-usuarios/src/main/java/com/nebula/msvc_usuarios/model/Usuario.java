package com.nebula.msvc_usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID del usuario", example = "3")
    private Long idUsuario;

    @Column(name="nombre_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo nombre de usuario no puede estar vacio.")
    @Schema(description = "Nombre del usuario", example = "Pedro")
    private String nombreUsuario;

    @Column(name="contrasenna_usuario",nullable = false)
    @NotBlank(message = "El campo contraseña no puede estar vacio.")
    @Schema(description = "Correo electrónico del usuario", example = "pedro@example.com")
    private String contraUsuario;

    @Column(name="nombre_del_usuario",nullable = false)
    @NotBlank(message = "El campo nombre del usuario no puede estar vacio.")
    @Schema(description = "Nombre del usuario", example = "pedroxd")
    private String nombreDelUsuario;

    @Column(name="correo_usuario",nullable = false,unique = true)
    @Email(message = "El campo email tiene que tener un formato valido.")
    @NotBlank(message = "El campo correo no puede estar vacio.")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    private String correoUsuario;

    @Column(name="rut_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo rut no puede estar vacio.")
    @Pattern(regexp= "\\d{1,8}-[\\dKk]",message="El formato del rut debe ser XXXXXXXX-X")
    @Schema(description = "RUT del usuario con guion y dígito verificador", example = "12345678-9")
    private String rutUsuario;

    @Column(name="direccion_usuario",nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio.")
    @Pattern(regexp = "([1-9][0-9]{0,3})\\s+([A-Za-zÁÉÍÓÚáéíóúñÑüÜ]+\\s*)+", message = "El campo direccion tiene que llevar el formato 'XXXX LETRAS'")
    @Schema(description = "Dirección del usuario", example = "1111 av. xdent")
    private String direccionUsuario;

    @Column(name="telefono_usuario",nullable = false,unique = true)
    @NotBlank(message = "El campo numero telefono no puede estar vacio.")
    @Pattern(regexp = "\\+56 9\\d{1,8}", message = "El formato del telefono debe ser +56 9XXXXXXXX")
    @Schema(description = "Número de teléfono del usuario en formato chileno", example = "+56912345678")
    private String telefonoUsuario;

    public Usuario(String otroUser, String mail, String s, String s1, String s2) {
    }
}
