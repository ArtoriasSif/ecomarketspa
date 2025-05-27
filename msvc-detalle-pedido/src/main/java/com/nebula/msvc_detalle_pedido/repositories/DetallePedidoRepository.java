package com.nebula.msvc_detalle_pedido.repositories;

import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository <DetallePedido, Long>{
    List<DetallePedido> findByIdPedido(Long idPedido);
}
