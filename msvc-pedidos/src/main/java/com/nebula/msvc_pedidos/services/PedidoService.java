package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;

public interface PedidoService {
    PedidoResponseDTO save(PedidoDTO pedidoDTO);
}
