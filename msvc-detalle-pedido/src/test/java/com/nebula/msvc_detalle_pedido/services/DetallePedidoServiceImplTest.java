package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.clients.InventarioClientRest;
import com.nebula.msvc_detalle_pedido.clients.PedidoClientRest;
import com.nebula.msvc_detalle_pedido.clients.ProductoClientRest;
import com.nebula.msvc_detalle_pedido.dtos.QuantityUpdateDTO;
import com.nebula.msvc_detalle_pedido.dtos.UpdateQuantidadProductoPedidoDTO;
import com.nebula.msvc_detalle_pedido.exceptions.DetallePedidosException;
import com.nebula.msvc_detalle_pedido.models.Inventario;
import com.nebula.msvc_detalle_pedido.models.Pedido;
import com.nebula.msvc_detalle_pedido.models.Producto;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import feign.FeignException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetallePedidoServiceImplTest {

    @Mock
    private DetallePedidoRepository detallePedidoRepository;

    @Mock
    private PedidoClientRest pedidoClientRest;

    @Mock
    private ProductoClientRest productoClientRest;

    @Mock
    private InventarioClientRest inventarioClientRest;


    @InjectMocks
    private DetallePedidoServiceImpl detallePedidoService;

    private Faker faker;
    private List<DetallePedido> detalles;

    @BeforeEach
    public void setUp() {
        faker = new Faker(new Locale("es", "CL"));
        detalles = new ArrayList<>();

        for (long i = 1; i <= 5; i++) {
            detalles.add(new DetallePedido(
                    i,
                    100L, // idPedido común
                    faker.number().numberBetween(1L, 100L),
                    faker.number().numberBetween(1L, 5L),
                    faker.number().randomDouble(2, 1000, 5000)
            ));
        }
    }

    //GetMapping con parametro id Pedido, retorna lista de detalles asociados Id Pedido
    @Test
    @DisplayName("Debe retornar los detalles de un pedido por su ID")
    public void debeRetornarDetallesPorIdPedido() {
        Long idPedido = 100L;

        when(detallePedidoRepository.findByIdPedido(idPedido)).thenReturn(detalles);

        List<DetallePedido> resultado = detallePedidoService.findByIdPedido(idPedido);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getIdPedido()).isEqualTo(idPedido);

        verify(detallePedidoRepository, times(1)).findByIdPedido(idPedido);
    }

    //GetMapping retorna todos los detalles de todos los pedidos
    @Test
    @DisplayName("Debe retornar todos los detalles de pedido")
    public void debeRetornarTodosLosDetalles() {
        when(detallePedidoRepository.findAll()).thenReturn(detalles);

        List<DetallePedido> resultado = detallePedidoService.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(5);
        verify(detallePedidoRepository, times(1)).findAll();
    }

    //PostMapping crea detalles asociados a Id pedido
    @Test
    @DisplayName("Debe guardar los detalles del pedido")
    void debeGuardarDetallesCorrectamente() {
        Long idPedido = 100L;
        Long idSucursal = 1L;

        // Crear detalle
        DetallePedido detalle = new DetallePedido(null, idPedido, 10L, 2L, null);
        List<DetallePedido> detalles = List.of(detalle);

        // Mock pedido
        Pedido pedido = new Pedido(100L, LocalDateTime.now(), 1000.0, 1L, idSucursal);
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);

        // Mock producto
        Producto producto = new Producto(10L, "Arroz", 2999.95);
        when(productoClientRest.findByIdProducto(10L)).thenReturn(producto);

        // Mock inventarios
        List<Inventario> inventarios = List.of(new Inventario(10L,10L, idSucursal, 10L));
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);

        // Mock update inventario
        doNothing().when(inventarioClientRest).updateQuantity(any(QuantityUpdateDTO.class));

        // Mock guardar
        when(detallePedidoRepository.saveAll(anyList())).thenReturn(detalles);

        // Ejecutar
        List<DetallePedido> resultado = detallePedidoService.save(detalles);

        // Verificaciones
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getSubTotal()).isEqualTo(2 * 2999.95);
    }

    //PostMapping crea detalles. Lanza Exception cuando no hay detalles de pedidos para guardar
    @Test
    @DisplayName("Debe lanzar excepción si la lista de detalles es null")
    void debeLanzarExcepcionSiListaEsNull() {
        assertThatThrownBy(() -> detallePedidoService.save(null))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No hay productos para guardar");

        verifyNoInteractions(pedidoClientRest);
        verifyNoInteractions(detallePedidoRepository);
    }

    //PostMapping crea detalles. Lanza Exception cuando la lista de detalles de pedidos esta vazia, no permitindo guardar datos
    @Test
    @DisplayName("Debe lanzar excepción si la lista de detalles está vacía")
    void debeLanzarExcepcionSiListaEstaVacia() {
        assertThatThrownBy(() -> detallePedidoService.save(Collections.emptyList()))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No hay productos para guardar");

        verifyNoInteractions(pedidoClientRest);
        verifyNoInteractions(detallePedidoRepository);
    }

    //PostMapping crea detalles. Lanza Exception cuando la Id del pedido no existe
    @Test
    @DisplayName("Debe lanzar excepción si el pedido no existe")
    void debeLanzarExcepcionSiPedidoNoExiste() {
        Long idPedido = 100L;
        List<DetallePedido> detalles = List.of(new DetallePedido(null, idPedido, 10L, 2L, null));

        when(pedidoClientRest.findById(idPedido)).thenThrow(FeignException.NotFound.class);

        assertThatThrownBy(() -> detallePedidoService.save(detalles))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("Pedido con id: 100 no existe");

        verify(pedidoClientRest).findById(idPedido);
        verifyNoInteractions(detallePedidoRepository);
    }

    //PostMapping crea detalles. Lanza Exception cuando la Id del producto no existe o no esta asociado al inventario de la id sucursal
    @Test
    @DisplayName("Debe lanzar excepción si producto no existe en inventario")
    void debeLanzarExcepcionSiProductoNoExisteEnInventario() {
        Long idPedido = 100L;
        Long idSucursal = 1L;
        Long idProducto = 10L;

        List<DetallePedido> detalles = List.of(new DetallePedido(null, idPedido, idProducto, 2L, null));

        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 1000.0, 1L, idSucursal);
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(List.of()); // vacío

        assertThatThrownBy(() -> detallePedidoService.save(detalles))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No existe el producto con id: 10");

        verify(detallePedidoRepository, never()).saveAll(anyList());
    }

    //PutMapping actualiza detalles asociados la id producto
    @Test
    @DisplayName("Debe actualizar la cantidad de producto cuando hay stock")
    void debeActualizarCantidadProductoCuandoHayStockSuficiente() {
        // Datos de prueba
        Long idDetalle = 1L;
        Long idPedido = 100L;
        Long idProducto = 10L;
        Long idSucursal = 1L;

        UpdateQuantidadProductoPedidoDTO updateDTO = new UpdateQuantidadProductoPedidoDTO();
        updateDTO.setCantidad(2L);

        // DetallePedido inicial con cantidad 1 y subtotal 1000
        DetallePedido detallePedido = new DetallePedido(idDetalle, idPedido, idProducto, 1L, 1000.0);

        // Pedido asociado al detalle con idSucursal = 1
        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 5000.0, 1L, idSucursal);

        // Inventario en sucursal con suficiente stock (5 unidades)
        List<Inventario> inventarios = List.of(new Inventario(10L, idProducto, idSucursal, 5L));

        // Producto con precio 500
        Producto producto = new Producto(idProducto, "Arroz", 500.0);

        // Mockeos
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.of(detallePedido));
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);
        when(productoClientRest.findByIdProducto(idProducto)).thenReturn(producto);
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(inventarioClientRest).updateQuantity(any(QuantityUpdateDTO.class));

        // Ejecución del método
        String resultado = detallePedidoService.updateCatidadProductoPedido(idDetalle, updateDTO);

        // Validaciones
        assertThat(resultado).contains("cantidad=3");    // Cantidad actualizada: 1 + 2 = 3
        assertThat(resultado).contains("subTotal=1500.0"); // subtotal actualizado: 3 * 500

        verify(inventarioClientRest).updateQuantity(any(QuantityUpdateDTO.class));
        verify(detallePedidoRepository).save(any(DetallePedido.class));
    }

    //PutMapping actualiza detalles. Lanza Exception si intenta actualizar detalles de un ID que no existe
    @Test
    @DisplayName("Debe lanzar excepción si no existe el detalle del pedido")
    void debeLanzarExcepcionSiNoExisteDetalle() {
        // ID que no existe
        Long idDetalle = 999L;

        // DTO con cualquier cantidad
        UpdateQuantidadProductoPedidoDTO dto = new UpdateQuantidadProductoPedidoDTO();
        dto.setCantidad(2L);

        // Simula que no se encuentra el detalle
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.empty());

        // Ejecutar y validar excepción
        assertThatThrownBy(() -> detallePedidoService.updateCatidadProductoPedido(idDetalle, dto))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No existe el producto de id: " + idDetalle);

        // Verifica que no se llamaron otros servicios
        verifyNoInteractions(pedidoClientRest);
        verifyNoInteractions(inventarioClientRest);
        verifyNoInteractions(productoClientRest);
        verify(detallePedidoRepository, never()).save(any());
    }

    //PutMapping actualiza detalles. Lanza Exception cuando no hay cantidad de stock
    @Test
    @DisplayName("Debe lanzar excepción cuando no hay suficiente stock")
    void debeLanzarExcepcionCuandoNoHayStockSuficiente() {
        // Datos de prueba
        Long idDetalle = 1L;
        Long idPedido = 100L;
        Long idProducto = 10L;
        Long idSucursal = 1L;

        UpdateQuantidadProductoPedidoDTO updateDTO = new UpdateQuantidadProductoPedidoDTO();
        updateDTO.setCantidad(5L); // Se quiere agregar 5

        // DetallePedido actual tiene cantidad 1 y subtotal 1000
        DetallePedido detallePedido = new DetallePedido(idDetalle, idPedido, idProducto, 1L, 1000.0);

        // Pedido con sucursal
        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 5000.0, 1L, idSucursal);

        // Inventario con stock insuficiente (solo 2 unidades disponibles)
        List<Inventario> inventarios = List.of(new Inventario(10L, idProducto, idSucursal, 2L));

        // Mocks
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.of(detallePedido));
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);

        // Verificar excepción
        assertThatThrownBy(() -> detallePedidoService.updateCatidadProductoPedido(idDetalle, updateDTO))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No hay suficientes stock del producto con id: " + idProducto);

        // Verificar que **no** se llamó a updateQuantity ni a save
        verify(inventarioClientRest, never()).updateQuantity(any());
        verify(detallePedidoRepository, never()).save(any());
    }


    //DeleteMapping detalles de pedido asociado a una ID. El msvc Pedido accede el metodo por client al eliminar pedido.
    @Test
    @DisplayName("Debe eliminar detalles asociado a una id pedido")
    void debeEliminarDetallesCuandoExisten() {
        Long idPedido = 100L;

        // Simulamos que hay detalles asociados a ese pedido
        List<DetallePedido> detalles = List.of(
                new DetallePedido(1L, idPedido, 10L, 2L, 1000.0),
                new DetallePedido(2L, idPedido, 20L, 1L, 500.0)
        );

        when(detallePedidoRepository.findByIdPedido(idPedido)).thenReturn(detalles);

        // Ejecutamos el método
        detallePedidoService.deleteDetallePedido(idPedido);

        // Verificamos que se llamó a deleteAll con la lista
        verify(detallePedidoRepository).deleteAll(detalles);
    }

    //DeleteMapping detalles de pedido asociado a una ID. Lanza Exception cuando no hay detalles de pedidos para eliminar
    @Test
    @DisplayName("Debe lanzar exception al eliminar detalles asociado a una id pedido")
    void debeLanzarExcepcionCuandoNoHayDetallesParaEliminar() {
        Long idPedido = 100L;

        // Simulamos que no hay detalles para ese pedido
        when(detallePedidoRepository.findByIdPedido(idPedido)).thenReturn(Collections.emptyList());

        // Ejecutamos y verificamos que lance excepción
        assertThrows(DetallePedidosException.class, () -> {
            detallePedidoService.deleteDetallePedido(idPedido);
        });
    }


}

