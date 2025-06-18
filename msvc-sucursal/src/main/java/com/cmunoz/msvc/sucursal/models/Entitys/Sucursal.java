package com.cmunoz.msvc.sucursal.models.Entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "sucursal")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Entidad que representa una sucursal")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    @Schema(description = "Id de una sucursal", example = "1")
    private Long idSucursal;

    @Column(name = "nombre_sucursal", nullable = false)
    @NotBlank
    @Schema(description = "Nombre de una sucursal", example = "Sucursal Lastarria")
    private String nombreSucursal;

    @Column(name = "direccion_sucursal",nullable = false)
    @NotBlank(message = "El campo dirección no puede estar vacío")
    @Pattern(
            regexp = "([A-Za-zÁÉÍÓÚáéíóúñÑüÜ]+\\s?)+\\s([1-9][0-9]*)\\s*",
            message = "El campo dirección debe tener el formato 'Calle Ejemplo 1234'"
    )
    @Schema(description = "Direccion de una sucursal", example = "Lastarria 1234")
    private String direccionSucursal;

    @Column(name="ciudad_sucursal",nullable = false)
    @NotBlank(message = "El campo ciudad no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo provincia tiene que tener solo letras")
    @Schema(description = "Ciudad de una sucursal", example = "Santiago")
    private String ciudadSucursal;

    @Column(name="provincia_sucursal",nullable = false)
    @NotBlank(message = "El campo provincia no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo provincia tiene que tener solo letras")
    @Schema(description = "Provincia de una sucursal", example = "Santiago")
    private String provinciaSucursal;

    @Column(name="region_sucursal",nullable = false)
    @NotBlank(message = "El campo region no puede estar vacio")
    @Pattern(regexp = "[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo region tiene que tener solo letras")
    @Schema(description = "Region de una sucursal", example = "Metropolitana")
    private String regionSucursal;

    @Column(name="telefono_sucursal",nullable = false)
    @NotBlank(message = "El campo telefono no puede estar vacio")
    @Pattern(regexp = "^\\+569[0-9]{8}$", message = "El número debe comenzar con +569 y tener 8 dígitos finales")
    @Schema(description = "Telefono de una sucursal", example = "+56991234567")
    private String telefonoSucursal;

    @Column(name="email_sucursal",nullable = false, unique = true)
    @NotBlank(message = "El campo Email no puede estar vacio")
    @Email(message = "El campo Email tiene que tener un formato valido")
    @Schema(description = "Email de una sucursal", example = "lastarria@marketspaeco.cl")
    private String emailSucursal;

    public Sucursal(String nombreSucursal, String direccionSucursal, String ciudadSucursal, String provinciaSucursal, String regionSucursal, String telefonoSucursal, String emailSucursal) {
        this.nombreSucursal = nombreSucursal;
        this.direccionSucursal = direccionSucursal;
        this.ciudadSucursal = ciudadSucursal;
        this.provinciaSucursal = provinciaSucursal;
        this.regionSucursal = regionSucursal;
        this.telefonoSucursal = telefonoSucursal;
        this.emailSucursal = emailSucursal;
    }

}
