package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.clients.PedidoClientRest;
import com.nebula.msvc_detalle_pedido.clients.ProductoClientRest;
import com.nebula.msvc_detalle_pedido.exceptions.DetalleProductoException;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.models.Pedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    @Autowired
    PedidoClientRest pedidoClientRest;

    @Autowired
    ProductoClientRest productoClientRest;

    @Transactional
    @Override
    public List<DetallePedido> save(DetallePedido detallePedido){

        try {
            Pedido pedido = pedidoClientRest.findById(detallePedido.getIdPedido());
        }catch (FeignException ex){
            throw new DetalleProductoException("No se pudo encontrar el pedido");
        }


        return xd;
    }

}
