package com.opencode.minikeyvault.utils;

/**
 * class: Constants. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class Constants {

    private Constants() {
        throw new IllegalStateException(Constants.class.getName());
    }

    public static final String APP_NAME = "Mini Keyvault";
    public static final String CONFIGURATION_FILENAME = "application.properties";
    public static final String KEYSTORE_FILENAME = "keystore.json";

}
