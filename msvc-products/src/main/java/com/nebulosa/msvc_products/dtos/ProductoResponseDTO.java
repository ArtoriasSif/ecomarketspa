package com.nebulosa.msvc_products.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductoResponseDTO {
    private String descripcion;
    private Double precio;
    private String nombreProducto;


}
