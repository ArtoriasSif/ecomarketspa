package com.nebula.msvc_detalle_pedido.model;


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
    private Long idDetallePedido;

    private Long idPedido;

    private Long idProducto;

    private Double cantidad;
}
