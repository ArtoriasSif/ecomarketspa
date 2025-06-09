package com.nebula.msvc_usuarios.init;

import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository) {
        return args -> {
            Faker faker = new Faker(new Locale("es", "CL"));

            String[] personajesDarkSouls = {
                    "Solaire", "Artorias", "Gwyn", "Gwyndolin", "Seath", "Ornstein",
                    "Smough", "Aldrich", "Yhorm", "Lorian", "Lothric", "Gael"
            };
            if (usuarioRepository.count() == 0) {
                for (int i = 0; i < personajesDarkSouls.length; i++) {
                    Usuario usuario = new Usuario();

                    String nombreUsuario = personajesDarkSouls[i] ;

                    usuario.setNombreUsuario(nombreUsuario);
                    usuario.setContraUsuario("clave" + i);
                    usuario.setNombreDelUsuario("Knight " + personajesDarkSouls[i]);

                    usuario.setCorreoUsuario(nombreUsuario + "@souls.com");
                    //rut
                    String numeroStr = faker.idNumber().valid().replaceAll("-", "");
                    String ultimo = numeroStr.substring(numeroStr.length() - 1);
                    String restante = numeroStr.substring(0, numeroStr.length() - 1);
                    usuario.setRutUsuario(restante + "-" + ultimo);
                    //direccion
                    int numero = faker.number().numberBetween(100, 9999);
                    String calle = faker.address().streetName().replaceAll("[^A-Za-zÁÉÍÓÚáéíóúñÑ\\s]", "");
                    usuario.setDireccionUsuario(numero + " " + calle);
                    //telefono CHI
                    usuario.setTelefonoUsuario("+56 9" + faker.number().digits(8));

                    usuarioRepository.save(usuario);
                    log.info("Usuario Dark Souls creado: {}", usuario);
                }
            }
        };
    }

}
