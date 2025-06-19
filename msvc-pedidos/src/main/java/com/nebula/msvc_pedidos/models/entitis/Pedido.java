package com.nebula.msvc_pedidos.models.entitis;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Entidad que representa un Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido",nullable = false)
    @Schema(description = "Codigo del pedido", example = "1")
    private Long idPedido;

    @Column(name = "fecha_pedido", nullable = false)
    @Schema(description = "fecha del pedido", example = "2025-06-19T21:32:00")
    private LocalDateTime fechaPedido;

    @Column(name = "id_usuario", nullable = false)
    @Schema(description = "Codigo del usuario", example = "1")
    private Long idUsuario; // ID del usuario desde msvc-usuario

    @Column(name = "id_sucursal", nullable = false)
    @Schema(description = "Codigo del usuario", example = "1")
    private Long idSucursal; // ID de la sucursal desde msvc-sucursal

    public Pedido(LocalDateTime fechaPedido, Long idPedido, Long idUsuario, Long idSucursal) {
        this.fechaPedido = fechaPedido;
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
    }

}
