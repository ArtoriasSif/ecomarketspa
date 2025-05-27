package com.nebula.msvc_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter @Setter
public class PedidoResponseDTO {
    private String nombreUsuario;
    private Long idPedido;
    private String mensaje;
}
