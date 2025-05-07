package com.nebulosa.msvc_inventario.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="inventarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventario_id")
    private Long inventarioId;

    @Column(nullable = false)
    private Long idProducto;

    @Column(nullable = false)
    private Long cantidad;

}
