package com.nebulosa.msvc_inventario.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private Long idSucursal;

    private String direccionSucursal;

    private String ciudadSucursal;

    private String telefonoSucursal;

    private String nombreSucursal;

    private String provinciaSucursal;

    private String regionSucursal;

     private String emailSucursal;
}
