package com.nebulosa.msvc_inventario.repositories;

import com.nebulosa.msvc_inventario.models.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository <Inventory, Long>{


    Optional<Inventory> findByIdProductoAndIdSucursal(Long productoId, Long sucursalId);

    List<Inventory> findByIdSucursal(Long idSucursal);
}
