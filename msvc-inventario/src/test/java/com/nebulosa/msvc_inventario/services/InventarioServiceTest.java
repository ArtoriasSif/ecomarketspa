package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.clients.ProductoClientRest;
import com.nebulosa.msvc_inventario.clients.SucursalClientRest;
import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import com.nebulosa.msvc_inventario.models.Product;
import com.nebulosa.msvc_inventario.models.Sucursal;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductoClientRest productoClientRest ;

    @Mock
    private SucursalClientRest sucursalClientRest;


    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private List<Inventory> inventarios;

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker();
        inventarios = new ArrayList<>();

        for (long i = 1; i <= 50; i++) {
            inventarios.add(new Inventory(
                    i, // idInventario
                    faker.number().numberBetween(1L, 100L), // idProducto
                    faker.number().numberBetween(1L, 10L),  // idSucursal
                    faker.number().numberBetween(1L, 200L)  // cantidad
            ));
        }
    }

    @Test
    @DisplayName("Debe retornar todos los inventarios (50)")
    void debeRetornarTodosLosInventarios() {
        // Mock
        when(inventoryRepository.findAll()).thenReturn(inventarios);

        // Ejecutar
        List<Inventory> resultado = inventoryService.findAll();

        // Verificar
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(50);
        assertThat(resultado).containsAll(inventarios);

        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar inventario desde la lista cuando existe")
    void debeRetornarInventarioDesdeLista() {
        Inventory inventario = inventarios.get(0); // el primero de la lista
        Long id = inventario.getIdInventario();

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventario));

        Inventory resultado = inventoryService.findById(id);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdInventario()).isEqualTo(id);

        verify(inventoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el inventario no existe en la lista")
    void debeLanzarExcepcionSiNoExisteInventario() {
        Long idInexistente = 999L; // asegurarse que no esté en la lista

        when(inventoryRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.findById(idInexistente))
                .isInstanceOf(InventoryException.class)
                .hasMessageContaining("No se encontró el inventario con id: " + idInexistente);

        verify(inventoryRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Debe retornar inventarios por idSucursal")
    void debeRetornarInventariosPorIdSucursal() {
        Long idSucursal = inventarios.get(0).getIdSucursal(); // usar una sucursal existente

        List<Inventory> inventariosSucursal = inventarios.stream()
                .filter(i -> i.getIdSucursal().equals(idSucursal))
                .collect(Collectors.toList());

        when(inventoryRepository.findByIdSucursal(idSucursal)).thenReturn(inventariosSucursal);

        List<Inventory> resultado = inventoryService.findByIdSucursal(idSucursal);

        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(inventariosSucursal.size());
        assertThat(resultado).allMatch(i -> i.getIdSucursal().equals(idSucursal));

        verify(inventoryRepository).findByIdSucursal(idSucursal);
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay inventarios para la sucursal")
    void debeRetornarListaVaciaSiNoHayInventarios() {
        Long idSucursalInexistente = 999L;

        when(inventoryRepository.findByIdSucursal(idSucursalInexistente)).thenReturn(Collections.emptyList());

        List<Inventory> resultado = inventoryService.findByIdSucursal(idSucursalInexistente);

        assertThat(resultado).isEmpty();

        verify(inventoryRepository).findByIdSucursal(idSucursalInexistente);
    }

    @Test
    @DisplayName("Debe retornar el inventario por producto y sucursal si existe")
    void debeRetornarInventarioSiExisteIdProductoyIdSucursal() {
        Inventory inventario = inventarios.get(0); // usa uno de la lista generada
        Long productoId = inventario.getIdProducto();
        Long sucursalId = inventario.getIdSucursal();

        when(inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId))
                .thenReturn(Optional.of(inventario));

        Inventory resultado = inventoryService.findByIdProductoAndIdSucursal(productoId, sucursalId);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdProducto()).isEqualTo(productoId);
        assertThat(resultado.getIdSucursal()).isEqualTo(sucursalId);

        verify(inventoryRepository).findByIdProductoAndIdSucursal(productoId, sucursalId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no se encuentra el inventario para producto y sucursal")
    void debeLanzarExcepcionSiNoExisteInventarioIdProductoyIdSucursal() {
        Long productoId = 999L;
        Long sucursalId = 888L;

        when(inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                inventoryService.findByIdProductoAndIdSucursal(productoId, sucursalId)
        )
                .isInstanceOf(InventoryException.class)
                .hasMessageContaining("No se encontró inventario para el producto " + productoId + " en la sucursal " + sucursalId);

        verify(inventoryRepository).findByIdProductoAndIdSucursal(productoId, sucursalId);
    }

    @Test
    @DisplayName("Debe guardar inventario correctamente cuando no hay excepciones")
    void debeGuardarInventarioCorrectamente() {
        // Datos de prueba
        Inventory inventario = new Inventory(null, 10L, 1L, 100L);

        // Mock para producto válido
        when(productoClientRest.findByIdProducto(inventario.getIdProducto()))
                .thenReturn(new Product(inventario.getIdProducto(), "Producto Test", 1000.0));

        Faker faker = new Faker(new Locale("es", "CL")); // Faker con locale Chile para nombres y direcciones realistas

        Sucursal sucursalFake = new Sucursal();
        sucursalFake.setIdSucursal(faker.number().numberBetween(1L, 100L));
        sucursalFake.setNombreSucursal(faker.company().name());

// Para dirección tipo "Calle Ejemplo 1234"
        String calle = faker.address().streetName(); // Ejemplo: "Av. Las Condes"
        int numero = faker.number().numberBetween(1, 9999);
        sucursalFake.setDireccionSucursal(calle + " " + numero);

// Ciudad, Provincia y Región solo letras, sin números ni símbolos
        sucursalFake.setCiudadSucursal(faker.address().cityName()); // Ejemplo: "Santiago"
        sucursalFake.setProvinciaSucursal(faker.address().state()); // Ejemplo: "Metropolitana"
        sucursalFake.setRegionSucursal(faker.address().state()); // Usa también estado o región

// Teléfono Chile +569 seguido de 8 dígitos
        String telefono = "+569" + faker.number().digits(8);
        sucursalFake.setTelefonoSucursal(telefono);

// Email válido
        String emailSeguro = faker.name().firstName().toLowerCase() + "@gmail.com";
        sucursalFake.setEmailSucursal(emailSeguro);

        // Mock para que no exista inventario previo
        when(inventoryRepository.findByIdProductoAndIdSucursal(inventario.getIdProducto(), inventario.getIdSucursal()))
                .thenReturn(Optional.empty());

        // Mock para guardar inventario (simula asignar ID al guardar)
        Inventory inventarioGuardado = new Inventory(1L, inventario.getIdProducto(), inventario.getIdSucursal(), inventario.getCantidad());
        when(inventoryRepository.save(inventario)).thenReturn(inventarioGuardado);

        // Ejecutar método a testear
        Inventory resultado = inventoryService.save(inventario);

        // Validaciones
        assertThat(resultado.getIdInventario()).isEqualTo(1L);
        assertThat(resultado.getIdProducto()).isEqualTo(inventario.getIdProducto());
        assertThat(resultado.getCantidad()).isEqualTo(inventario.getCantidad());

        // Verificar que los mocks se llamaron correctamente
        verify(productoClientRest).findByIdProducto(inventario.getIdProducto());
        verify(sucursalClientRest).findByIdSucursal(inventario.getIdSucursal());
        verify(inventoryRepository).findByIdProductoAndIdSucursal(inventario.getIdProducto(), inventario.getIdSucursal());
        verify(inventoryRepository).save(inventario);
    }

    @Test
    @DisplayName("Debe actualizar correctamente la cantidad del inventario")
    void debeActualizarCantidadCorrectamente() {
        Long productoId = 10L;
        Long sucursalId = 1L;
        Long cantidadActual = 5L;
        Long cantidadAActualizar = -2L;

        Inventory inventario = new Inventory(1L, productoId, sucursalId, cantidadActual);

        when(inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId))
                .thenReturn(Optional.of(inventario));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> inv.getArgument(0));

        Inventory actualizado = inventoryService.updateQuantity(productoId, sucursalId, cantidadAActualizar);

        assertThat(actualizado).isNotNull();
        assertThat(actualizado.getCantidad()).isEqualTo(3L); // 5 - 2
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si cantidad final es negativa")
    void debeLanzarExcepcionSiCantidadFinalEsNegativa() {
        Long productoId = 10L;
        Long sucursalId = 1L;
        Inventory inventario = new Inventory(1L, productoId, sucursalId, 3L);

        when(inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId))
                .thenReturn(Optional.of(inventario));

        assertThrows(InventoryException.class, () ->
                inventoryService.updateQuantity(productoId, sucursalId, -5L));
    }

    @Test
    @DisplayName("Debe actualizar la cantidad del inventario a 0")
    void debeActualizarCantidadACero() {
        Long id = 1L;
        Inventory inventario = new Inventory(id, 10L, 1L, 50L); // cantidad inicial: 50

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventario));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> inv.getArgument(0));

        inventoryService.updateInventory(id);

        assertThat(inventario.getCantidad()).isEqualTo(0L);
        verify(inventoryRepository).save(inventario);
    }

    @Test
    @DisplayName("Debe eliminar inventario si cantidad es 0")
    void debeEliminarInventarioSiCantidadEsCero() {
        Long id = 1L;
        Inventory inventory = new Inventory(id, 10L, 1L, 0L);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        doNothing().when(inventoryRepository).deleteById(id);

        inventoryService.deleteById(id);

        verify(inventoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción si cantidad es mayor que 0")
    void debeLanzarExcepcionSiCantidadMayorCero() {
        Long id = 1L;
        Inventory inventory = new Inventory(id, 10L, 1L, 5L); // cantidad > 0
        Product producto = new Product(10L, "Arroz", 2990.0);

        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(productoClientRest.findByIdProducto(10L)).thenReturn(producto);

        InventoryException exception = assertThrows(InventoryException.class, () ->
                inventoryService.deleteById(id));

        assertThat(exception.getMessage()).contains("porque tiene el producto Arroz una cantidad mayor que 0");
    }


}

