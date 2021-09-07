package com.opencode.minikeyvault.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * class: ConfigFile. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class ConfigFile {

    private ConfigFile() {
        throw new IllegalStateException(ConfigFile.class.getName());
    }

    private static Properties properties;

    public static final String AUTH_DB_USERNAME = "auth.dsuc";
    public static final String AUTH_DB_PASSWORD = "auth.upds";

    /**
     * Metodo que verifica que el archivo de configuración que la aplicación
     * requiere para su funcionamientos se encuentre definido. Caso contrario, 
     * lo inicializa.
     */
    public static void init() {

        File file = new File(Constants.CONFIGURATION_FILENAME);

        if (!file.exists()) {
            boolean iscreated = false;

            try {
                iscreated = file.createNewFile();
            } catch (IOException e) {
                iscreated = false;
                log.error("Ocurrio un error al inicializar el '{}': {}",
                        Constants.CONFIGURATION_FILENAME, e.getMessage());
            }

            if (iscreated) {
                try (OutputStream output = new FileOutputStream(file)) {
                    Properties prop = new Properties();
                    prop.putIfAbsent(AUTH_DB_USERNAME, UUID.randomUUID().toString());
                    prop.putIfAbsent(AUTH_DB_PASSWORD, UUID.randomUUID().toString());
                    prop.store(output, null);

                    log.debug("Se inicializó el archivos de propiedades.");
                } catch (Exception e) {
                    log.error("Ocurrio un error al inicializar el '{}': {}",
                            Constants.CONFIGURATION_FILENAME, e.getMessage());
                }
            }
        }

    }

    /**
     * Metodo que devuelve el valor de la propiedad indicada.
     * 
     * @param key nombre de la propiedad.
     * @return valor de la propiedad.
     */
    public static String readProperty(String key) {
        return properties.getProperty(key, null);
    }

    /**
     * Metodo para obtener el archivo de propiedades del sistema.
     * 
     * @return archivo de propoedades.
     */
    public static Properties getProperties() {

        try (InputStream input = new FileInputStream(Constants.CONFIGURATION_FILENAME)) {
            Properties prop = new Properties();
            prop.load(input);

            return prop;
        } catch (Exception e) {
            log.error("Ocurrio un error al leer el '{}': {}",
                    Constants.CONFIGURATION_FILENAME, e.getMessage());
        }

        return null;
    }

    /**
     * Metodo para insertar una entrada en el archivo de propiedades del sistema.
     * 
     * @param key clave del atributo. 
     * @param value valor del atributo.
     */
    public void setProperty(String key, String value) {

        try (OutputStream output = new FileOutputStream(Constants.CONFIGURATION_FILENAME)) {
            Properties prop = new Properties();
            prop.setProperty(key, value);
            prop.store(output, null);

            log.debug("Se añadió el atributo (key: '{}' / valor: '{}') al archivo de "
                    + "propiedades.", key, value);
        } catch (Exception e) {
            log.error("Ocurrio un error al leer el '{}': {}",
                    Constants.CONFIGURATION_FILENAME, e.getMessage());
        }

    }

}
