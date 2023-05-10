package com.opencode.minikeyvault.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** class: ImageFactory. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-02 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageFactory {

    /**
     * class: FontAwesome. <br/>
     * @author Henry Navarro <br/><br/>
     *          <u>Cambios</u>:<br/>
     *          <ul>
     *          <li>2021-09-03 Creación del proyecto.</li>
     *          </ul>
     * @version 1.0
     */
    public enum FontAwesome {

        FA_FILE_O('\uf016'),
        FA_PENSIL('\uf040'),
        FA_TRASH_O('\uf014'),
        FA_FILES_O('\uf0c5'),
        FA_EYE('\uf06e'),
        FA_EYE_SLASH('\uf070'),
        FA_FLOPPY_O('\uf0c7'),
        FA_TIMES('\uf00d'),
        FA_TOGGLE_ON('\uf205'),
        FA_TOGGLE_OFF('\uf204'),
        FA_SIGN_OUT('\uf08b'),
        FA_SIGN_IN('\uf090'),
        FA_DATABASE('\uf1c0'),
        FA_QUESTION_CIRCLE_O('\uf29c');

        private final Character character;

        FontAwesome(char character) {
            this.character = character;
        }

        public Character getChar() {
            return character;
        }

        @Override
        public String toString() {
            return character.toString();
        }
    }

    static {
        Font.loadFont(ResourceManager.getFont("FontAwesome.otf")
                .toExternalForm(), 20);
    }

    public static final String DEFAULT_FONT_FAMILY = "-fx-font-family: FontAwesome";
    public static final String DEFAULT_ICON_SIZE = "16.0";
    public static final String DEFAULT_FONT_SIZE = "1em";

    /**
     * Metodo que devuelve una instancia de un Label cargado con el icono indicada.
     * <ul>
     * <li>Tamaño por defecto: 16.0
     * <li>Color por defecto: Gris.
     * </ul>
     * @param icon nombre del icono a cargar.
     * @return etiqueta con la imagen.
     */
    public static Label getIconifiedLabel(FontAwesome icon) {
        return getIconifiedLabel(icon, null, null);
    }
    
    /**
     * Metodo que devuelve una instancia de un Label cargado con el icono indicada.
     * <ul>
     * <li>Color por defecto: Gris.
     * </ul>
     * @param icon nombre del icono a cargar.
     * @param size tamaño. Si null se asigna tamaño por defecto (16.0).
     * @return etiqueta con la imagen.
     */
    public static Label getIconifiedLabel(FontAwesome icon, Double size) {
        return getIconifiedLabel(icon, size, null);
    }

    /**
     * Metodo que devuelve una instancia de un Label cargado con el icono indicada.
     * 
     * @param icon nombre del icono a cargar.
     * @param size tamaño. Si null se asigna tamaño por defecto (16.0).
     * @param color color. Si null se asigna color por defecto (Gris).
     * @return objeto con la imagen.
     */
    public static Label getIconifiedLabel(FontAwesome icon, Double size, String color) {

        String fontSize = "-fx-font-size: " + (size == null ? DEFAULT_ICON_SIZE : size);
        String fontColor = "-fx-text-fill: " + (StringUtils.isBlank(color) 
                ? "-fx-text-inner-color" : color);

        Label label = new Label(icon.toString());
        label.getStyleClass().add("Awesome");
        label.setStyle(DEFAULT_FONT_FAMILY + ";" + fontSize + ";" + fontColor + ";");

        return label;
    }

    /**
     * Metodo para añadir un icono en el control especificado.
     * <ul>
     * <li>Tamaño por defecto: 16.0
     * <li>Color por defecto: Gris.
     * </ul>
     * @param ctrl control en el cual se desea añadir el icono.
     * @param icon nombre del icono a cargar.
     */
    public static void setIcon(Object ctrl, FontAwesome icon) {
        setIcon(ctrl, icon, null, null);
    }

    /**
     * Metodo para añadir un icono en el control especificado.
     * <ul>
     * <li>Color por defecto: Gris.
     * </ul>
     * @param ctrl control en el cual se desea añadir el icono.
     * @param icon nombre del icono a cargar.
     * @param size tamaño. Si null se asigna tamaño por defecto (16.0).
     */
    public static void setIcon(Object ctrl, FontAwesome icon,  Double size) {
        setIcon(ctrl, icon, size, null);
    }

    /**
     * Metodo para añadir un icono en el control especificado.
     * 
     * @param ctrl control en el cual se desea añadir el icono.
     * @param icon nombre del icono a cargar.
     * @param size tamaño. Si null se asigna tamaño por defecto (16.0).
     * @param color color. Si null se asigna color por defecto (Gris).
     */
    public static void setIcon(Object ctrl, FontAwesome icon, Double size, String color) {

        Label label = getIconifiedLabel(icon, size, color);

        if (ctrl instanceof Button) {
            ((Button) ctrl).setGraphic(label);
        } else if (ctrl instanceof ToggleButton) {
            ((ToggleButton) ctrl).setGraphic(label);
        } else if (ctrl instanceof MenuItem) {
            ((MenuItem) ctrl).setGraphic(label);
        } else {
            log.debug("El tipo de control especificado no se reconoce.");
        }

    }

    public static final Image IMG_APP_ICON = ResourceManager.getImage(Constants.APP_IMAGE);

    /**
     * Metodo que devuelve un ImageView cargado con la imagen especificada.
     * 
     * @param img nombre de la imagen a cargar en el objeto.
     * @return instancia de ImageView.
     */
    public static ImageView getImageView(String img, double size) {

        ImageView imageView = new ImageView(ResourceManager.getImage(img));
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);

        return imageView;
    }
    
    /**
     * Metodo que devuelve un ImageView cargado con la imagen especificada.
     * 
     * @param img imagen a cargar en el objeto.
     * @return instancia de ImageView.
     */
    public static ImageView getImageView(Image img, double size) {

        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);

        return imageView;
    }

}
