package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.clients.ProductoClientRest;
import com.nebulosa.msvc_inventario.clients.SucursalClientRest;
import com.nebulosa.msvc_inventario.dtos.InventoryDTO;
import com.nebulosa.msvc_inventario.dtos.InventoryResponseDTO;
import com.nebulosa.msvc_inventario.exceptions.InventoryException;
import com.nebulosa.msvc_inventario.models.Product;
import com.nebulosa.msvc_inventario.models.Sucursal;
import feign.FeignException.NotFound;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductoClientRest productoClientRest;

    @Autowired
    private SucursalClientRest sucursalClientRest;


    @Transactional
    @Override
    public InventoryResponseDTO findById (Long id){
        Inventory I = inventoryRepository.findById(id).orElseThrow(
                () -> new InventoryException("No se encontró el inventario con id: " + id)
        );
        Product product= productoClientRest.findByIdProducto(I.getIdProducto());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(I.getIdSucursal());

        return new InventoryResponseDTO(
                id,I.getIdProducto(),product.getNombreProducto(),
                I.getIdSucursal(), sucursal.getNombreSucursal(), I.getCantidad()
        );
    }

    @Transactional
    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Transactional
    @Override
    public List<InventoryResponseDTO> findAllWithDetails() {
        List<Inventory> inventarios = inventoryRepository.findAll();
        List<InventoryResponseDTO> dtos = new ArrayList<>();

        for (Inventory i : inventarios) {
            Product p = productoClientRest.findByIdProducto(i.getIdProducto());
            Sucursal s = sucursalClientRest.findByIdSucursal(i.getIdSucursal());

            InventoryResponseDTO dto = InventoryResponseDTO.builder()
                    .idInventario(i.getIdInventario())
                    .idProducto(i.getIdProducto())
                    .nombreProducto(p.getNombreProducto())
                    .idSucursal(i.getIdSucursal())
                    .nombreSucursal(s.getNombreSucursal())
                    .cantidad(i.getCantidad())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    @Override
    public List<Inventory> findByIdSucursal(Long idSucursal) {
        return inventoryRepository.findByIdSucursal(idSucursal);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InventoryResponseDTO> findDetalleBySucursal(Long idSucursal) {
        List<Inventory> inventarios = inventoryRepository.findByIdSucursal(idSucursal);

        if (inventarios.isEmpty()) {
            throw new InventoryException("No se encontró inventario para la sucursal con ID " + idSucursal);
        }

        List<InventoryResponseDTO> dtos = new ArrayList<>();

        for (Inventory i : inventarios) {
            Product p = productoClientRest.findByIdProducto(i.getIdProducto());
            Sucursal s = sucursalClientRest.findByIdSucursal(i.getIdSucursal());

            InventoryResponseDTO dto = InventoryResponseDTO.builder()
                    .idInventario(i.getIdInventario())
                    .idProducto(i.getIdProducto())
                    .nombreProducto(p.getNombreProducto())
                    .idSucursal(i.getIdSucursal())
                    .nombreSucursal(s.getNombreSucursal())
                    .cantidad(i.getCantidad())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }




    @Transactional
    @Override
    public Inventory findByIdProductoAndIdSucursal(Long productoId, Long sucursalId) {
        return inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId)
                .orElseThrow(() -> new InventoryException("No se encontró inventario para el producto "
                        + productoId + " en la sucursal " + sucursalId));
    }


    @Transactional
    @Override
    public InventoryResponseDTO save(InventoryDTO dto) {
        // Validar que existe el producto
        Product producto;
        try {
            producto = productoClientRest.findByIdProducto(dto.getProductoId());
        } catch (NotFound ex) {
            throw new InventoryException("No se encontró el producto con id: " + dto.getProductoId());
        }

        // Validar que existe la sucursal
        Sucursal sucursal;
        try {
            sucursal = sucursalClientRest.findByIdSucursal(dto.getSucursalId());
        } catch (NotFound ex) {
            throw new InventoryException("No se encontró la sucursal con id: " + dto.getSucursalId());
        }

        // Validar que no exista inventario duplicado
        inventoryRepository.findByIdProductoAndIdSucursal(dto.getProductoId(), dto.getSucursalId())
                .ifPresent(existingInventory -> {
                    throw new InventoryException("Ya existe un inventario para el producto " +
                            dto.getProductoId() + " en la sucursal " + dto.getSucursalId() +
                            ". Actualice el inventario con ID: " + existingInventory.getIdInventario());
                });

        // Crear entidad desde el DTO
        Inventory nuevoInventario = Inventory.builder()
                .idProducto(dto.getProductoId())
                .idSucursal(dto.getSucursalId())
                .cantidad(dto.getQuantity())
                .build();

        // Guardar en base de datos
        Inventory savedInventory = inventoryRepository.save(nuevoInventario);

        // Retornar DTO de respuesta
        return InventoryResponseDTO.builder()
                .idInventario(savedInventory.getIdInventario())
                .idProducto(savedInventory.getIdProducto())
                .nombreProducto(producto.getNombreProducto())
                .idSucursal(savedInventory.getIdSucursal())
                .nombreSucursal(sucursal.getNombreSucursal())
                .cantidad(savedInventory.getCantidad())
                .build();
    }



    @Transactional
    @Override
    public Inventory updateQuantity(Long productoId, Long sucursalId, Long quantity) {
        Inventory i = inventoryRepository.findByIdProductoAndIdSucursal(productoId, sucursalId)
                .orElseThrow(() -> new InventoryException("Inventario no encontrado."));

        if (i.getCantidad() + quantity < 0) {
            throw new InventoryException("La cantidad a actualizar no puede ser negativa. Cantidad actual: " + i.getCantidad());
        }

        i.setCantidad(i.getCantidad() + quantity);
        return inventoryRepository.save(i);
    }

    @Transactional
    @Override
    public InventoryResponseDTO updateInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Inventario no encontrado con id: " + id));

        inventory.setCantidad(0L);
        inventoryRepository.save(inventory);

        // Obtener datos del producto y sucursal desde los microservicios
        Product producto = productoClientRest.findByIdProducto(inventory.getIdProducto());
        Sucursal sucursal = sucursalClientRest.findByIdSucursal(inventory.getIdSucursal());

        return new InventoryResponseDTO(
                inventory.getIdInventario(),
                inventory.getIdProducto(),
                producto.getNombreProducto(),
                inventory.getIdSucursal(),
                sucursal.getNombreSucursal(),
                inventory.getCantidad()
        );
    }

    /*@Transactional
    @Override
    public void updateInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Inventario no encontrado con id: " + id));
        inventory.setCantidad(0L);
        inventoryRepository.save(inventory);
    }*/


    @Transactional
    @Override
    public void deleteById(Long id) {
        if (inventoryRepository.findById(id).isPresent()) {
            Inventory inventory = inventoryRepository.findById(id).get();
            if(inventory.getCantidad() == 0){
                inventoryRepository.deleteById(id);
            }else {
                throw new InventoryException("No se puede eliminar el inventario con id: " + id +
                        " porque tiene el producto "+productoClientRest.findByIdProducto(inventory.getIdProducto()).getNombreProducto()+
                        " una cantidad mayor que 0. ");
            }
        } else {
            throw new InventoryException("No se encontró el inventario con id: " + id);
        }
    }

}

