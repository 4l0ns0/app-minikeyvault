package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.utils.Utils;
import com.opencode.minikeyvault.view.commons.KeyDataRow;
import com.opencode.minikeyvault.view.dto.KeyData;
import com.opencode.minikeyvault.viewmodel.KeyDataViewModel;
import com.opencode.minikeyvault.viewmodel.KeyDataViewModel.OperationType;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * class: KeyDataMenuView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class KeyDataMenuView implements Initializable {

    private final KeyDataViewModel viewModel = KeyDataViewModel.getInstance();

    private Stage parentStage;
    private Tooltip tooltip;
    private PauseTransition tooltipTransition;

    @FXML BorderPane bpnPrincipal;

    @FXML CheckMenuItem chkAlwaysOnTop;
    @FXML CheckMenuItem chkFixItWhenCopyUser;
    @FXML CheckMenuItem chkFixItWhenCopyPassword;

    @FXML MenuItem mnuClose;
    @FXML MenuItem mnuBackup;
    @FXML MenuItem mnuHelp;
    @FXML MenuItem mnuAbout;

    @FXML TextField txtFilter;

    @FXML Button btnInsert;
    @FXML Button btnUpdate;
    @FXML Button btnDelete;
    @FXML ToggleButton btnFixit;
    @FXML Label lblTotalRecords;
    @FXML Label lblMessage;

    @FXML TableView<KeyDataRow> tblData;
    @FXML TableColumn<KeyDataRow, ?> tblColApplication;
    @FXML TableColumn<KeyDataRow, ?> tblColUserName;
    @FXML TableColumn<KeyDataRow, ?> tblColPassword;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tooltip = new Tooltip("Texto copiado");

        tooltipTransition = new PauseTransition(Duration.seconds(3));
        tooltipTransition.setOnFinished(event -> tooltip.hide());

        mnuClose.setOnAction(e -> parentStage.close());
        mnuBackup.setOnAction(e -> showDlgBackup());
        mnuAbout.setOnAction(e -> showDlgAbout());

        txtFilter.textProperty().bindBidirectional(viewModel.filterProperty());
        txtFilter.textProperty().addListener(
                (observable, oldValue, newValue) -> viewModel.fillObservableList(newValue));

        ImageFactory.setIcon(btnInsert, FontAwesome.FA_FILE_O);
        btnInsert.setOnAction(e -> showIupDialog(OperationType.INSERT));

        ImageFactory.setIcon(btnUpdate, FontAwesome.FA_PENSIL);
        btnUpdate.setOnAction(e -> showIupDialog(OperationType.UPDATE));
        btnUpdate.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tblData.getSelectionModel().getSelectedItem() == null, 
                tblData.getSelectionModel().selectedItemProperty()));

        ImageFactory.setIcon(btnDelete, FontAwesome.FA_TRASH_O);
        btnDelete.setOnAction(e -> showIupDialog(OperationType.DELETE));
        btnDelete.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tblData.getSelectionModel().getSelectedItem() == null, 
                tblData.getSelectionModel().selectedItemProperty()));

        ImageFactory.setIcon(btnFixit, FontAwesome.FA_TOGGLE_OFF);
        btnFixit.setTooltip(new Tooltip("Se activa al copiar valores \n"
                + "de la tabla (Ctrl + Dbl. Click)"));
        btnFixit.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    parentStage.setAlwaysOnTop(newValue);
                    
                    if (newValue.booleanValue()) {
                        btnFixit.setText("Bloquear");
                        ImageFactory.setIcon(btnFixit, FontAwesome.FA_TOGGLE_ON);
                    } else {
                        btnFixit.setText("Bloquear");
                        ImageFactory.setIcon(btnFixit, FontAwesome.FA_TOGGLE_OFF);
                    }
                });

        lblTotalRecords.textProperty().bind(Bindings.size((viewModel
                .getObservableList())).asString());

        lblMessage.textProperty().bind(viewModel.getUserMessage());

        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        tblData.setEditable(false);
        tblData.setFocusTraversable(false);
        tblData.setItems(viewModel.getObservableList());
        tblData.setRowFactory(tableView -> {

            final TableRow<KeyDataRow> tableRow = new TableRow<>();

            tableRow.hoverProperty().addListener(listener -> {

                final KeyDataRow keyDataRow = tableRow.getItem();

                if (tableRow.isHover() && keyDataRow != null) {
                    setCellBehavior(
                            (PasswordField) keyDataRow.getUserName()
                                .getChildren().get(0),
                            (Label) keyDataRow.getUserName()
                                .getChildren().get(1),
                            tableRow.getIndex());

                    setCellBehavior(
                            (PasswordField) keyDataRow.getPassword()
                                .getChildren().get(0),
                            (Label) keyDataRow.getPassword()
                                .getChildren().get(1),
                            tableRow.getIndex());

                }
            });

            return tableRow;
        });

        Platform.runLater(() -> txtFilter.requestFocus());

    }

    /**
     * Metodo que define el comportamiento de los botones alojados en las celdas de 
     * las columnas usuario y password.
     * 
     * @param passwordField objeto que contiene la información de la celda. 
     * @param image imagen que muestra el icono de copiar.
     * @param rowIndex fila a la que corresponden los valores.
     */
    private void setCellBehavior(PasswordField passwordField, Label image, int rowIndex) {

        passwordField.setOnMouseEntered(e -> image.setVisible(true));
        passwordField.setOnMouseExited(e -> image.setVisible(false));
        passwordField.setOnMousePressed(e -> {
            tblData.requestFocus();
            tblData.getSelectionModel().select(rowIndex);
        });
        passwordField.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) 
                    && e.getClickCount() == 2) {
                if (e.isControlDown()) {
                    btnFixit.setSelected(true);
                }

                Utils.copyToClipboard(passwordField.getText());
                showTooltipMessage(e.getScreenX(), e.getScreenY());
            }
        });

    }

    /**
     * Muestra el dialogo respectivo según el tipo de operación (Insert, Update o Delete) 
     * indicada.
     * 
     * @param operationType tipo de operación a realizar.
     */
    public void showIupDialog(OperationType operationType) {

        KeyData keyData = null;

        if (operationType == OperationType.UPDATE
                || operationType == OperationType.DELETE) {
            keyData = tblData.getSelectionModel().getSelectedItem().getKeyData();
        }

        btnFixit.setSelected(false);
        viewModel.setOperationType(operationType, keyData);

        if (operationType == OperationType.INSERT 
                || operationType == OperationType.UPDATE) {
            try {
                FXMLLoader loader = new FXMLLoader(ResourceManager
                        .getFxDialog("KeyDataInsUpdView"));

                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add("/styles/styles.css");

                Stage stage = new Stage();
                stage.setTitle(operationType == OperationType.INSERT 
                        ? "Nuevo Registro" : "Actualizar Registro");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.getIcons().add(ImageFactory.IMG_APP_ICON);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.DELETE) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Registro");
            alert.setHeaderText(null);
            alert.setContentText("Está seguro que desea eliminar el registro '" 
                    + keyData.getApplication() + "'?");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(ImageFactory.IMG_APP_ICON);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                viewModel.delete();
            }
        }

    }

    /**
     * Muestra el dialogo 'Acerca de...'
     */
    private void showDlgAbout() {

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager
                    .getFxDialog("AboutView"));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles/styles.css");

            Stage stage = new Stage();
            stage.setTitle("Acerca de Mini Key Vault");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(ImageFactory.IMG_APP_ICON);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Muestra el dialogo para generar / importar Backups.
     */
    private void showDlgBackup() {

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager
                    .getFxDialog("BackupView"));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles/styles.css");

            Stage stage = new Stage();
            stage.setTitle("Generar / Restaurar Backup");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(ImageFactory.IMG_APP_ICON);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Muestra el tooltip con el mensaje de 'Texto copiado'
     * e inicia la transición para ocultar el dialogo.
     * 
     * @param x posición horizontal.
     * @param y posición vertical.
     */
    private void showTooltipMessage(double x, double y) {

        hideTooltipMessage();

        tooltip.show(parentStage, x + 10.0, y + 5.0);
        tooltipTransition.play();

    }

    /**
     * Oculta el tooltip con el mensaje de 'Texto copiado'
     * y detiene transición para ocultar el dialogo. 
     */
    private void hideTooltipMessage() {
        tooltip.hide();
        tooltipTransition.stop();
    }
    
    /**
     * Metodo para recibir el Stage desde la clase que invoca la vista.
     * 
     * @param stage stage al que pertenece la vista.
     */
    public void setStage(Stage stage) {

        parentStage = stage;
        parentStage.focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        btnFixit.setSelected(false);
                    } else {
                        hideTooltipMessage();
                    }
                });

    }

}
