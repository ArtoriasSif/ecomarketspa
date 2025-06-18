package com.cmunoz.msvc.sucursal.repositories;

import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    Optional<Sucursal> findByNombreSucursal(String nombreSucursal);



}
