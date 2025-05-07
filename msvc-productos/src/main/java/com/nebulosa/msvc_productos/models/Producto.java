package com.nebulosa.msvc_productos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name ="productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id",nullable = false)
    private Long productoId;

    @Column(name="nombre_producto",nullable = false)
    @NotBlank(message = "El campo nombre del producto no puede ser vacio")
    private String nombreProducto;

    @Column(nullable = false)
    @NotNull(message = "El campo precio no puede ser vacio")
    private Long precio;

}
