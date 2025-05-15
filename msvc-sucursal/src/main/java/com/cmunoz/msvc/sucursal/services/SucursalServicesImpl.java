package com.cmunoz.msvc.sucursal.services;


import com.cmunoz.msvc.sucursal.exception.SucursalException;
import com.cmunoz.msvc.sucursal.models.Sucursal;
import com.cmunoz.msvc.sucursal.repositories.SucursalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServicesImpl implements SucursalService {

    @Autowired
    private SucursalRepository SucursalRepository;

    @Override
    public List<Sucursal> findAllSucursal() {
        return SucursalRepository.findAll();
    }

    @Transactional
    @Override
    public Sucursal findByIdSucursal(Long id) {
        return SucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("No se encontro la sucursal con id: " + id ));
    }


    @Transactional
    @Override
    public Sucursal findByNombreSucursal(String nombreSucursal) {
        return SucursalRepository.findByNombreSucursal(nombreSucursal).orElseThrow(
                ()-> new SucursalException("No se encontro la sucursal con nombre: " + nombreSucursal)
        );
    }

    @Transactional
    @Override
    public Sucursal save(Sucursal sucursal) {
        if (SucursalRepository.findByNombreSucursal(sucursal.getNombreSucursal()).isPresent()){
            throw new SucursalException("La sucursal con el nombre "+sucursal.getNombreSucursal()+" ya existe");
        }
        return SucursalRepository.save(sucursal);
    }

    @Transactional
    @Override
    public void deleteByIdSucursal(Long id) {
        if(SucursalRepository.findById(id).isPresent()) {
            SucursalRepository.deleteById(id);
        }else{
            throw new SucursalException ("No se encontro la sucursal con id: " + id);
        }
    }
}
