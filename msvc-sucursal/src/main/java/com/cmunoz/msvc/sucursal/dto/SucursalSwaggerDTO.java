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
    @Schema (description = "Ciudad de la sucursal", example = "Santiago")
    private String ciudadSucursal;
    @Schema (description = "Provincia de la sucursal", example = "Santiago")
    private String provinciaSucursal;
    @Schema (description = "Region de la sucursal", example = "Metropolitana")
    private String regionSucursal;
    @Schema (description = "Telefono de la sucursal", example = "+56991234567")
    private String telefonoSucursal;
    @Schema (description = "Email de la sucursal", example = "<EMAIL>")
    private String emailSucursal;
}
