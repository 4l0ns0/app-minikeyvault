package com.opencode.minikeyvault.utils;

import javafx.scene.image.Image;

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

    public static final Image IMG_APP_ICON = ResourceManager
            .getImage("app-icon.png");

    public static final Image IMG_COPY_TO_CLIPBOARD = ResourceManager
            .getImage("copy-to-clipboard-2.gif");

    public static final Image IMG_INSERT = ResourceManager
            .getImage("action-insert.png");

    public static final Image IMG_UPDATE = ResourceManager
            .getImage("action-update.gif");

    public static final Image IMG_DELETE = ResourceManager
            .getImage("action-delete.gif");

    public static final Image IMG_OK = ResourceManager
            .getImage("option-ok.png");

    public static final Image IMG_CANCEL = ResourceManager
            .getImage("option-cancel.png");

}
