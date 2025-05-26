package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.models.DetallePedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    public DetallePedido save(DetallePedido detallePedido){



        return xd;
    }

}
