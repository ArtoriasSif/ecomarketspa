package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.clients.InventarioClientRest;
import com.nebula.msvc_detalle_pedido.clients.PedidoClientRest;
import com.nebula.msvc_detalle_pedido.clients.ProductoClientRest;
import com.nebula.msvc_detalle_pedido.dtos.QuantityUpdateDTO;
import com.nebula.msvc_detalle_pedido.exceptions.DetallePedidosException;
import com.nebula.msvc_detalle_pedido.models.Inventario;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.models.Pedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    @Autowired
    PedidoClientRest pedidoClientRest;

    @Autowired
    ProductoClientRest productoClientRest;

    @Autowired
    InventarioClientRest inventarioClientRest;

    @Transactional
    @Override
    public List<DetallePedido> save(List<DetallePedido> detallePedido){
        if (detallePedido == null || detallePedido.isEmpty()) {
            throw new DetallePedidosException("No hay productos para guardar");
        }
        //validar si existe pedido
        Pedido pedido;
        try {
            pedido = pedidoClientRest.findById(detallePedido.get(0).getIdPedido());
        }catch (FeignException ex){
            throw new DetallePedidosException("Pedido con id: "+detallePedido.get(0).getIdPedido()+" no existe");
        }
        //Buscar inventarios por sucursal
        List<Inventario> inventarios = inventarioClientRest.findByIdSucursal(pedido.getIdSucursal());

        //Mapear por idProducto
        Map<Long, Inventario> inventariosMap = inventarios.stream().collect(
                Collectors.toMap(Inventario::getIdProducto, inventario -> inventario));

        List<DetallePedido> pedidoGuardado = new ArrayList<>();

        for (DetallePedido dp : detallePedido) {
            Inventario i = inventariosMap.get(dp.getIdProducto());

            if(i== null){
                throw new DetallePedidosException("No existe el producto con id: "+dp.getIdProducto());
            }

            if(i.getCantidad() < dp.getCantidad()){
                throw new DetallePedidosException("No hay suficientes stock del producto con id: "+dp.getIdProducto());
            }

            dp.setSubTotal(dp.getCantidad() * productoClientRest.findByIdProducto(dp.getIdProducto()).getPrecio());

            //Guardar detalle
            pedidoGuardado.add(dp);

            //Actualizar Inventario
            inventarioClientRest.updateQuantity(new QuantityUpdateDTO(dp.getIdProducto(),pedido.getIdSucursal(),-dp.getCantidad()));

        }
        //Guardar Detalles
        detallePedidoRepository.saveAll(pedidoGuardado);

        return pedidoGuardado;
    }

    @Transactional
    @Override
    public List<DetallePedido> findByIdPedido(Long idPedido) {
        return detallePedidoRepository.findByIdPedido(idPedido);
    }

}
