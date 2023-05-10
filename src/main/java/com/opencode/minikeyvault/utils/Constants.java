package com.opencode.minikeyvault.utils;

import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * class: Constants. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String APP_NAME = "Mini Key Vault";
    public static final String APP_IMAGE = "app.png";
    public static final String APP_ICON_FILE = "64x64.ico";
    public static final String APP_ICON_NAME = "mkv.ico";

    public static final String DB_DRIVER = "jdbc:h2";
    public static final String DB_NAME = "keystore";
    public static final String DB_FILENAME = DB_NAME + ".mv.db";

    public static final String CONFIGURATION_FILENAME = "._mkv";
//    public static final String PROP_KEY_DB_USERNAME = "auth.dsuc";
//    public static final String PROP_KEY_DB_PASSWORD = "auth.upds";

    public static final String SECURITY_FILENAME = "._mkvsec";
    public static final String SECURITY_FILEPATH =
            System.getProperty("user.home") + File.separator + SECURITY_FILENAME;
}
