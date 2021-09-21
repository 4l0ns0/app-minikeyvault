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

    /**
     * class: ResultType. <br/>
     * @author Henry Navarro <br/><br/>
     *          <u>Cambios</u>:<br/>
     *          <ul>
     *          <li>2021-09-11 Creación del proyecto.</li>
     *          </ul>
     * @version 1.0
     */
    public enum ResultType {
        INITIALIZED, ALREADY_INITIALIZED, ERROR_ON_INITIALIZATION
    }

    public static final String APP_NAME = "Mini Key Vault";
    public static final String APP_IMAGE = "app.png";
    public static final String APP_ICON_FILE = "64x64.ico";
    public static final String APP_ICON_NAME = "mkv.ico";

    public static final String DB_DRIVER = "jdbc:h2";
    public static final String DB_NAME = "keystore";
    public static final String DB_FILENAME = DB_NAME + ".mv.db";

    public static final String PROP_FILENAME = "mkv.properties";
    public static final String PROP_KEY_DB_USERNAME = "auth.dsuc";
    public static final String PROP_KEY_DB_PASSWORD = "auth.upds";

}
