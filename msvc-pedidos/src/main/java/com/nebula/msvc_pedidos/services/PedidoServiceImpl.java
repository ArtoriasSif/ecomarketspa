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
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public PedidoResponseDTO save(PedidoDTO pedidoDTO) {
        //Validacion
        try {
            usuarioClientRest.findByIdUsuario(pedidoDTO.getIdUsuario());
        }catch (FeignException ex){
            throw new PedidoException("No se encontró el usuario con id: " + pedidoDTO.getIdUsuario());
        }
        try {
            sucursalClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        }catch (FeignException ex){
            throw new PedidoException("No se encontró la sucursal con id: " + pedidoDTO.getIdSucursal());
        }
        List<Inventario> inventarioSucursal = inventarioClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        Map<Long , Long> mapaInventario = inventarioSucursal.stream().collect(
                Collectors.toMap(Inventario::getIdProducto, Inventario::getCantidad)
        );

        List<ItemPedidoResponseDTO> listaProductos = new ArrayList<>();
        Double total = 0.0;
        for (ItemProductoDTO item : pedidoDTO.getProductos()) {
            if (!mapaInventario.containsKey(item.getIdProducto())){
                throw new PedidoException("No se encontró el producto con id: " + item.getIdProducto());
            }
            if (mapaInventario.get(item.getIdProducto()) < item.getCantidad()){
                throw new PedidoException("No hay suficientes existencias del producto con id: " + item.getIdProducto());
            }

            //Creacion de producto
            Producto producto = productoClientRest.findByIdProducto(item.getIdProducto());
            ItemPedidoResponseDTO itemPedido = new ItemPedidoResponseDTO(producto.getNombreProducto(),
            producto.getPrecio(), item.getCantidad(), item.getCantidad() * producto.getPrecio() );

            total += item.getCantidad() * producto.getPrecio();
            listaProductos.add(itemPedido);
        }
        //Guardar pedido
        LocalDateTime fecha = LocalDateTime.now();
        Pedido pedido = new Pedido(null,fecha, total , pedidoDTO.getIdUsuario(), pedidoDTO.getIdSucursal());
        pedidoRepositoty.save(pedido);

        //Respoesta

        Usuario usuario = usuarioClientRest.findByIdUsuario(pedidoDTO.getIdUsuario());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());

        return new PedidoResponseDTO(usuario.getNombreUsuario(),
                usuario.getCorreoUsuario(), sucursal.getNombreSucursal(), sucursal.getDireccionSucursal(),
                sucursal.getProvinciaSucursal(),fecha, total , listaProductos );

    }
}

