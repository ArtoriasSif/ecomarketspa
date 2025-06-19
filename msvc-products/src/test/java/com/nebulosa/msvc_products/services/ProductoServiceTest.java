package com.nebulosa.msvc_products.services;
import com.nebulosa.msvc_products.clients.InventarioClientRest;
import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.exceptions.ProductException;
import com.nebulosa.msvc_products.models.Inventario;
import com.nebulosa.msvc_products.models.entities.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private InventarioClientRest inventarioClientRest;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private List<Product> productos;

    @InjectMocks
    private ProductServiceImpl  productService;

    @BeforeEach
    void setUp() {
        Faker faker = new Faker();
        productos = new ArrayList<>();

        for (long i = 1; i <= 50; i++) {
            productos.add(new Product(
                    i,
                    faker.commerce().productName(),
                    faker.number().randomDouble(2, 100, 50000) // precios entre 100 y 50000
            ));
        }
    }

    @Test
    @DisplayName("Debe retornar todos los productos")
    void debeRetornarTodosLosProductos() {
        when(productRepository.findAll()).thenReturn(productos);

        List<Product> resultado = productService.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(50);
        assertThat(resultado.get(0).getNombreProducto()).isNotEmpty();

        verify(productRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Debe retornar todos los productos en formato DTO")
    void debeRetornarProductosEnFormatoDTO() {
        when(productRepository.findAll()).thenReturn(productos);

        List<ProductoResponseDTO> resultado = productService.findAllProductDTO();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(50);

        ProductoResponseDTO primerDTO = resultado.get(0);
        assertThat(primerDTO.getDescripcion()).contains("Producto agregado exitosamente");
        assertThat(primerDTO.getPrecio()).isGreaterThan(0);
        assertThat(primerDTO.getNombreProducto()).isNotEmpty();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe devolver el producto cuando existe")
    void debeDevolverProductoCuandoExiste() {
        // Arrange
        Long idProducto = 1L;
        Product productoMock = new Product(idProducto, "Arroz", 2999.95);
        when(productRepository.findById(idProducto)).thenReturn(Optional.of(productoMock));

        // Act
        Product resultado = productService.findByIdProducto(idProducto);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdProducto()).isEqualTo(idProducto);
        assertThat(resultado.getNombreProducto()).isEqualTo("Arroz");
        verify(productRepository).findById(idProducto);
    }

    @Test
    @DisplayName("Debe lanzar ProductException si el producto no existe")
    void debeLanzarExcepcionSiProductoNoExiste() {
        // Arrange
        Long idInexistente = 999L;
        when(productRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ProductException ex = assertThrows(ProductException.class, () ->
                productService.findByIdProducto(idInexistente));

        assertThat(ex.getMessage()).isEqualTo("Producto con el id 999 no encontrado");
        verify(productRepository).findById(idInexistente);
    }

    @Test
    @DisplayName("Debe devolver el producto cuando existe por nombre")
    void debeDevolverProductoCuandoExistePorNombre() {
        // Arrange
        String nombre = "Arroz";
        Product productoMock = new Product(1L, nombre, 2999.95);
        when(productRepository.findByNombreProducto(nombre)).thenReturn(Optional.of(productoMock));

        // Act
        Product resultado = productService.findByNombreProducto(nombre);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreProducto()).isEqualTo(nombre);
        verify(productRepository).findByNombreProducto(nombre);
    }

    @Test
    @DisplayName("Debe lanzar ProductException si el producto no existe por nombre")
    void debeLanzarExcepcionSiProductoNoExistePorNombre() {
        // Arrange
        String nombreInexistente = "ProductoInexistente";
        when(productRepository.findByNombreProducto(nombreInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ProductException ex = assertThrows(ProductException.class, () ->
                productService.findByNombreProducto(nombreInexistente)
        );

        assertThat(ex.getMessage()).isEqualTo("Producto con el nombre " + nombreInexistente + " no encontrado");
        verify(productRepository).findByNombreProducto(nombreInexistente);
    }

    @Test
    @DisplayName("Debe guardar producto y capitalizar nombre correctamente")
    void debeGuardarProductoCorrectamente() {
        // Arrange
        Product producto = new Product(null, "arroz integral", 2500.0);

        // Mock para que no exista producto con ese nombre capitalizado
        when(productRepository.findByNombreProducto("Arroz Integral")).thenReturn(Optional.empty());

        // Mock para simular guardado y asignar ID
        Product productoGuardado = new Product(1L, "Arroz Integral", 2500.0);
        when(productRepository.save(any(Product.class))).thenReturn(productoGuardado);

        // Act
        ProductoResponseDTO resultado = productService.save(producto);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreProducto()).isEqualTo("Arroz Integral");
        assertThat(resultado.getDescripcion()).contains("registrado exitosamente");
        verify(productRepository).findByNombreProducto("Arroz Integral");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar ProductException si el producto ya existe con nombre capitalizado")
    void debeLanzarExcepcionSiProductoYaExiste() {
        // Arrange
        Product producto = new Product(null, "arroz integral", 2500.0);

        // Simular que ya existe producto con nombre capitalizado
        when(productRepository.findByNombreProducto("Arroz Integral"))
                .thenReturn(Optional.of(new Product(1L, "Arroz Integral", 2500.0)));

        // Act & Assert
        ProductException ex = assertThrows(ProductException.class, () -> {
            productService.save(producto);
        });

        assertThat(ex.getMessage()).isEqualTo("Ya existe un producto con el nombre: Arroz Integral");
        verify(productRepository).findByNombreProducto("Arroz Integral");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe actualizar precio correctamente")
    void debeActualizarPrecioCorrectamente() {
        Long id = 1L;
        Double nuevoPrecio = 1500.0;
        Product producto = new Product(id, "Arroz", 1000.0);

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductoResponseDTO response = productService.updatePrice(id, nuevoPrecio);

        assertThat(response).isNotNull();
        assertThat(response.getPrecio()).isEqualTo(nuevoPrecio);
        assertThat(response.getDescripcion()).contains("actualizado exitosamente");
        verify(productRepository).save(producto);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el precio es negativo")
    void debeLanzarExcepcionPrecioNegativo() {
        Long id = 1L;
        Double precioNegativo = -100.0;
        Product producto = new Product(id, "Arroz", 1000.0);

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));

        ProductException ex = assertThrows(ProductException.class, () ->
                productService.updatePrice(id, precioNegativo)
        );

        assertThat(ex.getMessage()).isEqualTo("El precio no puede ser negativo");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el precio no cambia")
    void debeLanzarExcepcionPrecioSinCambio() {
        Long id = 1L;
        Double precioIgual = 1000.0;
        Product producto = new Product(id, "Arroz", precioIgual);

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));

        ProductException ex = assertThrows(ProductException.class, () ->
                productService.updatePrice(id, precioIgual)
        );

        assertThat(ex.getMessage()).isEqualTo("El precio no ha cambiado");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si producto no existe")
    void debeLanzarExcepcionProductoNoExiste() {
        Long id = 999L;
        Double precio = 1200.0;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ProductException ex = assertThrows(ProductException.class, () ->
                productService.updatePrice(id, precio)
        );

        assertThat(ex.getMessage()).isEqualTo("No se encontró el producto con id: " + id);
        verify(productRepository, never()).save(any());
    }

    //Eliminar producto
    @Test
    @DisplayName("Debe eliminar inventarios con cantidad 0 y luego eliminar producto")
    void deleteByIdProductoInventariosSinStockEliminaInventariosYProducto() {
        Long id = 1L;
        Product producto = new Product(id, "Producto Test", 1000.0);
        Inventario inventarioSinStock1 = new Inventario(10L, id, 1L, 0L);
        Inventario inventarioSinStock2 = new Inventario(11L, id, 2L, 0L);
        Inventario inventarioOtraProducto = new Inventario(12L, 2L, 1L, 10L);

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));
        when(inventarioClientRest.findAll()).thenReturn(List.of(inventarioSinStock1, inventarioSinStock2, inventarioOtraProducto));
        doNothing().when(inventarioClientRest).deleteInventoryById(anyLong());
        doNothing().when(productRepository).deleteById(id);

        String resultado = productService.deleteByIdProducto(id);

        // Verificaciones
        verify(inventarioClientRest).deleteInventoryById(10L);
        verify(inventarioClientRest).deleteInventoryById(11L);
        verify(productRepository).deleteById(id);

        assertThat(resultado).contains("El inventario con id: 10");
        assertThat(resultado).contains("El inventario con id: 11");
        assertThat(resultado).contains("El producto con id: " + id + " ha sido eliminado correctamente");
    }

    @Test
    @DisplayName("Debe eliminar producto sin inventarios relacionados")
    void deleteByIdProducto_sinInventariosEliminaProducto() {
        Long id = 1L;
        Product producto = new Product(id, "Producto Test", 1000.0);

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));
        when(inventarioClientRest.findAll()).thenReturn(Collections.emptyList());
        doNothing().when(productRepository).deleteById(id);

        String resultado = productService.deleteByIdProducto(id);

        verify(inventarioClientRest).findAll();
        verify(productRepository).deleteById(id);

        assertThat(resultado).contains("El producto con id: " + id + " ha sido eliminado correctamente");
    }

    @Test
    @DisplayName("Debe lanzar excepción si producto tiene inventario con cantidad > 0")
    void deleteByIdProducto_productoConStock_lanzaExcepcion() {
        Long id = 1L;
        Product producto = new Product(id, "Producto Test", 1000.0);
        Inventario inventarioConStock = new Inventario(10L, id, 1L, 5L); // cantidad > 0

        when(productRepository.findById(id)).thenReturn(Optional.of(producto));
        when(inventarioClientRest.findAll()).thenReturn(List.of(inventarioConStock));

        ProductException ex = assertThrows(ProductException.class, () ->
                productService.deleteByIdProducto(id)
        );

        assertThat(ex.getMessage()).contains("No se puede eliminar el producto con id: " + id);
        verify(productRepository, never()).deleteById(any());
        verify(inventarioClientRest).findAll();
        verify(inventarioClientRest, never()).deleteInventoryById(any());
    }

}
