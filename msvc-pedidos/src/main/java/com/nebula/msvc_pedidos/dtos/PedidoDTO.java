package com.nebula.msvc_pedidos.dtos;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @ToString
@Getter @Setter
public class PedidoDTO {
    private Long idUsuario;
    private Long idSucursal;
}
