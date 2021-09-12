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

    private static Properties prop;

    private ConfigFile() {
        throw new IllegalStateException(ConfigFile.class.getName());
    }

    /**
     * Metodo que devuelve una intancia del archivo de configuración.
     * 
     * @param onlyIfExist Si es true, únicamente se devolverá del archivo de configuración 
     *     si el archivo físico existe y devolvera una excepción en caso esto no se cumpla. 
     *     Caso contrario, si es false y el archivo físico no existe, intentará crearlo.
     * @return instancia del archivo de configuración.
     * @throws IOException excepción en caso ocurriera un error.
     */
    private static Properties getPropertiesInstance(boolean onlyIfExist) throws IOException {

        if (prop == null) {
            File file = new File(Constants.PROP_FILENAME);

            if (!onlyIfExist && !file.exists()) {
                boolean isCreated = file.createNewFile();

                if (isCreated) {
                    log.debug("El archivo ('{}') de configuración se creó "
                            + "correctamente.", file.getAbsolutePath());
                }
            }

            try (InputStream input = new FileInputStream(Constants.PROP_FILENAME)) {
                prop = new Properties();
                prop.load(input);
            }
        }

        return prop;
    }

    /**
     * Metodo para inicializar el archivo de configuración.
     * 
     * <p>Este metodo solo debe ser ejecutado cuando el archivo de configuración 
     * no existe y lo que se desea es inicializarlo, puesto que lo que se hará será
     * crearlo.
     * 
     * @return true si se creó el archivo de configuración, caso contrario false.
     */
    public static boolean init() {

        boolean result = false;

        try {
            Properties properties = getPropertiesInstance(false);

            try (OutputStream output = new FileOutputStream(Constants.PROP_FILENAME)) {
                properties.putIfAbsent(Constants.PROP_KEY_DB_USERNAME, 
                        UUID.randomUUID().toString());
                properties.putIfAbsent(Constants.PROP_KEY_DB_PASSWORD, 
                        UUID.randomUUID().toString());
                properties.store(output, null);
                result = true;
            }
        } catch (Exception e) {
            log.error("Ocurrió un error al inicializar el archivo ({}) de "
                    + "configuración: {}", Constants.PROP_FILENAME, e.getMessage());
        }

        return result;
    }

    /**
     * Devuelve la instancia del archivo de configuración.
     * 
     * @return instancia del archivo de configuración.
     */
    public static Properties getProperties() {

        try {
            return getPropertiesInstance(true);
        } catch (IOException e) {
            log.error("Error al capturar el archivo ('{}') de configuración: {}", 
                    e.getMessage());
        }

        return null;
    }

    /**
     * Metodo que devuelve el valor de la propiedad indicada.
     * 
     * @param key nombre de la propiedad.
     * @return valor de la propiedad.
     */
    public static String getValue(String key) {

        Properties prop = getProperties();

        if (prop != null) {
            try {
                return prop.getProperty(key, null);
            } catch (Exception e) {
                log.error("Error al capturar el valor del atributo '{}': {}", 
                        key, e.getMessage());
            }
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
     */
    public static Object put(String key, String value) {
        return setValue(PUT, key, value);
    }

    /**
     * Metodo que define el valor especificado en el atributo con la 
     * clave indicada, siempre y cuando el atributo no exista previamente.
     * 
     * @param key clave del atributo.
     * @param value valor del atributo.
     * @return valor previo del atributo o null en caso el atributo no existia.
     */
    public static Object putIfAbsent(String key, String value) {
        return setValue(PUT_IF_ABSENT, key, value);
    }

    /**
     * Metodo que reemplaza el valor especificado en el atributo con la 
     * clave indicada, siempre y cuando el atributo exista.
     * 
     * @param key clave del atributo.
     * @param value valor del atributo.
     * @return valor previo del atributo o null en caso el atributo no exista.
     */
    public static Object replace(String key, String value) {
        return setValue(REPLACE, key, value);
    }

    private static final int PUT = 1;
    private static final int PUT_IF_ABSENT = 2;
    private static final int REPLACE = 3;
    
    private static Object setValue(int type, String key, String value) {

        Object obj = null;

        Properties prop = getProperties();

        if (prop != null) {
            try {
                switch (type) {
                    case PUT:
                        obj = prop.put(key, value);
                        break;
                    case PUT_IF_ABSENT:
                        obj = prop.putIfAbsent(key, value);
                        break;
                    case REPLACE:
                        obj = prop.replace(key, value);
                        break;
                    default:
                        log.info("Tipo de operación incorrecta.");
                        return null;
                }

                storeValue();
            } catch (Exception e) {
                log.error("Ocurrio un error al guardar el atributo ('{}'='{}') en el "
                        + "archivo de configuración: {}", key, value, e.getMessage());
            }
        }

        return obj;
    }
    
    /**
     * Metodo encargado de persistir los datos de properties al archivo físico.
     * @throws IOException excepción
     */
    private static void storeValue() throws IOException {

        try (OutputStream output = new FileOutputStream(Constants.PROP_FILENAME)) {
            prop.store(output, null);
        }

    }

}
