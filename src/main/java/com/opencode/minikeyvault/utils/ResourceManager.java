package com.opencode.minikeyvault.utils;

import java.io.InputStream;
import java.net.URL;

/** class: ResourceManager. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-28 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class ResourceManager {

    private ResourceManager() {
        throw new IllegalStateException(ResourceManager.class.getName());
    }

    public static URL getFxDialog(String name) {
        return getResource("fxml/dialog", name + ".fxml");
    }

    public static URL getFxView(String name) {
        return getResource("fxml/view", name + ".fxml");
    }

    public static InputStream getScriptFile(String name) {
        return getResourceAsStream("scripts", name);
    }

    public static InputStream getResourceAsStream(String dir, String name) {
        return ResourceManager.class.getClassLoader()
                .getResourceAsStream(dir + "/" + name);
    }

    public static URL getResource(String dir, String name) {
        return ResourceManager.class.getClassLoader()
                .getResource(dir + "/" + name);
    }

}
