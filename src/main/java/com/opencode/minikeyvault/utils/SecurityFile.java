package com.opencode.minikeyvault.utils;

import java.io.*;
import java.util.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

/**
 * class: SecurityFile. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2023-04-7 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityFile {

    public static final String PROP_SEC = "random";

    private static CustomProperties properties;

    /**
     * Método para inicializar el archivo de configuración.
     *
     * @return true si se creó el archivo de configuración, caso contrario false.
     */
    public static boolean init() {

        boolean result = false;

        try {
            properties = new CustomProperties(Constants.SECURITY_FILEPATH);

            result = true;
        } catch (Exception e) {
            log.error("Ocurrió un error al inicializar el archivo de encriptación ('{}'): {}",
                    Constants.SECURITY_FILENAME, e.getMessage());
        }

        return result;
    }

    /**
     * Método que devuelve la cadena de encriptación guardada en el archivo de seguridad.
     *
     * @return cadena de encriptación.
     */
    public static byte[] getSecret() {

        if (properties.containsKey(PROP_SEC)) {
            return Base64.getDecoder().decode(properties.getValue(PROP_SEC));
        } else {
            log.error("No se encuentra la llave de seguridad ('{}') en el archivo de " +
                    "encriptación ('{}').", PROP_SEC, Constants.SECURITY_FILENAME);
        }

        return null;
    }

    /**
     * Método que guarda el valor de encriptación (en base64) en el archivo de seguridad.
     *
     * @param secret valor de encriptación.
     * @return true si el valor se guardó correctamente, caso contrario false.
     */
    public static boolean setSecret(byte[] secret) {

        try {
            properties.put(PROP_SEC, Base64.getEncoder().encodeToString(secret));

            return true;
        } catch (IOException e) {
            log.error("Ocurrió un error al guardar la llave de seguridad en el archivo de "
                    + "encriptación ('{}'): {}", Constants.CONFIGURATION_FILENAME, e.getMessage());
        }

        return false;
    }

}
