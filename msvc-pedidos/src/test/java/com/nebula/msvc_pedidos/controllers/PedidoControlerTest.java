package com.nebula.msvc_pedidos.controllers;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nebula.msvc_pedidos.clients.DetallePedidoClientRest;
import com.nebula.msvc_pedidos.clients.ProductoClientRest;
import com.nebula.msvc_pedidos.clients.SucursalClientRest;
import com.nebula.msvc_pedidos.clients.UsuarioClientRest;
import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.Producto;
import com.nebula.msvc_pedidos.models.Sucursal;
import com.nebula.msvc_pedidos.models.Usuario;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepository;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedidoControlerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private DetallePedidoClientRest detallePedidoClientRest;

    @MockBean
    private ProductoClientRest productoClientRest;

    @MockBean
    private UsuarioClientRest usuarioClientRest;

    @MockBean
    private SucursalClientRest sucursalClientRest;


    @Test //FindAll()
    public void shouldReturnAllOrdersWhenTheListIsRequested (){
        List<Pedido> mockPedidos = List.of(
                new Pedido(1L, LocalDateTime.parse("2024-10-21T10:15:30"), 1L, 1L),
                new Pedido(2L, LocalDateTime.parse("2024-10-22T11:00:00"), 2L, 1L)
        );

        given(pedidoRepository.findAll()).willReturn(mockPedidos);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/pedido", String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        //Processar el JSON con JsonPath
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        // Verificar que el JSON devuelto contiene 2 elementos
        int ordersCount = documentContext.read("$.length()");
        assertThat(ordersCount).isEqualTo(2); // O mejor: assertThat(ordersCount).isEqualTo(mockPedidos.size());

        // Validar IDs de los pedidos
        JSONArray ids = documentContext.read("$..idPedido");
        assertThat(ids).containsExactlyInAnyOrder(1, 2); // Podemos ser más específicos ahora

        // Validar que los campos no son nulos
        JSONArray idUsuarios = documentContext.read("$..idUsuario");
        assertThat(idUsuarios).doesNotContainNull();

        JSONArray idSucursales = documentContext.read("$..idSucursal");
        assertThat(idSucursales).doesNotContainNull();

        // Validar formato de fecha
        JSONArray fechas = documentContext.read("$..fechaPedido");
        assertThat(fechas).allSatisfy(fecha -> {
            assertThat(fecha).isInstanceOf(String.class);
            assertThat(fecha.toString()).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}");
        });
    }

    @Test //findAllPedidos() | Con Detalles
    public void shouldReturnAllOrdersWithDetailsWhenTheListIsRequested (){
        // 1. Prepara datos simulados
        List<Pedido> pedidosEnBd = List.of(
                new Pedido(1L, LocalDateTime.parse("2025-06-05T20:25:52.275015"), 1L, 1L),
                new Pedido(2L, LocalDateTime.parse("2025-05-31T20:25:52.315906"), 2L, 2L)
        );

        List<DetallePedido> detallesPedido1 = List.of(
                new DetallePedido(1L, 1L, 1L, 2L, 6640.0),
                new DetallePedido(2L, 1L, 2L, 1L, 5000.0)
        );

        List<DetallePedido> detallesPedido2 = List.of(
                new DetallePedido(3L, 2L, 3L, 3L, 3000.0)
        );

        List<DetallePedido> allDetalles = List.copyOf(detallesPedido1); // importante para el .findAll()

        // 2. Mocks necesarios para que no falle el servicio
        given(pedidoRepository.findAll()).willReturn(pedidosEnBd);
        given(detallePedidoClientRest.findAll()).willReturn(allDetalles); // evitar exception inicial

        given(detallePedidoClientRest.findByIdPedido(1L)).willReturn(detallesPedido1);
        given(detallePedidoClientRest.findByIdPedido(2L)).willReturn(detallesPedido2);

        given(productoClientRest.findByIdProducto(1L)).willReturn(new Producto(1L, "Shampoo", 6640.0));
        given(productoClientRest.findByIdProducto(2L)).willReturn(new Producto(2L, "Cepillo", 5000.0));
        given(productoClientRest.findByIdProducto(3L)).willReturn(new Producto(3L, "Jabón", 3000.0));

        given(usuarioClientRest.findByIdUsuario(1L)).willReturn(new Usuario(
                1L, "Solaire", "clave0", "Knight Solaire",
                "Solaire@souls.com", "14061701-7", "3604 Masía Clemente", "+56 904905636"
        ));

        given(usuarioClientRest.findByIdUsuario(2L)).willReturn(new Usuario(
                2L, "Artorias", "clave1", "Knight Artorias",
                "Artorias@souls.com", "16850407-5", "9542 Salida Silvia Cortés", "+56 970010556"
        ));

        given(sucursalClientRest.findByIdSucursal(1L)).willReturn(new Sucursal(
                1L, "Sucursal Lastarria", "Lastarria 1234", "Santiago",
                "Santiago", "Metropolitana", "+56991234567", "lastarria@marketspaeco.cl"
        ));

        given(sucursalClientRest.findByIdSucursal(2L)).willReturn(new Sucursal(
                2L, "Sucursal Valdivia", "Picarte 4567", "Valdivia",
                "Valdivia", "Los Ríos", "+56992345678", "valdivia@marketspaeco.cl"
        ));

        // 3. Ejecutar la llamada real
        String url = "http://localhost:" + port + "/api/v1/pedido/detalle";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 4. Validaciones
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int ordersDetailsCount = documentContext.read("$.length()");
        assertThat(ordersDetailsCount).isEqualTo(pedidosEnBd.size());

        List<String> nombresUsuarios = documentContext.read("$..nombreUsuario");
        assertThat(nombresUsuarios).containsExactlyInAnyOrder("Solaire", "Artorias");
    }

    @Test // findById()
    public void shouldReturnOrderSearchById (){
        // 1. Datos simulados
        Long idPedido = 1L;
        Pedido pedidoSimulado = new Pedido(
                idPedido,
                LocalDateTime.parse("2025-06-05T20:25:52.275015"),
                1L, // idUsuario
                2L  // idSucursal
        );

        // 2. Mockear comportamiento del repositorio
        given(pedidoRepository.findById(idPedido)).willReturn(Optional.of(pedidoSimulado));

        // 3. Ejecutar llamada HTTP real usando TestRestTemplate
        String url = "http://localhost:" + port + "/api/v1/pedido/" + idPedido;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 4. Validar respuesta HTTP
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // 5. Validar contenido del JSON de respuesta
        DocumentContext json = JsonPath.parse(response.getBody());
        Long idDevuelto = json.read("$.idPedido", Long.class);
        Long idUsuarioDevuelto = json.read("$.idUsuario", Long.class);
        Long idSucursalDevuelta = json.read("$.idSucursal", Long.class);
        String fechaDevuelta = json.read("$.fechaPedido");

        assertThat(idDevuelto).isEqualTo(idPedido);
        assertThat(idUsuarioDevuelto).isEqualTo(1L);
        assertThat(idSucursalDevuelta).isEqualTo(2L);
        assertThat(fechaDevuelta).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}.*"); // Validar formato ISO
    }

    @Test
    public void shouldReturnOrderWithDetailsSearchById(){

    }




}
