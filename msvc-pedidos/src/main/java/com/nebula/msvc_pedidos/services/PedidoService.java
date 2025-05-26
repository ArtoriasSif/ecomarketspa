package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO save(PedidoDTO pedidoDTO);
    List<PedidoResponseDTO> findAll();
    Pedido findById(Long id);
}
