package com.nebula.msvc_detalle_pedido.services;


import com.nebula.msvc_detalle_pedido.dtos.UpdateQuantidadProductoPedidoDTO;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;

import java.util.List;

public interface DetallePedidoService {
    List<DetallePedido> save(List<DetallePedido> detallePedido);
    List<DetallePedido> findByIdPedido(Long idPedido);
    void deleteDetallePedido (Long idPedido);
    String updateCatidadProductoPedido (Long idDetallePedido, UpdateQuantidadProductoPedidoDTO updateDTO);
}
