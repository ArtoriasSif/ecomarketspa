package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.clients.DetallePedidoClientRest;
import com.nebula.msvc_pedidos.clients.ProductoClientRest;
import com.nebula.msvc_pedidos.clients.SucursalClientRest;
import com.nebula.msvc_pedidos.clients.UsuarioClientRest;
import com.nebula.msvc_pedidos.dtos.*;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.Producto;
import com.nebula.msvc_pedidos.models.Sucursal;
import com.nebula.msvc_pedidos.models.Usuario;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepository;
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
    private PedidoRepository pedidoRepository;
    @Autowired
    private UsuarioClientRest usuarioClientRest;
    @Autowired
    private SucursalClientRest sucursalClientRest;
    @Autowired
    private ProductoClientRest productoClientRest;
    @Autowired
    private DetallePedidoClientRest detallePedidoClientRest;


    @Override
    @Transactional
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("Pedido no encontrado")
        );
    }

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

        //Crear el pedido
        Pedido pedido = new Pedido(null,LocalDateTime.now(), pedidoDTO.getIdUsuario(), pedidoDTO.getIdSucursal());
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(usuarioClientRest.findByIdUsuario(pedido.getIdUsuario()).getNombreUsuario(),
                pedido.getIdPedido(), "Cabecera del Pedido registrado exitosamente");

    }

    @Transactional
    @Override
    public PedidoConDetalleDTO findPedidoConDetalles(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoException("No existe el pedido"));

        List<DetallePedido> detalles = detallePedidoClientRest.findByIdPedido(idPedido);
        List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
        for (DetallePedido detallePedido : detalles) {
            Producto producto = productoClientRest.findByIdProducto(detallePedido.getIdProducto());

            DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO(
                    producto.getNombreProducto(),
                    detallePedido.getCantidad(),
                    producto.getPrecio(),
                    producto.getPrecio()*detallePedido.getCantidad()
            );
            detallesDTO.add(detallePedidoDTO);
        }

        Usuario usuario = usuarioClientRest.findByIdUsuario(pedido.getIdUsuario());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(pedido.getIdSucursal());
        Double total = detalles.stream().mapToDouble(DetallePedido::getSubTotal).sum();

        return new PedidoConDetalleDTO(
                usuario.getNombreUsuario(),
                usuario.getRutUsuario(),
                sucursal.getNombreSucursal(),
                detallesDTO,
                total);
    }

    @Transactional
    @Override
    public List<PedidoConDetalleDTO> findAllPedidoConDetalle(){
        if (pedidoRepository.findAll().isEmpty() || detallePedidoClientRest.findAll().isEmpty()) {
            throw new PedidoException("No existe pedidos con detalles");
        }

        List<Pedido> todosPedidos = pedidoRepository.findAll();
        List<PedidoConDetalleDTO> pedidosDTO = new ArrayList<>();

        for (Pedido P : todosPedidos) {
            List<DetallePedidoDTO> detallesDTO = new ArrayList<>();
            List<DetallePedido> detalles = detallePedidoClientRest.findByIdPedido(P.getIdPedido());

            for (DetallePedido D : detalles) {
                Producto producto = productoClientRest.findByIdProducto(D.getIdProducto());

                DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO(
                        producto.getNombreProducto(),
                        D.getCantidad(),
                        producto.getPrecio(),
                        producto.getPrecio() * D.getCantidad()
                );
                detallesDTO.add(detallePedidoDTO);
            }

            Usuario usuario = usuarioClientRest.findByIdUsuario(P.getIdUsuario());
            Sucursal sucursal = sucursalClientRest.findByIdSucursal(P.getIdSucursal());
            Double total = detalles.stream().mapToDouble(DetallePedido::getSubTotal).sum();

            pedidosDTO.add(new PedidoConDetalleDTO(
                    usuario.getNombreUsuario(),
                    usuario.getRutUsuario(),
                    sucursal.getNombreSucursal(),
                    detallesDTO,
                    total
            ));
        }
        return pedidosDTO;
    }


    @Transactional
    @Override
    public Pedido updatePedido (Long id, Pedido pedido){
        Pedido pedidoUpdate = pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("Pedido con la id:"+id+" no encontrado")
        );
        if (pedido.getIdSucursal().equals(pedidoUpdate.getIdSucursal()) &&
            pedido.getIdUsuario().equals(pedidoUpdate.getIdUsuario())){
            throw new PedidoException("Los datos son iguales no hay cambios");
        }
        return pedidoRepository.save(pedidoUpdate);
    }

    @Transactional
    @Override
    public String deletePedidoId(Long id){
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(
                () -> new PedidoException("Pedido no encontrado")
        );
        //Deletar detalles
        detallePedidoClientRest.deleteByIdPedido(pedido.getIdPedido());

        pedidoRepository.delete(pedido);

        return "El pedido con su detalle fue eliminado exitosamente";
    }

    //Deletar pedidos de la sucursal con los detalles
    @Transactional
    @Override
    public void deletarPedidosConDetalles(Long idSucursal){
        sucursalClientRest.findByIdSucursal(idSucursal);
        List<Pedido> listaPedidos = pedidoRepository.findAllByIdSucursal(idSucursal);

        if(listaPedidos.isEmpty()){
            throw new PedidoException("El pedido no existe");
        }
        for (Pedido P: listaPedidos){
            deletePedidoId(P.getIdPedido());
        }
    }
}

