package com.nebulosa.msvc_products.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name ="producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto",nullable = false)
    private Long idProducto;

    @Column(name="nombre_producto",nullable = false,unique = true)
    @NotBlank(message = "El campo nombre del producto no puede ser vacio")
    private String nombreProducto;

    @Column(nullable = false)
    @NotNull(message = "El campo precio no puede ser vacio")
    private Double precio;

}
