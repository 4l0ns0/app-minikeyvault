package com.opencode.minikeyvault.utils;

import java.io.InputStream;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** class: ResourceManager. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-28 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class ResourceManager {

    private static final String IMAGES_DIR = "images";
    private static final String FXMLVIEWS_DIR = "fxml/view";
    private static final String FXMLDIALOGS_DIR = "fxml/dialog";
    private static final String SCRIPTS_DIR = "scripts";

    private ResourceManager() {
        throw new IllegalStateException(ResourceManager.class.getName());
    }

    /**
     * Devuelve un objecto Image con la imagen solicitada.
     * 
     * @param name name nombre de la imagen (Ubicada en la carpera resource).
     * @return instancia de Image con la imagen indicada.
     */
    public static Image getImage(String name) {

        Image image = null;

        try (InputStream inputStream = getResourceAsStream(IMAGES_DIR, name);) {
            image = new Image(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Devuelve un objeto ImageView con la imagen y tamaño solicitado.
     * 
     * @param name nombre de la imagen (Ubicada en la carpera resource).
     * @param size tamaño en pixeles para la imagen.
     * @return instancia de ImageView con la imagen indicada.
     */
    public static ImageView getImageView(String name, int size) {

        Image image = getImage(name);

        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);

            return imageView;
        }

        return null;
    }

    /**
     * Devuelve la referencia al archivo (FXML) solicitado.
     * 
     * @param name nombre del archivo (Sin extensión).
     * @return referencia al archivo.
     */
    public static URL getFxDialog(String name) {
        return getResource(FXMLDIALOGS_DIR, name + ".fxml");
    }

    /**
     * Devuelve la referencia al archivo (FXML) solicitado.
     * @param name nombre del archivo (Sin extensión).
     * @return referencia al archivo.
     */
    public static URL getFxView(String name) {
        return getResource(FXMLVIEWS_DIR, name + ".fxml");
    }

    /**
     * Devuelve el script (Stream de bytes) indicado.
     * 
     * @param name nombre completo del archivo.
     * @return archivo.
     */
    public static InputStream getScriptFile(String name) {
        return getResourceAsStream(SCRIPTS_DIR, name);
    }

    private static InputStream getResourceAsStream(String dir, String name) {
        return ResourceManager.class.getClassLoader()
                .getResourceAsStream(dir + "/" + name);
    }

    private static URL getResource(String dir, String name) {
        return ResourceManager.class.getClassLoader()
                .getResource(dir + "/" + name);
    }

}
