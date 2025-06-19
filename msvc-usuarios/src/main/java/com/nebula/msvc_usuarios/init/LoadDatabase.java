package com.nebula.msvc_usuarios.init;

import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es", "CL"));

        String[] personajes = {
                // Dark Souls I
                "Gwyn", "Solaire de Astora", "Artorias el Caminante del Abismo", "Seath el Descamado",
                "Ornstein", "Smough", "Gwyndolin", "Nito", "Havel", "Quelana de Izalith", "Manus",

                // Dark Souls II
                "Vendrick", "Aldia", "Nashandra", "Elana", "Sir Alonne", "Lud y Zallen", "Raime",

                // Dark Souls III
                "Aldrich", "Yhorm el Gigante", "Lothric", "Lorian", "Pontiff Sulyvahn", "Sister Friede",
                "Slave Knight Gael", "Dancer de la Boreal", "Vordt de la Boreal",

                // Bloodborne
                "Gehrman", "Lady Maria", "Ludwig", "Father Gascoigne", "The Moon Presence",
                "Micolash", "Laurence", "Martyr Logarius",

                // Sekiro
                "Wolf", "Genichiro Ashina", "Lady Butterfly", "Isshin Ashina", "Emma",

                // Elden Ring
                "Radagon", "Godrick el Injertado", "Ranni la Bruja", "Blaidd", "Maliketh",
                "Mohg", "Malenia", "Radahn", "Rykard", "Starscourge Radagon",

                // Más personajes agregados
                "Sir Gideon Ofnir", "Hoarah Loux", "Fia, the Deathbed Companion", "Ranni's Puppet"
        };

        if (usuarioRepository.count() == 0) {
            for (int i = 0; i < personajes.length; i++) {
                Usuario usuario = new Usuario();

                String nombreUsuario = personajes[i];

                usuario.setNombreUsuario(nombreUsuario);
                usuario.setContraUsuario("clave" + i);
                usuario.setNombreDelUsuario("Knight " + personajes[i]);

                // Usar Faker para email válido
                String emailSeguro = faker.internet().emailAddress();
                usuario.setCorreoUsuario(emailSeguro);

                // Generar RUT con formato
                String numeroStr = faker.idNumber().valid().replaceAll("-", "");
                String ultimo = numeroStr.substring(numeroStr.length() - 1);
                String restante = numeroStr.substring(0, numeroStr.length() - 1);
                usuario.setRutUsuario(restante + "-" + ultimo);

                // Dirección limpia (número + calle)
                int numero = faker.number().numberBetween(100, 9999);
                String calle = faker.address().streetName().replaceAll("[^A-Za-zÁÉÍÓÚáéíóúñÑ\\s]", "");
                usuario.setDireccionUsuario(numero + " " + calle);

                // Teléfono Chile con formato +56 9XXXXXXXX
                usuario.setTelefonoUsuario("+56 9" + faker.number().digits(8));

                usuarioRepository.save(usuario);
                log.info("Usuario Dark Souls creado: {}", usuario);
            }
        }
    }
}
