package com.opencode.minikeyvault.utils;

import java.io.InputStream;
import java.net.URL;
import javafx.scene.image.Image;

/** class: ResourceManager. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-28 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class ResourceManager {

    private static final String DIR_FONTS = "fonts";
    private static final String DIR_FXMLVIEWS = "fxml";
    private static final String DIR_IMAGES = "images";
    private static final String DIR_SCRIPTS = "scripts";
    private static final String DIR_STYLES = "styles";

    private ResourceManager() {
        throw new IllegalStateException(ResourceManager.class.getName());
    }

    /**
     * Devuelve la referencia al archivo de fuente solicitada.
     * 
     * @param name nombre del archivo (Sin extensión).
     * @return referencia al archivo.
     */
    public static URL getFont(String name) {
        return getResource(DIR_FONTS, name);
    }

    /**
     * Devuelve la referencia a la vista (FXML) solicitado.
     * 
     * @param name nombre del archivo (Sin extensión).
     * @return referencia al archivo.
     */
    public static URL getFxView(String name) {
        return getResource(DIR_FXMLVIEWS, name + ".fxml");
    }

    /**
     * Devuelve un objecto Image con la imagen solicitada.
     * 
     * @param name name nombre de la imagen (Ubicada en la carpera resource).
     * @return instancia de Image con la imagen indicada.
     */
    public static Image getImage(String name) {

        Image image = null;

        try (InputStream inputStream = getResourceAsStream(DIR_IMAGES, name);) {
            image = new Image(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Devuelve el script (Stream de bytes) indicado.
     * 
     * @param name nombre completo del archivo.
     * @return archivo.
     */
    public static InputStream getScriptFile(String name) {
        return getResourceAsStream(DIR_SCRIPTS, name);
    }

    /**
     * Devuelve la referencia a la hoja de estilos (CSS) solicitada.
     * 
     * @param name nombre del archivo (Sin extensión).
     * @return referencia al archivo.
     */
    public static URL getCssStyle(String name) {
        return getResource(DIR_STYLES, name);
    }

    /**
     * Devuelve un inputstream del recurso solicitado.
     * 
     * @param dir nombre del directorio dentro del cual se ubica el archivo.
     *     null en caso se encuentre directamente dentro de 'resources'.
     * @param name nombre del archivo.
     * @return inputstream.
     */
    public static InputStream getResourceAsStream(String dir, String name) {
        return ResourceManager.class.getResourceAsStream(
                (dir != null ? "/" + dir : "")  + "/" + name);
    }

    /**
     * Devuelve la URL del recurso solicitado.
     * @param dir nombre del directorio dentro del cual se ubica el archivo.
     *     null en caso se encuentre directamente dentro de 'resources'.
     * @param name nombre del archivo.
     * @return URL.
     */
    public static URL getResource(String dir, String name) {
        return ResourceManager.class.getResource(
                (dir != null ? "/" + dir : "")  + "/" + name);
    }

}
