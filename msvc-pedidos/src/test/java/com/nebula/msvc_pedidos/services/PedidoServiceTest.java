package com.nebula.msvc_pedidos.services;

import com.nebula.msvc_pedidos.clients.DetallePedidoClientRest;
import com.nebula.msvc_pedidos.clients.ProductoClientRest;
import com.nebula.msvc_pedidos.clients.SucursalClientRest;
import com.nebula.msvc_pedidos.clients.UsuarioClientRest;
import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import com.nebula.msvc_pedidos.dtos.PedidoDTO;
import com.nebula.msvc_pedidos.dtos.PedidoResponseDTO;
import com.nebula.msvc_pedidos.exceptions.PedidoException;
import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.Producto;
import com.nebula.msvc_pedidos.models.Sucursal;
import com.nebula.msvc_pedidos.models.Usuario;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
    @Mock
    private DetallePedidoClientRest detallePedidoClientRest;

    @Mock
    private UsuarioClientRest usuarioClientRest;

    @Mock
    private SucursalClientRest sucursalClientRest;

    @Mock
    private ProductoClientRest productoClientRest;

    @Mock
    private PedidoRepository pedidoRepository;


    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedidoPrueba;

    private List<Pedido> pedidos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker();
        pedidoPrueba = new Pedido(
               1L, LocalDateTime.now(),  faker.number().numberBetween(1L, 100L),  faker.number().numberBetween(1L, 100L)
        );
        for (long i = 2; i <= 100; i++) {
            pedidos.add(new Pedido(i, LocalDateTime.now(), faker.number().numberBetween(1L, 100L),  faker.number().numberBetween(1L, 100L)));
        }
    }

    //GetMapping listar todos los pedidos
    @Test
    @DisplayName("Debo listar todos los pedidos")
    public void deboListarTodosLosPedidos() {
        List<Pedido> pedidos = this.pedidos;
        pedidos.add(pedidoPrueba);
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.findAll();

        assertThat(resultado).hasSize(10);
        assertThat(resultado).contains(pedidoPrueba);

        verify(pedidoRepository, times(1)).findAll();
    }

    //GetMapping Listar un pedido
    @Test
    @DisplayName("Debo listar un pedido")
    public void deboListarUnMedico() {
        when(pedidoRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(pedidoPrueba));

        Pedido resultado = pedidoService.findById(Long.valueOf(1));
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(pedidoPrueba);
        verify(pedidoRepository, times(1)).findById(Long.valueOf(1));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el pedido no existe al buscar por ID")
    public void debeLanzarExcepcionCuandoPedidoNoExistePorId() {
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.findById(idInexistente))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("Pedido no encontrado");

        verify(pedidoRepository, times(1)).findById(idInexistente);
    }

    //PostMapping guardar pedido
    @Test
    @DisplayName("Debe guardar un pedido")
    public void debeGuardarUnPedido() {

        Faker faker = new Faker();

        PedidoDTO pedidoDTO = new PedidoDTO(1L, 1L);

        // Crear un usuario fake
        Usuario usuarioFake = new Usuario();
        usuarioFake.setNombreUsuario(faker.name().fullName());

        // Crear una sucursal fake
        Sucursal sucursalFake = new Sucursal();
        sucursalFake.setIdSucursal(pedidoDTO.getIdSucursal());
        // puedes agregar más atributos si los usas

        // Mocks para los clientes
        when(usuarioClientRest.findByIdUsuario(anyLong())).thenReturn(usuarioFake);
        when(sucursalClientRest.findByIdSucursal(anyLong())).thenReturn(sucursalFake);

        // Mock para guardar pedido
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoGuardado = invocation.getArgument(0);
            return new Pedido(1L, LocalDateTime.now(), pedidoGuardado.getIdUsuario(), pedidoGuardado.getIdSucursal());
        });

        PedidoResponseDTO result = pedidoService.save(pedidoDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNombreUsuario()).isNotEmpty();
        assertThat(result.getMensaje()).contains("registrado exitosamente");

        verify(pedidoRepository, times(1)).save(any(Pedido.class));

    }

    @Test
    @DisplayName("Debe actualizar un pedido correctamente")
    public void debeActualizarPedido() {
        Long id = 1L;

        // Pedido actual en repositorio
        Pedido pedidoExistente = pedidos.get(0);

        // Pedido con cambios para actualizar
        Pedido pedidoActualizado = new Pedido(id, LocalDateTime.now(), 2L, 2L);

        // Mock: findById devuelve el pedido existente
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedidoExistente));
        // Mock: save devuelve el pedido actualizado (simulación)
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Llamada al metodo bajo prueba
        Pedido resultado = pedidoService.updatePedido(id, pedidoActualizado);

        // Verificaciones
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdUsuario()).isEqualTo(pedidoExistente.getIdUsuario());
        assertThat(resultado.getIdSucursal()).isEqualTo(pedidoExistente.getIdSucursal());

        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, times(1)).save(pedidoExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el pedido a actualizar no existe")
    public void debeLanzarExcepcionCuandoPedidoNoExiste() {
        Long idInexistente = 999L;
        Pedido pedido = new Pedido(idInexistente, LocalDateTime.now(), 2L, 2L);

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.updatePedido(idInexistente, pedido))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("Pedido con la id:" + idInexistente + " no encontrado");

        verify(pedidoRepository, times(1)).findById(idInexistente);
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando los datos del pedido son iguales (sin cambios)")
    public void debeLanzarExcepcionCuandoDatosIguales() {
        Long id = 1L;
        Pedido pedidoExistente = new Pedido(id, LocalDateTime.now(), 1L, 1L);
        Pedido pedidoSinCambios = new Pedido(id, LocalDateTime.now(), 1L, 1L);

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedidoExistente));

        assertThatThrownBy(() -> pedidoService.updatePedido(id, pedidoSinCambios))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("Los datos son iguales no hay cambios");

        verify(pedidoRepository, times(1)).findById(id);
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un pedido por id correctamente")
    public void debeEliminarPedidoPorId() {
        Long id = 1L;

        Pedido pedido = new Pedido(id, LocalDateTime.now(), 1L, 1L);

        // Mock: cuando se busca el pedido por id, retorna el pedido
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        // No necesitamos mockear el void delete, solo verificar que se llama
        doNothing().when(detallePedidoClientRest).deleteByIdPedido(id);
        doNothing().when(pedidoRepository).delete(pedido);

        String resultado = pedidoService.deletePedidoId(id);

        assertThat(resultado).isEqualTo("El pedido con su detalle fue eliminado exitosamente");

        verify(pedidoRepository, times(1)).findById(id);
        verify(detallePedidoClientRest, times(1)).deleteByIdPedido(id);
        verify(pedidoRepository, times(1)).delete(pedido);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no encuentra el pedido para eliminar")
    public void debeLanzarExcepcionCuandoNoExistePedidoParaEliminar() {
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.deletePedidoId(idInexistente))
                .isInstanceOf(PedidoException.class)
                .hasMessageContaining("Pedido no encontrado");

        verify(pedidoRepository, times(1)).findById(idInexistente);
        verify(detallePedidoClientRest, never()).deleteByIdPedido(anyLong());
        verify(pedidoRepository, never()).delete(any(Pedido.class));
    }

    //GetMapping que esta implementado para traer lista de msvc-detalles
    @Test
    @DisplayName("Debe obtener un pedido con detalles correctamente")
    public void debeObtenerPedidoConDetalles() {
        Long idPedido = 1L;

        // Pedido fake
        Pedido pedidoFake = new Pedido(idPedido, LocalDateTime.now(), 10L, 20L);

        // Lista de detalles fake
        List<DetallePedido> detallesFake = List.of(
                new DetallePedido(100L, idPedido, 200L, 2L, 100.0), // 2 * 50.0 = 100.0
                new DetallePedido(101L, idPedido, 201L, 1L, 100.0)  // 1 * 100.0 = 100.0
        );

        // Productos fake
        Producto producto1 = new Producto(200L, "Producto A", 50.0);
        Producto producto2 = new Producto(201L, "Producto B", 100.0);

        // Usuario fake
        Usuario usuarioFake = new Usuario();
        usuarioFake.setNombreUsuario("Usuario Test");
        usuarioFake.setRutUsuario("12345678-9");

        // Sucursal fake
        Sucursal sucursalFake = new Sucursal();
        sucursalFake.setNombreSucursal("Sucursal Test");

        // Mocks configurados
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoFake));
        when(detallePedidoClientRest.findByIdPedido(idPedido)).thenReturn(detallesFake);
        when(productoClientRest.findByIdProducto(200L)).thenReturn(producto1);
        when(productoClientRest.findByIdProducto(201L)).thenReturn(producto2);
        when(usuarioClientRest.findByIdUsuario(10L)).thenReturn(usuarioFake);
        when(sucursalClientRest.findByIdSucursal(20L)).thenReturn(sucursalFake);

        // Ejecutar el metodo bajo prueba
        PedidoConDetalleDTO resultado = pedidoService.findPedidoConDetalles(idPedido);

        // Validaciones
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreUsuario()).isEqualTo("Usuario Test");
        assertThat(resultado.getRutUsuario()).isEqualTo("12345678-9");
        assertThat(resultado.getNombreSucursal()).isEqualTo("Sucursal Test");
        assertThat(resultado.getDetalles()).hasSize(2);

        // Validar detalles
        assertThat(resultado.getDetalles().get(0).getNombreProducto()).isEqualTo("Producto A");
        assertThat(resultado.getDetalles().get(0).getCantidad()).isEqualTo(2);
        assertThat(resultado.getDetalles().get(0).getPrecioUnitario()).isEqualTo(50.0);
        assertThat(resultado.getDetalles().get(0).getSubTotal()).isEqualTo(100.0);

        assertThat(resultado.getDetalles().get(1).getNombreProducto()).isEqualTo("Producto B");
        assertThat(resultado.getDetalles().get(1).getCantidad()).isEqualTo(1);
        assertThat(resultado.getDetalles().get(1).getPrecioUnitario()).isEqualTo(100.0);
        assertThat(resultado.getDetalles().get(1).getSubTotal()).isEqualTo(100.0);

        // Validar total
        assertThat(resultado.getTotal()).isEqualTo(200.0); // 100 + 100

        // Verificar invocaciones
        verify(pedidoRepository, times(1)).findById(idPedido);
        verify(detallePedidoClientRest, times(1)).findByIdPedido(idPedido);
        verify(productoClientRest, times(1)).findByIdProducto(200L);
        verify(productoClientRest, times(1)).findByIdProducto(201L);
        verify(usuarioClientRest, times(1)).findByIdUsuario(10L);
        verify(sucursalClientRest, times(1)).findByIdSucursal(20L);
    }


}
