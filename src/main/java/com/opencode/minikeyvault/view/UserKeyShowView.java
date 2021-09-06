package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.utils.Utils;
import com.opencode.minikeyvault.view.commons.TableData;
import com.opencode.minikeyvault.view.dto.KeyData;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel.OperationType;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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

/**
 * class: UserKeyShowView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class UserKeyShowView implements Initializable {

    private final UserKeyViewModel viewModel = UserKeyViewModel.getInstance();

    private Stage parentStage;

    @FXML BorderPane bpnPrincipal;

    @FXML CheckMenuItem chkAlwaysOnTop;
    @FXML CheckMenuItem chkFixItWhenCopyUser;
    @FXML CheckMenuItem chkFixItWhenCopyPassword;
    @FXML MenuItem mnuClose;

    @FXML TextField txtFilter;

    @FXML Button btnInsert;
    @FXML Button btnUpdate;
    @FXML Button btnDelete;
    @FXML ToggleButton btnFixit;
    @FXML Label lblTotalRecords;
    @FXML Label lblMessage;

    @FXML TableView<TableData> tblData;
    @FXML TableColumn<TableData, ?> tblColApplication;
    @FXML TableColumn<TableData, ?> tblColUserName;
    @FXML TableColumn<TableData, ?> tblColPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ImageFactory.setIcon(mnuClose, FontAwesome.FA_FILE_O, 12.0);
        mnuClose.setOnAction(e -> showDialog(OperationType.INSERT));

        txtFilter.textProperty().bindBidirectional(viewModel.filterProperty());
        txtFilter.textProperty().addListener(
                (observable, oldValue, newValue) -> viewModel.fillObservableList(newValue));

        ImageFactory.setIcon(btnInsert, FontAwesome.FA_FILE_O);
        btnInsert.setOnAction(e -> showDialog(OperationType.INSERT));

        ImageFactory.setIcon(btnUpdate, FontAwesome.FA_PENSIL);
        btnUpdate.setOnAction(e -> showDialog(OperationType.UPDATE));
        btnUpdate.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tblData.getSelectionModel().getSelectedItem() == null, 
                tblData.getSelectionModel().selectedItemProperty()));

        ImageFactory.setIcon(btnDelete, FontAwesome.FA_TRASH_O);
        btnDelete.setOnAction(e -> showDialog(OperationType.DELETE));
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
                        btnFixit.setText("Fijado: Si");
                        ImageFactory.setIcon(btnFixit, FontAwesome.FA_TOGGLE_ON);
                    } else {
                        btnFixit.setText("Fijado: No");
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

            final TableRow<TableData> tableRow = new TableRow<>();

            tableRow.hoverProperty().addListener(listener -> {
                
                final TableData tableData = tableRow.getItem();

                if (tableRow.isHover() && tableData != null) {
                    setCellBehavior(
                            (PasswordField) tableData.getUserName()
                                .getChildren().get(0),
                            (Label) tableData.getUserName()
                                .getChildren().get(1),
                            tableRow.getIndex());

                    setCellBehavior(
                            (PasswordField) tableData.getPassword()
                                .getChildren().get(0),
                            (Label) tableData.getPassword()
                                .getChildren().get(1),
                            tableRow.getIndex());

                }
            });

            return tableRow;
        });

        Platform.runLater(() -> txtFilter.requestFocus());

    }

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
                Utils.copyToClipboard(passwordField.getText());

                if (e.isControlDown()) {
                    btnFixit.setSelected(true);
                }
            }
        });

    }
    
    /**
     * Muestra el dialogo respectivo según el tipo de operación indicada.
     * 
     * @param operationType tipo de operación a realizar.
     */
    public void showDialog(OperationType operationType) {

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
                        .getFxDialog("UserKeyInsUpdView"));

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
                    }
                });

    }

}
