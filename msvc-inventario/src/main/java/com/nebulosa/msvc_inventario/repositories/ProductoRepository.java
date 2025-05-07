package com.nebulosa.msvc_inventario.repositories;

import com.nebulosa.msvc_inventario.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository <Producto,Long> {

}
