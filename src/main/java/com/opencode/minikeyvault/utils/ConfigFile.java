package com.opencode.minikeyvault.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigFile {

    public static final String PROP_KEY_AUTH = "auth.dsuc";

    private static CustomProperties properties;

    /**
     * Método que valida si el archivo de configuración existe.
     *
     * @return true si existe, caso contrario false.
     */
    public static boolean exists() {
        return new File(Constants.CONFIGURATION_FILENAME).exists();
    }

    /**
     * Método para inicializar el archivo de configuración.
     * 
     * @return true si se creó el archivo de configuración, caso contrario false.
     */
    public static boolean init() {

        boolean result = false;

        try {
            properties = new CustomProperties(Constants.CONFIGURATION_FILENAME);

            result = true;
        } catch (Exception e) {
            log.error("Ocurrió un error al inicializar el archivo ({}) de "
                    + "configuración: {}", Constants.CONFIGURATION_FILENAME, e.getMessage());
        }

        return result;
    }

    /**
     * Método que devuelve la cadena de autenticación guardada en el archivo de configuración.
     * 
     * @return cadena de autenticación.
     */
    public static String getAuth() {
        return properties.getValue(PROP_KEY_AUTH);
    }

    /**
     * Método que guarda las credenciales de autenticación en el archivo de configuración.
     *
     * @param value cadena de autenticación.
     * @return true si el valor se guardó correctamente, caso contrario false.
     */
    public static boolean setAuth(String value) {

        try {
            properties.put(PROP_KEY_AUTH, value);

            return true;
        } catch (IOException e) {
            log.error("Ocurrió un error al guardar las credenciales en el archivo ({}) de "
                    + "configuración: {}", Constants.CONFIGURATION_FILENAME, e.getMessage());
        }

        return false;
    }

}
