package com.opencode.minikeyvault.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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
public class CustomProperties {

    private Properties properties;
    private String filepath;

    /**
     * Método que inicializa el archivo de propiedades.
     *
     * @param filepath ruta del archivo.
     * @throws IOException excepción en caso ocurriera un error.
     */
    public CustomProperties(String filepath) throws IOException {

        File file = new File(filepath);

        if (!file.exists()) {
            if (file.createNewFile()) {
                log.debug("El archivo ('{}') se creó correctamente.", file.getAbsolutePath());
            }
        } else {
            log.debug("El archivo ('{}') ya existe.", file.getAbsolutePath());
        }

        this.filepath = filepath;

        try (InputStream input = new FileInputStream(filepath)) {
            properties = new Properties();
            properties.load(input);
        }

    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    /**
     * Metodo que devuelve el valor de la propiedad indicada.
     *
     * @param key nombre de la propiedad.
     * @return valor de la propiedad o null en caso la propiedad no exista.
     */
    public String getValue(String key) {

        if (properties != null) {
            return properties.getProperty(key, null);
        }

        return null;
    }

    /**
     * Metodo que define el valor especificado en el atributo con la
     * clave indicada. Si el atributo no existe, lo crea.
     *
     * @param key clave del atributo.
     * @param value valor del atributo.
     * @return valor previo del atributo o null en caso el atributo no existia.
     * @throws IOException excepción en caso de error.
     */
    public Object put(String key, String value) throws IOException {
        return setValue(PUT, key, value);
    }

    /**
     * Metodo que define el valor especificado en el atributo con la
     * clave indicada, siempre y cuando el atributo no exista previamente.
     *
     * @param key clave del atributo.
     * @param value valor del atributo.
     * @return valor previo del atributo o null en caso el atributo no existia.
     * @throws IOException excepción en caso de error.
     */
    public Object putIfAbsent(String key, String value) throws IOException {
        return setValue(PUT_IF_ABSENT, key, value);
    }

    /**
     * Metodo que reemplaza el valor especificado en el atributo con la
     * clave indicada, siempre y cuando el atributo exista.
     *
     * @param key clave del atributo.
     * @param value valor del atributo.
     * @return valor previo del atributo o null en caso el atributo no exista.
     * @throws IOException excepción en caso de error.
     */
    public Object replace(String key, String value) throws IOException {
        return setValue(REPLACE, key, value);
    }

    private static final int PUT = 1;
    private static final int PUT_IF_ABSENT = 2;
    private static final int REPLACE = 3;

    /**
     *
     * @param type
     * @param key
     * @param value
     * @return
     * @throws IOException excepción en caso de error.
     */
    private Object setValue(int type, String key, String value) throws IOException {

        Object previousValue = null;

        if (properties != null) {
            try (OutputStream output = new FileOutputStream(filepath)) {
                switch (type) {
                    case PUT:
                        previousValue = properties.put(key, value);
                        break;
                    case PUT_IF_ABSENT:
                        previousValue = properties.putIfAbsent(key, value);
                        break;
                    case REPLACE:
                        previousValue = properties.replace(key, value);
                        break;
                    default:
                        log.info("Tipo de operación incorrecta.");
                        return null;
                }

                properties.store(output, null);
            }
        }

        return previousValue;
    }

}
