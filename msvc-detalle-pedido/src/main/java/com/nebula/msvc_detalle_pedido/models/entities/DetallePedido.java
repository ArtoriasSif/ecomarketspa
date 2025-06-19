package com.nebula.msvc_detalle_pedido.models.entities;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="detalle_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "DetallePedido", description = "Entidad que representa el detalle de un pedido",
        example = "{\n" +
                "  \"idDetallePedido\": 123,\n" +
                "  \"idPedido\": 456,\n" +
                "  \"idProducto\": 789,\n" +
                "  \"cantidad\": 2,\n" +
                "  \"subTotal\": 49.99\n" +
                "}"
)
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
