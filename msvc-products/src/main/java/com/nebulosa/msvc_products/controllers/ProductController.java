package com.nebulosa.msvc_products.controllers;

import com.nebulosa.msvc_products.dtos.PrecioUpdateDTO;
import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.models.entities.Product;
import com.nebulosa.msvc_products.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Validated
@Tag(name = "Productos", description = "Operaciones CRUD de productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Obtener todos los productos", description = "Recupera una lista de todos los productos disponibles en la tienda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"nombre\": \"Laptop\", \"precio\": 1200.00, \"stock\": 50}, {\"id\": 2, \"nombre\": \"Mouse\", \"precio\": 25.00, \"stock\": 200}]")))
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAll());
    }

    @Operation(summary = "Obtener un producto por ID", description = "Permite buscar y recuperar los detalles de un producto usando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Laptop\", \"precio\": 1200.00, \"stock\": 50}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto no encontrado con ID: 1\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> findByIdProducto(@Parameter(description = "ID del producto a buscar", required = true) @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findByIdProducto(id));
    }

    @Operation(summary = "Obtener producto por nombre", description = "Permite buscar y recuperar los detalles de un producto usando su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Laptop\", \"precio\": 1200.00, \"stock\": 50}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto no encontrado con nombre: Laptop\"}")))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Product> getProductosByNombre(@Parameter(description = "Nombre del producto a buscar", required = true) @PathVariable String nombre){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.findByNombreProducto(nombre));
    }

    //Endpoint de las clases clients en otros msvc
    @Operation(summary = "Obtener todos los productos como DTOs", description = "Recupera una lista de todos los productos en formato DTO para uso en otros microservicios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de DTOs de productos recuperada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class, type = "array"),
                            examples = @ExampleObject(value = "[{\"id\": 1, \"nombre\": \"Laptop\", \"precio\": 1200.00}, {\"id\": 2, \"nombre\": \"Mouse\", \"precio\": 25.00}]")))
    })
    @GetMapping("/productoDTO")
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductosDTO(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findAllProductDTO());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Registra un nuevo producto en el inventario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 3, \"nombre\": \"Teclado\", \"precio\": 75.00}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el nombre no puede estar vacío\"}")))
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(@Parameter(description = "Datos del producto a crear", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class),
                    examples = @ExampleObject(value = "{\"nombre\": \"Teclado\", \"precio\": 75.00, \"stock\": 100}")))
                                                              @Validated @RequestBody Product producto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(producto));
    }

    @Operation(summary = "Actualizar el precio de un producto", description = "Modifica el precio de un producto existente usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio del producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"nombre\": \"Laptop\", \"precio\": 1250.00}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Producto no encontrado con ID: 1\"}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error de validación: el precio no puede ser negativo\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updatePrecioProducto(
            @Parameter(description = "ID del producto a actualizar", required = true) @PathVariable Long id,
            @Parameter(description = "Nuevo precio del producto", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrecioUpdateDTO.class),
                            examples = @ExampleObject(value = "{\"precio\": 1250.00}")))
            @Validated @RequestBody PrecioUpdateDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.updatePrice(id, dto.getPrecio()));
    }


    @Operation(summary = "Eliminar un producto por ID", description = "Elimina un producto del inventario usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Producto eliminado exitosamente"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "Producto no encontrado con ID: 1")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@Parameter(description = "ID del producto a eliminar", required = true) @PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.deleteByIdProducto(id));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }



}
