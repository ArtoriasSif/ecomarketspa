package com.cmunoz.msvc.sucursal.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pedido {

    private Long idPedido;

    private LocalDateTime fechaPedido;

    private Double totalPedido;

    private Long idUsuario; // ID del usuario desde msvc-usuario

    private Long idSucursal; // ID de la sucursal desde msvc-sucursal
}
