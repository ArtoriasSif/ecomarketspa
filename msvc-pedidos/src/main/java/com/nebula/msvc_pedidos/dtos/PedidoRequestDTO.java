package com.nebula.msvc_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    private Long idUsuario;
    private Long idSucursal;
    private List<ItemPedidoDTO> items;
}
