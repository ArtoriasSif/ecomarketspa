package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.clients.*;
import com.nebula.msvc_detalle_pedido.dtos.*;
import com.nebula.msvc_detalle_pedido.exceptions.DetallePedidosException;
import com.nebula.msvc_detalle_pedido.exceptions.ResourceNotFoundException;
import com.nebula.msvc_detalle_pedido.models.*;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
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

    @Autowired
    UsuarioClientRest usuarioClientRest;

    @Autowired
    SucursalClientRest sucursalClientRest;



    @Transactional
    @Override
    public List<DetallePedido> findByIdPedido(Long idPedido) {
        List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            throw new DetallePedidosException("No se encontraron detalles para el pedido con ID: " + idPedido);
        }
        return detalles;
    }

    @Override
    public List<DetallePedidoResponseDTO> findDetailsByIdPedido(Long idPedido) {
        List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(idPedido);
        if (detalles.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron detalles para el pedido con ID: " + idPedido);
        }

        List<DetallePedidoResponseDTO> dtos = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            Pedido pedido = pedidoClientRest.findById(detalle.getIdPedido());
            if (pedido == null) {
                throw new ResourceNotFoundException("No se encontró el pedido con ID: " + detalle.getIdPedido());
            }

            Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
            Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());
            Producto producto = productoClientRest.findByIdProducto(detalle.getIdProducto());

            if (usuario == null || sucursal == null || producto == null) {
                throw new ResourceNotFoundException("Faltan datos relacionados al pedido (usuario, sucursal o producto)");
            }

            DetallePedidoResponseDTO dto = DetallePedidoResponseDTO.builder()
                    .idDetallePedido(detalle.getIdDetallePedido())
                    .idPedido(detalle.getIdPedido())
                    .nombreUsuario(usuario.getNombreUsuario())
                    .nombreSucursal(sucursal.getNombreSucursal())
                    .nombreProducto(producto.getNombreProducto())
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subTotal(detalle.getSubTotal())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }



    @Transactional
    @Override
    public List<DetallePedido> findAll(){
        return detallePedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoResponseDTO> findAllDetallesDTO() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();

        List<DetallePedidoResponseDTO> dtos = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            Pedido pedido = pedidoClientRest.findById(detalle.getIdPedido());
            Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
            Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());
            Producto producto = productoClientRest.findByIdProducto(detalle.getIdProducto());

            DetallePedidoResponseDTO dto = DetallePedidoResponseDTO.builder()
                    .idDetallePedido(detalle.getIdDetallePedido())
                    .idPedido(detalle.getIdPedido())
                    .nombreUsuario(usuario.getNombreUsuario())
                    .nombreSucursal(sucursal.getNombreSucursal())
                    .idProducto(producto.getIdProducto())
                    .nombreProducto(producto.getNombreProducto())
                    .cantidad(detalle.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subTotal(detalle.getSubTotal())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    @Override
    public List<DetallePedidoResponseDTO> save(List<DetallePedidoRequestDTO> detallePedidosDto) {
        if (detallePedidosDto == null || detallePedidosDto.isEmpty()) {
            throw new DetallePedidosException("No hay productos para guardar");
        }

        Long idPedido = detallePedidosDto.get(0).getIdPedido();

        Pedido pedido;
        try {
            pedido = pedidoClientRest.findById(idPedido);
        } catch (FeignException ex) {
            throw new DetallePedidosException("Pedido con id: " + idPedido + " no existe");
        }

        Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());

        List<Inventario> inventarios = inventarioClientRest.findByIdSucursal(pedido.getIdSucursal());

        Map<Long, Inventario> inventarioMap = inventarios.stream()
                .collect(Collectors.toMap(Inventario::getIdProducto, inv -> inv));

        List<DetallePedido> pedidosGuardados = new ArrayList<>();
        List<DetallePedidoResponseDTO> responseList = new ArrayList<>();

        for (DetallePedidoRequestDTO dto : detallePedidosDto) {
            Inventario inv = inventarioMap.get(dto.getIdProducto());

            if (inv == null) {
                throw new DetallePedidosException("No existe el producto con id: " + dto.getIdProducto());
            }

            if (inv.getCantidad() < dto.getCantidad()) {
                throw new DetallePedidosException("No hay suficiente stock del producto con id: " + dto.getIdProducto());
            }

            Producto producto = productoClientRest.findByIdProducto(dto.getIdProducto());
            double subTotal = dto.getCantidad() * producto.getPrecio();

            DetallePedido detalle = new DetallePedido();
            detalle.setIdPedido(dto.getIdPedido());
            detalle.setIdProducto(dto.getIdProducto());
            detalle.setCantidad(dto.getCantidad());
            detalle.setSubTotal(subTotal);

            pedidosGuardados.add(detalle);

            // Actualizar inventario
            inventarioClientRest.updateQuantity(new QuantityUpdateDTO(
                    dto.getIdProducto(),
                    pedido.getIdSucursal(),
                    -dto.getCantidad()
            ));
        }

        List<DetallePedido> guardados = detallePedidoRepository.saveAll(pedidosGuardados);

        for (DetallePedido d : guardados) {
            Producto producto = productoClientRest.findByIdProducto(d.getIdProducto());

            responseList.add(DetallePedidoResponseDTO.builder()
                    .idDetallePedido(d.getIdDetallePedido())   // Si tienes este campo en DTO
                    .idPedido(d.getIdPedido())
                    .nombreUsuario(usuario.getNombreDelUsuario())
                    .nombreSucursal(sucursal.getNombreSucursal())
                    .nombreProducto(producto.getNombreProducto())
                    .idProducto(producto.getIdProducto())
                    .cantidad(d.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subTotal(d.getSubTotal())
                    .build()
            );
        }

        return responseList;
    }

    @Transactional
    @Override
    public UpdateCuantidadProductoDetallePedidoResponseDTO updateCantidadProductoPedido(Long idDetallePedido, UpdateCuantidadProductoDetallePedidoDTO updateDTO) {
        DetallePedido detallePedido = detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(() -> new DetallePedidosException("No existe el detalle pedido de la id: " + idDetallePedido));

        Pedido pedido = pedidoClientRest.findById(detallePedido.getIdPedido());
        Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());
        Producto producto = productoClientRest.findByIdProducto(detallePedido.getIdProducto());

        List<Inventario> inventarioList = inventarioClientRest.findByIdSucursal(sucursal.getIdSucursal());

        // Cantidades actuales y nuevas
        long cantidadActual = detallePedido.getCantidad();
        long nuevaCantidad = updateDTO.getCantidad();
        long diferencia = nuevaCantidad - cantidadActual; // Puede ser positiva (aumenta) o negativa (reduce)

        for (Inventario inv : inventarioList) {
            if (inv.getIdProducto().equals(producto.getIdProducto())) {

                if (diferencia > 0) {
                    // Quieren más → verificar y descontar stock
                    if (inv.getCantidad() < diferencia) {
                        throw new DetallePedidosException("No hay suficiente stock disponible para el producto con ID: " + producto.getIdProducto());
                    }

                    // Descontar diferencia del inventario
                    inventarioClientRest.updateQuantity(new QuantityUpdateDTO(
                            producto.getIdProducto(),
                            sucursal.getIdSucursal(),
                            -diferencia
                    ));
                } else if (diferencia < 0) {
                    // Quieren menos → devolver diferencia al inventario
                    inventarioClientRest.updateQuantity(new QuantityUpdateDTO(
                            producto.getIdProducto(),
                            sucursal.getIdSucursal(),
                            -diferencia // devolver: -(-x) = +x
                    ));
                }

                // Actualizar el detalle
                detallePedido.setCantidad(nuevaCantidad);
                detallePedido.setSubTotal(nuevaCantidad * producto.getPrecio());
                detallePedidoRepository.save(detallePedido);

                // Devolver respuesta
                return new UpdateCuantidadProductoDetallePedidoResponseDTO(
                        detallePedido.getIdPedido(),
                        usuario.getNombreDelUsuario(),
                        sucursal.getNombreSucursal(),
                        producto.getNombreProducto(),
                        detallePedido.getCantidad(),
                        producto.getPrecio(),
                        detallePedido.getSubTotal()
                );
            }
        }

        throw new DetallePedidosException("No se encontró inventario para el producto con ID: " + producto.getIdProducto());
    }


    @Transactional
    @Override
    public void deleteDetallePedido(Long idPedido) {
        try {
            Pedido pedido = pedidoClientRest.findById(idPedido);
            // si devuelve null, validar también si es necesario
            if (pedido == null) {
                throw new DetallePedidosException("No existe el pedido con ID: " + idPedido);
            }
        } catch (FeignException.NotFound ex) {
            // Captura el 404 de Feign y lanza tu excepción controlada para el handler global
            throw new DetallePedidosException("No existe el pedido con ID: " + idPedido);
        }

        List<DetallePedido> detallePedidos = detallePedidoRepository.findByIdPedido(idPedido);

        if (detallePedidos == null || detallePedidos.isEmpty()) {
            throw new DetallePedidosException("No hay productos en la lista de detalles para eliminar");
        }

        detallePedidoRepository.deleteAll(detallePedidos);
    }



}
