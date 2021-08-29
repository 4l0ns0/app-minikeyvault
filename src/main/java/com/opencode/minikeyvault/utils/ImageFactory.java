package com.opencode.minikeyvault.utils;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * class: ImageFactory. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class ImageFactory {

    /**
     * Instantiates a new image factory.
     */
    private ImageFactory() {
        throw new IllegalStateException(ImageFactory.class.getName());
    }

    public static final String APP_ICON = "app-icon.png"/*"app-icon.gif"*/;
    public static final String COPY_TO_CLIPBOARD = "copy_to_clipboard.png";

    /**
     * Devuelve el icono de la aplicación.
     * 
     * @return instancia de Image con el icono de la aplicación.
     */
    public static Image getApplicationIcon() {
        return getImage(APP_ICON);
    }
    
    /**
     * Devuelve
     * @return
     */
    public static ImageView getCopyToClipboardIcon() {
        return getImageView(COPY_TO_CLIPBOARD, 20);
    }
    
    /**
     * Devuelve un objecto Image con la imagen solicitada.
     * 
     * @param name name nombre de la imagen (Ubicada en la carpera resource).
     * @return instancia de Image con la imagen indicada.
     */
    public static Image getImage(String name) {

        Image image = null;

        try (InputStream inputStream = ImageFactory.class.getClassLoader()
                .getResourceAsStream("images/" + name);) {
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

}
