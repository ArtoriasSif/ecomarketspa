package com.nebulosa.msvc_inventario.models.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="inventarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto; // ID del producto desde msvc-products

    @Column(name = "id_sucursal", nullable = false)
    private Long idSucursal; // ID de la sucursal desde msvc-sucursal

    @Column(nullable = false)
    private Long cantidad;


}
