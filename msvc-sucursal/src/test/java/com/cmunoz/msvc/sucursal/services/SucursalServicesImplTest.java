package com.cmunoz.msvc.sucursal.services;

import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.client.SucursalClientRest;
import com.cmunoz.msvc.sucursal.client.PedidoClientRest;
import com.cmunoz.msvc.sucursal.models.Inventario;
import com.cmunoz.msvc.sucursal.repositories.SucursalRepository;
import net.datafaker.Faker;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        this.sucursalPrueba = new Sucursal(
                "Sucursal Lastarria", "Lastarria 1234", "Santiago", "Santiago", "Metropolitana", "+56991234567", "lastarria@marketspaeco.cl"
        );
        Faker faker = new Faker(Locale.of("es", "CL"));
        for (int i = 0; i < 10; i++) {
            Sucursal sucursal = new Sucursal();
                String nombreSucursal = faker.company().name();
                sucursal.setNombreSucursal(nombreSucursal);
                sucursal.setDireccionSucursal(faker.address().streetAddress());
                sucursal.setTelefonoSucursal(faker.phoneNumber().cellPhone());
                sucursal.setCiudadSucursal(faker.address().city());
                sucursal.setProvinciaSucursal(faker.address().state());
                sucursal.setRegionSucursal(faker.address().stateAbbr());
                sucursal.setEmailSucursal(faker.internet().emailAddress());
                sucursals.add(sucursal);
        }

    }

    @Test
    public void findAllSucursal() {
        List<Sucursal> sucursalsPrueba = this.sucursals;
        sucursals.add(sucursalPrueba);
        when(sucursalRepository.findAll()).thenReturn(sucursalsPrueba);

        List<Sucursal> result = sucursalServices.findAllSucursal();
        assertThat(result).hasSize(11);
        assertThat(result).contains(sucursalPrueba);

        verify(sucursalRepository, times(1)).findAll();
    }

    @Test
    public void findByIdSucursal() {
        when(sucursalRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(sucursalPrueba));

        Sucursal result = sucursalServices.findByIdSucursal(Long.valueOf(1L));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(sucursalPrueba);
        verify(sucursalRepository, times(1)).findById(Long.valueOf(1L));
    }

    @Test
    public void findByNombreSucursal() {
        when(sucursalRepository.findByNombreSucursal(sucursalPrueba.getNombreSucursal())).thenReturn(Optional.of(sucursalPrueba));
        Sucursal result = sucursalServices.findByNombreSucursal(sucursalPrueba.getNombreSucursal());
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(sucursalPrueba);
        verify(sucursalRepository, times(1)).findByNombreSucursal(sucursalPrueba.getNombreSucursal());
    }

    @Test
    public void save() {
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursalPrueba);
        Sucursal result = sucursalServices.save(sucursalPrueba);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(sucursalPrueba);
        verify(sucursalRepository, times(1)).save(any(Sucursal.class));
    }

    @Test
    public void deleteByIdSucursal() {
        doNothing().when(sucursalServices).deleteByIdSucursal(Long.valueOf(1L));
        sucursalServices.deleteByIdSucursal(Long.valueOf(1L));
        verify(sucursalServices, times(1)).deleteByIdSucursal(Long.valueOf(1L));

    }

    @Test
    public void updateByIdSucursal() {
        when(sucursalServices.updateByIdSucursal(Long.valueOf(1L), sucursalPrueba));

    }
}