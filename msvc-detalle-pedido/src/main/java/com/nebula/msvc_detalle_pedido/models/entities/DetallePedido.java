package com.nebula.msvc_detalle_pedido.models.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="detalle_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido",nullable = false)
    private Long idDetallePedido;

    @Column(name = "id_pedido",nullable = false)
    private Long idPedido;

    @Column(name = "id_producto",nullable = false)
    private Long idProducto;

    @Column(nullable = false)
    private Long cantidad;

    @Column(nullable = false)
    private Double subTotal;
}
