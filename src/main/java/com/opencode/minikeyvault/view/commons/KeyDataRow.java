package com.opencode.minikeyvault.view.commons;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.view.dto.KeyData;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** class: KeyDataRow. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-23 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public class KeyDataRow {

    private KeyData keyData;

    private int keyId;
    private String application;
    private String description;
    private HBox userName;
    private HBox password;

    /**
     * Constructor de la clase.
     * 
     * @param keyData instancia del KeyData.
     */
    public KeyDataRow(KeyData keyData) {

        this.keyData = keyData;

        this.keyId = keyData.getKeyId();
        this.application = keyData.getApplication();
        this.description = keyData.getDescription();
        this.userName = getDataPaneInstance(keyData.getUserName());
        this.password = getDataPaneInstance(keyData.getPassword());

    }

    /**
     * Genera una nueva instancia de un HBox, el cual contiene el Passwordfield, encargado
     * de mostrar la cadena de texto y la imagen (Fuente dentro de un Label) de copiar al
     * portapapeles a mostrar la pasar el mouse encima de la celda.
     * 
     * @param value cadena de texto que se desea cargar en el PasswordField.
     * @return instancia de HBox.
     */
    private HBox getDataPaneInstance(String value) {

        Label lblImg = ImageFactory.getIconifiedLabel(FontAwesome.FA_FILES_O, 14.0, null);
        lblImg.setEllipsisString("");
        lblImg.setOpacity(0.4);
        lblImg.setPrefWidth(0);

        PasswordField pwdControl = new PasswordField();
        pwdControl.setText(value);
        pwdControl.setEditable(false);
        pwdControl.setCursor(Cursor.DEFAULT);
        pwdControl.setBackground(Background.EMPTY);
        pwdControl.setPadding(new Insets(0.0));
        pwdControl.getStyleClass().add("not-selection-allowed");
        pwdControl.setContextMenu(new ContextMenu());

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(pwdControl, lblImg);
        hbox.setAlignment(Pos.CENTER);

        HBox.setHgrow(pwdControl, Priority.ALWAYS);

        return hbox;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        KeyDataRow other = (KeyDataRow) obj;

        return keyId == other.keyId
                && Objects.equals(application, other.application)
                && Objects.equals(description, other.description)
                && Objects.equals(((PasswordField) userName.getChildren().get(0)).getText(), 
                        ((PasswordField) other.userName.getChildren().get(0)).getText())
                && Objects.equals(((PasswordField) password.getChildren().get(0)).getText(),
                        ((PasswordField) other.password.getChildren().get(0)).getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, application, description, 
                ((PasswordField) userName.getChildren().get(0)).getText(),
                ((PasswordField) password.getChildren().get(0)).getText());
    }

}
