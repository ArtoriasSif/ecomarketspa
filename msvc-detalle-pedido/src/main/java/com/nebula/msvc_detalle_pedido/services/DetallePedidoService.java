package com.nebula.msvc_detalle_pedido.services;


import com.nebula.msvc_detalle_pedido.dtos.DetallePedidoRequestDTO;
import com.nebula.msvc_detalle_pedido.dtos.DetallePedidoResponseDTO;
import com.nebula.msvc_detalle_pedido.dtos.UpdateCuantidadProductoDetallePedidoDTO;
import com.nebula.msvc_detalle_pedido.dtos.UpdateCuantidadProductoDetallePedidoResponseDTO;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;

import java.util.List;

public interface DetallePedidoService {
    List<DetallePedidoResponseDTO> save(List<DetallePedidoRequestDTO> detallePedidosDto);
    List<DetallePedido> findByIdPedido(Long idPedido);
    void deleteDetallePedido (Long idPedido);
    List<DetallePedidoResponseDTO> findDetailsByIdPedido(Long idPedido);
    UpdateCuantidadProductoDetallePedidoResponseDTO updateCantidadProductoPedido (Long idDetallePedido, UpdateCuantidadProductoDetallePedidoDTO updateDTO);
    List<DetallePedido> findAll();
    List<DetallePedidoResponseDTO> findAllDetallesDTO();

}
