package com.opencode.minikeyvault.view.commons;

import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/** class: TableData. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-23 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Data
public class TableData {

    private int id;
    private String application;
    private String description;

    @Setter(value = AccessLevel.NONE)
    private StackPane userName;

    @Setter(value = AccessLevel.NONE)
    private StackPane password;

    private HBox actions;

    /** Constructor de la clase.
     * 
     * @param id identificador del registro.
     * @param application nombre de la aplicación.
     * @param description descripcion de ayuda.
     * @param userName nombre de usuario.
     * @param password password asociado al usuario.
     */
    public TableData(int id, String application, String description, String userName,
            String password) {

        this.id = id;
        this.application = application;
        this.description = description;
        this.userName = getDataPaneInstance(userName);
        this.password = getDataPaneInstance(password);
        this.actions = getActionPaneInstance();

    }

    public void setUserName(String userName) {
        ((PasswordField) this.userName.getChildren().get(0)).setText(userName);
    }

    public void setPassword(String password) {
        ((PasswordField) this.password.getChildren().get(0)).setText(password);
    }

    /**
     * Genera una nueva instancia de un Stackpane, el cual contiene el 
     * Passwordfield e ImageView a mostrar en la celda.
     * 
     * @param value cadena de texto que contendrá la celda.
     * @return instancia de Stackpane.
     */
    private StackPane getDataPaneInstance(String value) {

        ImageView imgCopyToClipboard = ResourceManager.getImageView("copy-to-clipboard.png", 13);
        imgCopyToClipboard.setVisible(false);
        imgCopyToClipboard.setMouseTransparent(true);

        PasswordField pwdControl = new PasswordField();
        pwdControl.setText(value);
        pwdControl.setEditable(false);
        pwdControl.setBackground(Background.EMPTY);
        pwdControl.setPadding(new Insets(0, 0, 0, 0));
        pwdControl.setCursor(Cursor.DEFAULT);
        pwdControl.getStyleClass().add("not-selection-allowed");

        pwdControl.setOnMouseEntered(e -> imgCopyToClipboard.setVisible(true));
        pwdControl.setOnMouseExited(e -> imgCopyToClipboard.setVisible(false));
        pwdControl.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)
                    && e.getClickCount() == 2) {
                Utils.copyToClipboard(pwdControl.getText());
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pwdControl, imgCopyToClipboard);

        StackPane.setAlignment(imgCopyToClipboard, Pos.CENTER_RIGHT);

        return stackPane;
    }

    /**
     * Genera una nueva instancia de un HBox, el cual contien dos Buttons 
     * (edit / delete) a colocar en la columna de acciones.
     * 
     * @return nueva instancia de HBox.
     */
    private HBox getActionPaneInstance() {

        Button btnUpdate = new Button();
        btnUpdate.setTooltip(new Tooltip("Editar"));
        btnUpdate.setPadding(new Insets(2, 5, 2, 5));
        btnUpdate.setGraphic(ResourceManager.getImageView("action-update.gif", 12));
        btnUpdate.setOnAction(e -> System.out.println("Se modifica " + this.id));

        Button btnDelete = new Button();
        btnDelete.setPadding(new Insets(2, 5, 2, 5));
        btnDelete.setGraphic(ResourceManager.getImageView("action-delete.gif", 12));
        btnDelete.setOnAction(e -> System.out.println("Se elimina " + this.id));

        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(btnUpdate, btnDelete);

        return hbox;
    }

}
