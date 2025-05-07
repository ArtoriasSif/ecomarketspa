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
    @Column(name = "sucursal_id")
    private Long IdSucursal;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El campo direccion no puede estar vacio")
    @Pattern(regexp = "([1-9][0-9]{0,3})\\s+[A-Za-zÁÉÍÓÚáéíóúñÑüÜ ]+", message = "El campo direccion tiene que llevar el siguiente formato 'XXXX LETRAS'")
    private String direccionSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo ciudad no puede estar vacio")
    @Pattern(regexp = "^[A-Z]$", message = "El campo provincia tiene que tener solo letras")
    private String ciudadSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo provincia no puede estar vacio")
    @Pattern(regexp = "^[A-Z]$", message = "El campo provincia tiene que tener solo letras")
    private String provinciaSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo region no puede estar vacio")
    @Pattern(regexp = "^[A-Z]$", message = "El campo region tiene que tener solo letras")
    private String regionSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo telefono no puede estar vacio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El campo telefono tiene que tener 9 digitos")
    private String telefonoSucursal;

    @Column(nullable = false)
    @NotBlank(message = "El campo Email no puede estar vacio")
    @Email(message = "El campo Email tiene que tener un formato valido")
    private String EmailSucursal;

}
