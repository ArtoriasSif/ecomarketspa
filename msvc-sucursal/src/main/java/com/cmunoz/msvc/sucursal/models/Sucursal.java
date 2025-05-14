package com.cmunoz.msvc.sucursal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sucursal")
@Getter @Setter
@NoArgsConstructor
@ToString
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "nombre_sucursal", nullable = false)
    @NotBlank
    private String nombreSucursal;

    @Column(name = "direccion_sucursal",nullable = false)
    @NotBlank(message = "El campo dirección no puede estar vacío")
    @Pattern(
            regexp = "([A-Za-zÁÉÍÓÚáéíóúñÑüÜ]+\\s?)+\\s([1-9][0-9]*)\\s*",
            message = "El campo dirección debe tener el formato 'Calle Ejemplo 1234'"
    )
    private String direccionSucursal;

    @Column(name="ciudad_sucursal",nullable = false)
    @NotBlank(message = "El campo ciudad no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo provincia tiene que tener solo letras")
    private String ciudadSucursal;

    @Column(name="provincia_sucursal",nullable = false)
    @NotBlank(message = "El campo provincia no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo provincia tiene que tener solo letras")
    private String provinciaSucursal;

    @Column(name="region_sucursal",nullable = false)
    @NotBlank(message = "El campo region no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo region tiene que tener solo letras")
    private String regionSucursal;

    @Column(name="telefono_sucursal",nullable = false)
    @NotBlank(message = "El campo telefono no puede estar vacio")
    @Pattern(regexp = "^\\+569[0-9]{8}$", message = "El número debe comenzar con +569 y tener 8 dígitos finales")
    private String telefonoSucursal;

    @Column(name="email_sucursal",nullable = false, unique = true)
    @NotBlank(message = "El campo Email no puede estar vacio")
    @Email(message = "El campo Email tiene que tener un formato valido")
    private String emailSucursal;

}
