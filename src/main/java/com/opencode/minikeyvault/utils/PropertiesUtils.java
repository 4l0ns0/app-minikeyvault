package com.opencode.minikeyvault.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * class: PropertiesUtils. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class PropertiesUtils {

    private PropertiesUtils() {
        throw new IllegalStateException(PropertiesUtils.class.getName());
    }

    private static Properties prop = new Properties();

    static {

        try (InputStream input = new FileInputStream(Constants.APP_PROPERTIES)) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static final String KEYSTORE_FILENAME = "application.keystore.filename";
    
    /**
     * 
     * @param propertyName
     * @return
     */
    public static String readProperty(String propertyName) {
        return prop.getProperty(propertyName, null);
    }
    
}
