package com.nebula.msvc_pedidos.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Sucursal {

    private Long idSucursal;

    private String nombreSucursal;

    private String direccionSucursal;

    private String ciudadSucursal;

    private String provinciaSucursal;

    private String regionSucursal;

    private String telefonoSucursal;

    private String emailSucursal;
}
