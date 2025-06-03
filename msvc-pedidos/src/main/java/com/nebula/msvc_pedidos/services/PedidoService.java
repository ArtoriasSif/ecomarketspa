package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO save(PedidoDTO pedidoDTO);
    Pedido findById(Long id);
    PedidoConDetalleDTO findPedidoConDetalles(Long idPedido);
    String deletePedidoId(Long id);
}
