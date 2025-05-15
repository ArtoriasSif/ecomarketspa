package com.nebula.msvc_pedidos.repositories;

import com.nebula.msvc_pedidos.models.entitis.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositoty extends JpaRepository <Pedido, Long> {
}
