package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.clients.InventarioClientRest;
import com.nebula.msvc_pedidos.clients.ProductoClientRest;
import com.nebula.msvc_pedidos.clients.SucursalClientRest;
import com.nebula.msvc_pedidos.clients.UsuarioClientRest;
import com.nebula.msvc_pedidos.dtos.*;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.Inventario;
import com.nebula.msvc_pedidos.models.Producto;
import com.nebula.msvc_pedidos.models.Sucursal;
import com.nebula.msvc_pedidos.models.Usuario;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepositoty;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepositoty pedidoRepositoty;

    @Autowired
    private InventarioClientRest inventarioClientRest;

    @Autowired
    private UsuarioClientRest usuarioClientRest;
    @Autowired
    private SucursalClientRest sucursalClientRest;
    @Autowired
    private ProductoClientRest productoClientRest;

    @Transactional
    @Override
    public PedidoResponseDTO save(PedidoRequestDTO pedidoDTO) {

        // 1. Validar y obtener usuario
        Usuario usuario;
        try {
            usuario = usuarioClientRest.findByIdUsuario(pedidoDTO.getIdUsuario());
        } catch (FeignException.NotFound ex) {
            throw new PedidoException("No se encontró el usuario con id: " + pedidoDTO.getIdUsuario());
        }

        // 2. Validar y obtener sucursal
        Sucursal sucursal;
        try {
            sucursal = sucursalClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        } catch (FeignException.NotFound ex) {
            throw new PedidoException("No se encontró la sucursal con id: " + pedidoDTO.getIdSucursal());
        }

        // 3. Procesar items
        double total = 0.0;
        List<ItemDetalleDTO> detalleList = new ArrayList<>();

        for (ItemPedidoDTO item : pedidoDTO.getItems()) {

            if (item.getIdProducto() == null) {
                throw new PedidoException("El id del producto no puede ser null");
            }

            Producto producto;
            try {
                producto = productoClientRest.findByIdProducto(item.getIdProducto());
            } catch (FeignException.NotFound ex) {
                throw new PedidoException("No se encontró el producto con id: " + item.getIdProducto());
            }

            // Validar inventario
            Inventario inventario = inventarioClientRest.findByIdProductoAndIdSucursal(
                    item.getIdProducto(), pedidoDTO.getIdSucursal()
            );

            if (inventario == null || inventario.getCantidad() < item.getCantidad()) {
                throw new PedidoException("No hay suficientes productos en inventario para el producto con id: " + item.getIdProducto());
            }

            // Calcular subtotal
            double subtotal = producto.getPrecio() * item.getCantidad();
            total += subtotal;

            // Agregar a la lista de detalles
            ItemDetalleDTO detalle = new ItemDetalleDTO();
            detalle.setNombreProducto(producto.getNombreProducto());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(subtotal);

            detalleList.add(detalle);
        }

        // 4. Guardar pedido en la base de datos si lo necesitas (opcional)

        // 5. Construir la respuesta
        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setNombreUsuario(usuario.getNombreUsuario());
        response.setNombreSucursal(sucursal.getNombreSucursal());
        response.setDetalles(detalleList);
        response.setTotal(total);

        return response;
    }
}

