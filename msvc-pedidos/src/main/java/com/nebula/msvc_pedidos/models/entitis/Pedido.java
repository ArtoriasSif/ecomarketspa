package com.nebula.msvc_pedidos.models.entitis;

import com.nebula.msvc_pedidos.dtos.ItemPedidoResponseDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido",nullable = false)
    private Long idPedido;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "total_pedido", nullable = false)
    private Double totalPedido;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario; // ID del usuario desde msvc-usuario

    @Column(name = "sucursal_id", nullable = false)
    private Long idSucursal; // ID de la sucursal desde msvc-sucursal



}
