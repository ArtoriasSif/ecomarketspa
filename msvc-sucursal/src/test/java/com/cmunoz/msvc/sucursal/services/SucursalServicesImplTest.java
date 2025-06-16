package com.cmunoz.msvc.sucursal.services;

import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.repositories.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SucursalServicesImplTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalServicesImpl sucursalServices;
    private Sucursal sucursalPrueba;
    private List<Sucursal> sucursals = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        this.sucursalPrueba = new Sucursal();

    }

    @Test
    public void findAllSucursal() {

    }

    @Test
    public void findByIdSucursal() {
    }

    @Test
    public void findByNombreSucursal() {
    }

    @Test
    public void save() {
    }

    @Test
    public void deleteByIdSucursal() {
    }

    @Test
    public void updateByIdSucursal() {
    }
}