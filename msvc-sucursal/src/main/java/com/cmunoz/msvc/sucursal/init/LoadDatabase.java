package com.cmunoz.msvc.sucursal.init;

import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.repositories.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception {
        if (sucursalRepository.count() == 0) {
            Sucursal sucursal1 = new Sucursal();
            sucursal1.setNombreSucursal("Sucursal Lastarria");
            sucursal1.setDireccionSucursal("Lastarria 1234");
            sucursal1.setCiudadSucursal("Santiago");
            sucursal1.setProvinciaSucursal("Santiago");
            sucursal1.setRegionSucursal("Metropolitana");
            sucursal1.setTelefonoSucursal("+56991234567");
            sucursal1.setEmailSucursal("lastarria@marketspaeco.cl");

            Sucursal sucursal2 = new Sucursal();
            sucursal2.setNombreSucursal("Sucursal Valdivia");
            sucursal2.setDireccionSucursal("Picarte 4567");
            sucursal2.setCiudadSucursal("Valdivia");
            sucursal2.setProvinciaSucursal("Valdivia");
            sucursal2.setRegionSucursal("Los Ríos");
            sucursal2.setTelefonoSucursal("+56992345678");
            sucursal2.setEmailSucursal("valdivia@marketspaeco.cl");

            Sucursal sucursal3 = new Sucursal();
            sucursal3.setNombreSucursal("Sucursal Antofagasta");
            sucursal3.setDireccionSucursal("Prat 7890");
            sucursal3.setCiudadSucursal("Antofagasta");
            sucursal3.setProvinciaSucursal("Antofagasta");
            sucursal3.setRegionSucursal("Antofagasta");
            sucursal3.setTelefonoSucursal("+56993456789");
            sucursal3.setEmailSucursal("antofagasta@marketspaeco.cl");

            log.info("Sucursal creada: {}", sucursalRepository.save(sucursal1));
            log.info("Sucursal creada: {}", sucursalRepository.save(sucursal2));
            log.info("Sucursal creada: {}", sucursalRepository.save(sucursal3));
        }
    }
}
