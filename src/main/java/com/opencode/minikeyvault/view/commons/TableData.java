package com.opencode.minikeyvault.view.commons;

import com.opencode.minikeyvault.domain.UserKey;
import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** class: TableData. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-23 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public class TableData {

    private UserKey userKey;

    private int id;
    private String application;
    private String description;
    private StackPane userName;
    private StackPane password;
    
    /**
     * Constructor de la clase.
     * 
     * @param userKey instancia del UserKey.
     */
    public TableData(UserKey userKey) {
        
        this.userKey = userKey;

        this.id = userKey.getId();
        this.application = userKey.getApplication();
        this.description = userKey.getDescription();
        this.userName = getDataPaneInstance(userKey.getUserName());
        this.password = getDataPaneInstance(userKey.getPassword());

    }

    /**
     * Genera una nueva instancia de un Stackpane, el cual contiene el 
     * Passwordfield e ImageView a mostrar en la celda.
     * 
     * @param value cadena de texto que contendrá la celda.
     * @return instancia de Stackpane.
     */
    private StackPane getDataPaneInstance(String value) {

        Label imgCopy = ImageFactory.getIconifiedLabel(FontAwesome.FA_FILES_O, 14.0, null);
        imgCopy.setVisible(false);
        imgCopy.setOpacity(0.4);
        imgCopy.setMouseTransparent(true);

        PasswordField pwdControl = new PasswordField();
        pwdControl.setText(value);
        pwdControl.setEditable(false);
        pwdControl.setCursor(Cursor.DEFAULT);
        pwdControl.setBackground(Background.EMPTY);
        pwdControl.setPadding(new Insets(0.0));
        pwdControl.getStyleClass().add("not-selection-allowed");

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pwdControl, imgCopy);

        StackPane.setAlignment(imgCopy, Pos.CENTER_RIGHT);

        return stackPane;
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

        TableData other = (TableData) obj;

        return id == other.id
                && Objects.equals(application, other.application)
                && Objects.equals(description, other.description)
                && Objects.equals(((PasswordField) userName.getChildren().get(0)).getText(), 
                        ((PasswordField) other.userName.getChildren().get(0)).getText())
                && Objects.equals(((PasswordField) password.getChildren().get(0)).getText(),
                        ((PasswordField) other.password.getChildren().get(0)).getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, application, description, 
                ((PasswordField) userName.getChildren().get(0)).getText(),
                ((PasswordField) password.getChildren().get(0)).getText());
    }

}
