package com.nebula.msvc_pedidos.repositories;

import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long> {
    List<Pedido> findAllByIdSucursal(Long idSucursal);

}
