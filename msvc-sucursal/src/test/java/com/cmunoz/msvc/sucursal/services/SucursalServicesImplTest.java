package com.cmunoz.msvc.sucursal.services;

import com.cmunoz.msvc.sucursal.exception.SucursalException;
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
    @Mock
    private SucursalClientRest sucursalClientRest;
    @Mock
    private PedidoClientRest pedidoClientRest;


    @InjectMocks
    private SucursalServicesImpl sucursalServices;
    private Sucursal sucursalPrueba;
    private List<Sucursal> sucursals = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        sucursalServices = new SucursalServicesImpl(
                sucursalRepository,
                sucursalClientRest,
                pedidoClientRest
        );
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
        Long idSucursal = 1L;

        Sucursal mockSucursal = new Sucursal();
        mockSucursal.setIdSucursal(idSucursal);

        Inventario inventario = new Inventario();
        inventario.setIdInventario(100L);
        List<Inventario> inventarios = List.of(inventario);

        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(mockSucursal));
        when(sucursalClientRest.findByIdSucursal(idSucursal)).thenReturn(inventarios);

        doNothing().when(sucursalClientRest).updateInventory(100L);
        doNothing().when(sucursalClientRest).deleteInventoryById(100L);
        doNothing().when(pedidoClientRest).deletePedidoSucursal(idSucursal);
        doNothing().when(sucursalRepository).deleteById(idSucursal);


        String result = sucursalServices.deleteByIdSucursal(idSucursal);


        assertEquals("Sucursal eliminada con éxito", result);
        verify(sucursalRepository).findById(idSucursal);
        verify(sucursalClientRest).findByIdSucursal(idSucursal);
        verify(sucursalClientRest).updateInventory(100L);
        verify(sucursalClientRest).deleteInventoryById(100L);
        verify(pedidoClientRest).deletePedidoSucursal(idSucursal);
        verify(sucursalRepository).deleteById(idSucursal);


    }

    @Test
    public void updateByIdSucursal() {
        // Arrange
        Long idSucursal = 1L;

        Sucursal existente = new Sucursal();
        existente.setIdSucursal(idSucursal);
        existente.setNombreSucursal("Sucursal Antiguo Nombre");
        existente.setDireccionSucursal("Antigua Dirección");

        Sucursal actualizado = new Sucursal();
        actualizado.setNombreSucursal("Sucursal Nueva");
        actualizado.setDireccionSucursal("Nueva Dirección");
        actualizado.setTelefonoSucursal("+56999999999");
        actualizado.setCiudadSucursal("Nueva Ciudad");
        actualizado.setProvinciaSucursal("Nueva Provincia");
        actualizado.setRegionSucursal("Nueva Región");
        actualizado.setEmailSucursal("nueva@sucursal.cl");


        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(existente));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(existente);


        Sucursal resultado = sucursalServices.updateByIdSucursal(idSucursal, actualizado);


        assertThat(resultado).isNotNull();
        verify(sucursalRepository, times(2)).findById(idSucursal); // <-- clave
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    public void updateByIdSucursal_sucursalNoExiste_lanzaExcepcion() {
        Long idSucursal = 99L;
        Sucursal dummy = new Sucursal();

        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.empty());

        SucursalException excepcion = assertThrows(SucursalException.class, () -> {
            sucursalServices.updateByIdSucursal(idSucursal, dummy);
        });

        assertEquals("No se encontro la sucursal con id: 99", excepcion.getMessage());
        verify(sucursalRepository, times(1)).findById(idSucursal);
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    public void findByIdSucursal_sucursalNoExiste_lanzaExcepcion() {

        Long idSucursalNoExistente = 99L;
        when(sucursalRepository.findById(idSucursalNoExistente)).thenReturn(Optional.empty());


        SucursalException exception = assertThrows(SucursalException.class, () -> {
            sucursalServices.findByIdSucursal(idSucursalNoExistente);
        });

        assertEquals("No se encontro la sucursal con id: " + idSucursalNoExistente, exception.getMessage());
        verify(sucursalRepository, times(1)).findById(idSucursalNoExistente);
    }

    @Test
    public void findByNombreSucursal_sucursalNoExiste_lanzaExcepcion() {

        String nombreNoExistente = "Sucursal Inexistente";
        when(sucursalRepository.findByNombreSucursal(nombreNoExistente)).thenReturn(Optional.empty());


        SucursalException exception = assertThrows(SucursalException.class, () -> {
            sucursalServices.findByNombreSucursal(nombreNoExistente);
        });


        assertEquals("No se encontro la sucursal con nombre: " + nombreNoExistente, exception.getMessage());
        verify(sucursalRepository, times(1)).findByNombreSucursal(nombreNoExistente);

    }

    @Test
    public void deleteByIdSucursal_sucursalNoExiste_lanzaExcepcion() {

        Long idNoExistente = 99L;
        when(sucursalRepository.findById(idNoExistente)).thenReturn(Optional.empty());


        SucursalException exception = assertThrows(SucursalException.class, () -> {
            sucursalServices.deleteByIdSucursal(idNoExistente);
        });

        assertEquals("No se encontro la sucursal con id: " + idNoExistente, exception.getMessage());
        verify(sucursalRepository, times(1)).findById(idNoExistente);
        verify(sucursalClientRest, never()).findByIdSucursal(anyLong()); // Asegura que no se llamaron a los clientes
        verify(sucursalRepository, never()).deleteById(anyLong()); // Asegura que no se intentó eliminar
    }
}