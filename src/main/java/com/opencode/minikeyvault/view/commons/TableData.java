package com.opencode.minikeyvault.view.commons;

import com.opencode.minikeyvault.domain.UserKey;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.Utils;
import com.opencode.minikeyvault.view.UserKeyShowView;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel.OperationType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
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

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private UserKey userKey;

    private int id;
    private String application;
    private String description;

    @Setter(value = AccessLevel.NONE)
    private StackPane userName;

    @Setter(value = AccessLevel.NONE)
    private StackPane password;

    @Setter(value = AccessLevel.NONE)
    private HBox actions;

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

        ImageView imgCopy = new ImageView(Constants.IMG_COPY_TO_CLIPBOARD);
        imgCopy.setFitHeight(14);
        imgCopy.setPreserveRatio(true);
        imgCopy.setVisible(false);
        imgCopy.setOpacity(0.5);
        imgCopy.setMouseTransparent(true);

        PasswordField pwdControl = new PasswordField();
        pwdControl.setText(value);
        pwdControl.setEditable(false);
        pwdControl.setBackground(Background.EMPTY);
        pwdControl.setPadding(new Insets(0, 0, 0, 0));
        pwdControl.setCursor(Cursor.DEFAULT);
        pwdControl.getStyleClass().add("not-selection-allowed");

        pwdControl.setOnMouseEntered(e -> imgCopy.setVisible(true));
        pwdControl.setOnMouseExited(e -> imgCopy.setVisible(false));
        pwdControl.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)
                    && e.getClickCount() == 2) {
                Utils.copyToClipboard(pwdControl.getText());
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(pwdControl, imgCopy);

        StackPane.setAlignment(imgCopy, Pos.CENTER_RIGHT);

        return stackPane;
    }

    /**
     * Genera una nueva instancia de un HBox, el cual contien los botones 
     * (edit / delete) a colocar en la columna de acciones.
     * 
     * @return nueva instancia de HBox.
     */
    private HBox getActionPaneInstance() {

        ImageView imgEdit = new ImageView(Constants.IMG_UPDATE);
        imgEdit.setFitHeight(12);
        imgEdit.setPreserveRatio(true);

        Button btnUpdate = new Button();
        btnUpdate.setPadding(new Insets(2, 5, 2, 5));
        btnUpdate.setGraphic(imgEdit);
        btnUpdate.setOnAction(e -> UserKeyShowView.showInsUpdDialog(
                OperationType.UPDATE, this.userKey));

        ImageView imgDelete = new ImageView(Constants.IMG_DELETE);
        imgDelete.setFitHeight(12);
        imgDelete.setPreserveRatio(true);

        Button btnDelete = new Button();
        btnDelete.setPadding(new Insets(2, 5, 2, 5));
        btnDelete.setGraphic(imgDelete);
        btnDelete.setOnAction(e -> UserKeyShowView.showDeleteDialog(userKey));

        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(btnUpdate, btnDelete);

        return hbox;
    }
    
}
