package com.cmunoz.msvc.sucursal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SucursalSwaggerDTO {

    @JsonIgnore
    private Long idSucursal;
    @Schema (description = "Nombre de la sucursal", example = "Sucursal Lastarria")
    private String nombreSucursal;
    @Schema (description = "Direccion de la sucursal", example = "Lastarria 1234")
    private String direccionSucursal;
    @Schema
    private String ciudadSucursal;
    @Schema
    private String provinciaSucursal;
    @Schema
    private String regionSucursal;
    @Schema
    private String telefonoSucursal;
    private String emailSucursal;
}
