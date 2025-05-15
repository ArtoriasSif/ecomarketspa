package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.dtos.PedidoRequestDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;

public interface PedidoService {
    PedidoResponseDTO save(PedidoRequestDTO pedidoDTO);
}
