package com.opencode.minikeyvault.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * class: Configurations. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class Configurations {

    private Configurations() {
        throw new IllegalStateException(Configurations.class.getName());
    }

    private static Properties prop = new Properties();

    static {

        try (InputStream input = new FileInputStream(Constants.CONFIGURATION_FILENAME)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static final String KEYSTORE_FILENAME = "application.keystore.filename";
    
    /**
     * Metodo que verifica que el archivo de configuración que la aplicación
     * requiere para su funcionamientos se encuentre definido. Caso contrario, 
     * lo inicializa.
     */
    public static void initConfiguration() {

        File file = new File(Constants.CONFIGURATION_FILENAME);
        
        if (file.exists()) {
            if (!file.canRead() || !file.canWrite()) {
                log.error("No se puede leer o escribir en el {}", 
                        Constants.CONFIGURATION_FILENAME);
            }
        } else {
            try {
                if (file.createNewFile()) {
                    log.info("Se creó el {}", Constants.CONFIGURATION_FILENAME);
                } else {
                    log.error("No se pudo crear el {}", Constants.CONFIGURATION_FILENAME);
                }
            } catch (IOException e) {
                log.error("Ocurrio un error al inicializar el {}: {}",
                        e.getMessage(), Constants.CONFIGURATION_FILENAME);
            }
        }

    }

    /**
     * Metodo que devuelve el valor de la propiedad indicada.
     * 
     * @param propertyName nombre de la propiedad.
     * @return valor de la propiedad.
     */
    public static String readProperty(String propertyName) {
        return prop.getProperty(propertyName, null);
    }
    
}
