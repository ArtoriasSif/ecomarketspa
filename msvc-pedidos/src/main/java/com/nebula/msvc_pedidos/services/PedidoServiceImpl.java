package com.nebula.msvc_pedidos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        //validar que existe Usuario
        try {
            usuarioClientRest.findByIdUsuario(pedidoDTO.getIdUsuario());
        } catch (FeignException ex) {
            throw new PedidoException("No se encontró el usuario con id: " + pedidoDTO.getIdUsuario());
        }
        //validar que existe Sucursal
        try {
            sucursalClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        } catch (FeignException ex) {
            throw new PedidoException("No se encontró la sucursal con id: " + pedidoDTO.getIdSucursal());
        }
        //Buscar todos inventarios de la sucursal
        List<Inventario> inventarioSucursal = inventarioClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        Map<Long, Inventario> mapaInventario = inventarioSucursal.stream().collect(
                Collectors.toMap(Inventario::getIdProducto, Function.identity())
        );
        //Lista de respuesta al postman
        List<ItemPedidoResponseDTO> listaProductos = new ArrayList<>();
        Double total = 0.0;
        //Validar lista de la existencia de productos y cantidades enviados por posman
                                    //Lista de idProducto y cantidad de postman
        for (ItemProductoDTO item : pedidoDTO.getProductos()) {
            Inventario inventario = mapaInventario.get(item.getIdProducto());
            //Validar existencia de producto
            if (inventario == null) {
                throw new PedidoException("No se encontró el producto con id: " + item.getIdProducto());
            }
            //validar la cantidad del inventario
            if (inventario.getCantidad() < item.getCantidad()) {
                throw new PedidoException("No hay suficientes producto con id: " + item.getIdProducto()) ;
            }
            // Descontar inventario usando DTO (cantidad negativa)
            QuantityUpdateDTO dto = new QuantityUpdateDTO(item.getIdProducto(), pedidoDTO.getIdSucursal(), -item.getCantidad());
            inventarioClientRest.updateQuantity(dto);

            //Creacion de producto y guardado del pedidoResponse
            Producto producto = productoClientRest.findByIdProducto(item.getIdProducto());
            ItemPedidoResponseDTO itensPedidoResponse = new ItemPedidoResponseDTO(producto.getNombreProducto(),
                    producto.getPrecio(), item.getCantidad(), item.getCantidad() * producto.getPrecio());
            total += item.getCantidad() * producto.getPrecio();
            listaProductos.add(itensPedidoResponse);
        }

        // Convertir lista de productos a JSON
        ObjectMapper mapper = new ObjectMapper();
        String productosJson;
        try {
            productosJson = mapper.writeValueAsString(listaProductos);
        } catch (JsonProcessingException e) {
            throw new PedidoException("Error al convertir productos a JSON");
        }

        //Guardar pedido, clase Entity con el atributo detalles definida explícitamente como tipo SQL pero en String
        LocalDateTime fecha = LocalDateTime.now();
        Pedido pedido = new Pedido(null, fecha, total, pedidoDTO.getIdUsuario(), pedidoDTO.getIdSucursal(), productosJson);
        pedidoRepositoty.save(pedido); //Guarda las informaciones de pedido

        //Setar respuesta
        Usuario usuario = usuarioClientRest.findByIdUsuario(pedidoDTO.getIdUsuario());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedidoDTO.getIdSucursal());
        return new PedidoResponseDTO(usuario.getNombreUsuario(),
                usuario.getCorreoUsuario(), sucursal.getNombreSucursal(), sucursal.getDireccionSucursal(),
                sucursal.getProvinciaSucursal(), fecha, total, listaProductos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findAll() {
        //Acceder a todos los pedidos
        List<Pedido> pedidos = pedidoRepositoty.findAll();
        //Transformar a lista de respuesta
        List<PedidoResponseDTO> listaPedidos = new ArrayList<>();
        //Acceder al mapper para obtener detales de todos las id de producto y cantidad
        ObjectMapper mapper = new ObjectMapper();

        //Recorrer todos los pedidos
        for (Pedido pedido : pedidos) {
            // Obtener clases
            Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
            Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());

            // Convertir 'detales' JSON a lista de productos
            List<ItemPedidoResponseDTO> listaProductos;
            try {
                listaProductos = Arrays.asList(
                        mapper.readValue(pedido.getDetalleProductos(), ItemPedidoResponseDTO[].class)
                );
            } catch (JsonProcessingException e) {
                throw new PedidoException("Error al convertir productos del pedido ID " + pedido.getIdPedido());
            }
            // Crear DTO de respuesta
            PedidoResponseDTO dto = new PedidoResponseDTO(
                    usuario.getNombreUsuario(),
                    usuario.getCorreoUsuario(),
                    sucursal.getNombreSucursal(),
                    sucursal.getDireccionSucursal(),
                    sucursal.getProvinciaSucursal(),
                    pedido.getFechaPedido(),
                    pedido.getTotalPedido(),
                    listaProductos
            );
            listaPedidos.add(dto);
        }
        return listaPedidos;
    }
}

