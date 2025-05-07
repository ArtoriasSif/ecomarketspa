package com.cmunoz.msvc.sucursal.repositories;

import com.cmunoz.msvc.sucursal.models.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Optional<Sucursal> findById(Long IdSucursal);

}
