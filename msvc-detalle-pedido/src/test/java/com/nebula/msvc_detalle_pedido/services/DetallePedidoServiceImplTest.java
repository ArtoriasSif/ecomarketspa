package com.nebula.msvc_detalle_pedido.services;

import com.nebula.msvc_detalle_pedido.clients.*;
import com.nebula.msvc_detalle_pedido.dtos.*;
import com.nebula.msvc_detalle_pedido.exceptions.DetallePedidosException;
import com.nebula.msvc_detalle_pedido.models.*;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import feign.FeignException;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private UsuarioClientRest usuarioClientRest;

    @Mock
    private SucursalClientRest sucursalClientRest;


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

    @Test
    @DisplayName("Debe retornar los detalles DTOS de un pedido por su ID")
    void findDetailsByIdPedido_retornarListaDTOs() {
        // Arrange
        Long idPedido = 1L;

        DetallePedido detalle = new DetallePedido();
        detalle.setIdDetallePedido(10L);
        detalle.setIdPedido(idPedido);
        detalle.setIdProducto(99L);
        detalle.setCantidad(2L);
        detalle.setSubTotal(200.0);

        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        pedido.setIdUsuario(5L);
        pedido.setIdSucursal(7L);

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("Matheus");

        Sucursal sucursal = new Sucursal();
        sucursal.setNombreSucursal("Central");

        Producto producto = new Producto();
        producto.setNombreProducto("Laptop");
        producto.setPrecio(100.0);

        when(detallePedidoRepository.findByIdPedido(idPedido)).thenReturn(List.of(detalle));
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);
        when(usuarioClientRest.findByIdUsuario(5L)).thenReturn(usuario);
        when(sucursalClientRest.findByIdSucursal(7L)).thenReturn(sucursal);
        when(productoClientRest.findByIdProducto(99L)).thenReturn(producto);

        // Act
        List<DetallePedidoResponseDTO> result = detallePedidoService.findDetailsByIdPedido(idPedido);

        // Assert
        assertEquals(1, result.size());
        DetallePedidoResponseDTO dto = result.get(0);
        assertEquals(10L, dto.getIdDetallePedido());
        assertEquals("Laptop", dto.getNombreProducto());
        assertEquals("Matheus", dto.getNombreUsuario());
        assertEquals("Central", dto.getNombreSucursal());
        assertEquals(100.0, dto.getPrecioUnitario());
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

    @Test
    @DisplayName("Debe guardar los detalles del pedido correctamente desde DTO")
    void debeGuardarDetallesDesdeDTOCorrectamente() {
        Long idPedido = 100L;
        Long idSucursal = 1L;
        Long idUsuario = 99L;
        Long idProducto = 10L;
        Long cantidad = 2L;
        double precio = 2999.95;

        // Crear DTO de entrada (como en Postman)
        DetallePedidoRequestDTO dto = new DetallePedidoRequestDTO(idPedido, idProducto, cantidad);
        List<DetallePedidoRequestDTO> listaDto = List.of(dto);

        // Mock pedido
        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 1000.0, idUsuario, idSucursal);
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);

        // Mock usuario
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreDelUsuario("Juan Pérez");
        when(usuarioClientRest.findByIdUsuario(idUsuario)).thenReturn(usuario);

        // Mock sucursal
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(idSucursal);
        sucursal.setNombreSucursal("Sucursal Centro");
        when(sucursalClientRest.findByIdSucursal(idSucursal)).thenReturn(sucursal);

        // Mock producto
        Producto producto = new Producto(idProducto, "Arroz", precio);
        when(productoClientRest.findByIdProducto(idProducto)).thenReturn(producto);

        // Mock inventario
        List<Inventario> inventarios = List.of(new Inventario(10L, idProducto, idSucursal, 10L));
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);

        // Mock actualización de inventario
        doNothing().when(inventarioClientRest).updateQuantity(any(QuantityUpdateDTO.class));

        // Mock guardado
        DetallePedido detalleGuardado = new DetallePedido(1L, idPedido, idProducto, cantidad, cantidad * precio);
        when(detallePedidoRepository.saveAll(anyList())).thenReturn(List.of(detalleGuardado));

        // Ejecutar
        List<DetallePedidoResponseDTO> resultado = detallePedidoService.save(listaDto);

        // Verificaciones
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);

        DetallePedidoResponseDTO dtoRespuesta = resultado.get(0);
        assertThat(dtoRespuesta.getIdDetallePedido()).isEqualTo(1L);
        assertThat(dtoRespuesta.getIdPedido()).isEqualTo(idPedido);
        assertThat(dtoRespuesta.getCantidad()).isEqualTo(cantidad);
        assertThat(dtoRespuesta.getPrecioUnitario()).isEqualTo(precio);
        assertThat(dtoRespuesta.getSubTotal()).isEqualTo(precio * cantidad);
        assertThat(dtoRespuesta.getNombreProducto()).isEqualTo("Arroz");
        assertThat(dtoRespuesta.getNombreUsuario()).isEqualTo("Juan Pérez");
        assertThat(dtoRespuesta.getNombreSucursal()).isEqualTo("Sucursal Centro");

        // Verifica llamada a updateQuantity
        ArgumentCaptor<QuantityUpdateDTO> captor = ArgumentCaptor.forClass(QuantityUpdateDTO.class);
        verify(inventarioClientRest).updateQuantity(captor.capture());
        QuantityUpdateDTO actualUpdate = captor.getValue();
        assertThat(actualUpdate.getCantidad()).isEqualTo(-cantidad);

        // Verifica que guardó los detalles
        verify(detallePedidoRepository).saveAll(anyList());
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

    @Test
    @DisplayName("Debe lanzar excepción si el pedido no existe")
    void debeLanzarExcepcionSiPedidoNoExiste() {
        Long idPedido = 100L;
        Long idProducto = 10L;

        // DTO de entrada simulada (como desde Postman)
        DetallePedidoRequestDTO dto = new DetallePedidoRequestDTO(idPedido, idProducto, 2L);
        List<DetallePedidoRequestDTO> detallesDto = List.of(dto);

        // Simular que el pedido no existe (Feign lanza excepción)
        when(pedidoClientRest.findById(idPedido)).thenThrow(FeignException.NotFound.class);

        // Ejecutar y verificar que lanza la excepción esperada
        assertThatThrownBy(() -> detallePedidoService.save(detallesDto))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("Pedido con id: 100 no existe");

        // Verifica que se intentó buscar el pedido
        verify(pedidoClientRest).findById(idPedido);

        // Asegura que NO se intentó guardar nada
        verifyNoInteractions(detallePedidoRepository);
    }


    @Test
    @DisplayName("Debe lanzar excepción si producto no existe en inventario")
    void debeLanzarExcepcionSiProductoNoExisteEnInventario() {
        Long idPedido = 100L;
        Long idSucursal = 1L;
        Long idProducto = 10L;

        // DTO simulado que llega desde Postman
        DetallePedidoRequestDTO dto = new DetallePedidoRequestDTO(idPedido, idProducto, 2L);
        List<DetallePedidoRequestDTO> detallesDto = List.of(dto);

        // Mock: el pedido existe
        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 1000.0, 1L, idSucursal);
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);

        // Mock: inventario vacío, el producto no está registrado en esta sucursal
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(List.of());

        // Verificación: se lanza excepción porque el producto no está en inventario
        assertThatThrownBy(() -> detallePedidoService.save(detallesDto))
                .isInstanceOf(DetallePedidosException.class)
                .hasMessageContaining("No existe el producto con id: 10");

        // Verifica que no se intentó guardar nada
        verify(detallePedidoRepository, never()).saveAll(anyList());
    }


    @Test
    @DisplayName("Debe actualizar la cantidad de producto cuando hay stock suficiente (caso de aumento)")
    void debeActualizarCantidadProductoCuandoHayStockSuficiente() {
        // Datos de prueba
        Long idDetalle = 1L;
        Long idPedido = 100L;
        Long idProducto = 10L;
        Long idSucursal = 1L;
        Long idUsuario = 99L;

        UpdateCuantidadProductoDetallePedidoDTO updateDTO = new UpdateCuantidadProductoDetallePedidoDTO();
        updateDTO.setCantidad(3L); // quiere aumentar de 1 a 3

        // Entidad original con cantidad = 1
        DetallePedido detallePedido = new DetallePedido(idDetalle, idPedido, idProducto, 1L, 500.0); // subtotal actual 500

        Pedido pedido = new Pedido(idPedido, LocalDateTime.now(), 5000.0, idUsuario, idSucursal);

        // Faker para datos ficticios
        Faker faker = new Faker(new Locale("es"));

        Usuario usuario = new Usuario(
                idUsuario,
                faker.name().username(),
                faker.internet().password(),
                "Juan Perez",
                faker.internet().emailAddress(),
                faker.idNumber().valid(),
                faker.address().fullAddress(),
                faker.phoneNumber().phoneNumber()
        );

        Sucursal sucursal = new Sucursal(
                idSucursal,
                "Sucursal Centro",
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().stateAbbr(),
                faker.phoneNumber().cellPhone(),
                faker.internet().emailAddress()
        );

        Producto producto = new Producto(idProducto, "Arroz", 500.0);

        // Inventario con suficiente stock (5 unidades)
        List<Inventario> inventarios = List.of(new Inventario(10L, idProducto, idSucursal, 5L));

        // Mockeos
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.of(detallePedido));
        when(pedidoClientRest.findById(idPedido)).thenReturn(pedido);
        when(usuarioClientRest.findByIdUsuario(idUsuario)).thenReturn(usuario);
        when(sucursalClientRest.findByIdSucursal(idSucursal)).thenReturn(sucursal);
        when(productoClientRest.findByIdProducto(idProducto)).thenReturn(producto);
        when(inventarioClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenAnswer(invoc -> invoc.getArgument(0));
        doNothing().when(inventarioClientRest).updateQuantity(any(QuantityUpdateDTO.class));

        // Ejecución del método
        UpdateCuantidadProductoDetallePedidoResponseDTO resultado = detallePedidoService
                .updateCantidadProductoPedido(idDetalle, updateDTO);

        // Verificaciones
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(3L); // actualizado correctamente
        assertThat(resultado.getSubTotal()).isEqualTo(1500.0); // 3 * 500
        assertThat(resultado.getNombreProducto()).isEqualTo("Arroz");
        assertThat(resultado.getNombreSucursal()).isEqualTo("Sucursal Centro");
        assertThat(resultado.getNombreUsuario()).isEqualTo("Juan Perez");

        // Captura la llamada a updateQuantity para verificar la diferencia
        ArgumentCaptor<QuantityUpdateDTO> captor = ArgumentCaptor.forClass(QuantityUpdateDTO.class);
        verify(inventarioClientRest).updateQuantity(captor.capture());
        QuantityUpdateDTO update = captor.getValue();
        assertThat(update.getCantidad()).isEqualTo(-2L); // diferencia entre 3 nueva y 1 actual


        // Verifica que se guardó el detalle actualizado
        verify(detallePedidoRepository).save(any(DetallePedido.class));
    }



    //PutMapping actualiza detalles. Lanza Exception si intenta actualizar detalles de un ID que no existe
    @Test
    @DisplayName("Debe lanzar excepción si no existe el detalle del pedido")
    void debeLanzarExcepcionSiNoExisteDetalle() {
        // ID que no existe
        Long idDetalle = 999L;

        // DTO con cualquier cantidad
        UpdateCuantidadProductoDetallePedidoDTO dto = new UpdateCuantidadProductoDetallePedidoDTO();
        dto.setCantidad(2L);

        // Simula que no se encuentra el detalle
        when(detallePedidoRepository.findById(idDetalle)).thenReturn(Optional.empty());

        // Ejecutar y validar excepción
        assertThatThrownBy(() -> detallePedidoService.updateCantidadProductoPedido(idDetalle, dto))
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

        UpdateCuantidadProductoDetallePedidoDTO updateDTO = new UpdateCuantidadProductoDetallePedidoDTO();
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
        assertThatThrownBy(() -> detallePedidoService.updateCantidadProductoPedido(idDetalle, updateDTO))
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

