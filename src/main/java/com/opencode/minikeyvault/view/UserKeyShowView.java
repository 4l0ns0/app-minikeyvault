package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.domain.UserKey;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.view.commons.TableData;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel.OperationType;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    @FXML TextField txtFilter;
    @FXML Button btnNew;
    @FXML TableView<TableData> tblData;
    @FXML TableColumn<TableData, ?> tblColApplication;
    @FXML TableColumn<TableData, ?> tblColUserName;
    @FXML TableColumn<TableData, ?> tblColPassword;
    @FXML TableColumn<TableData, ?> tblColActions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chkAlwaysOnTop.setSelected(true);
        chkAlwaysOnTop.setOnAction(
                e -> fixWindow(chkAlwaysOnTop.isSelected()));

        txtFilter.textProperty().bindBidirectional(viewModel.filterProperty());
        txtFilter.textProperty().addListener(
                (observable, oldValue, newValue) -> viewModel.fillObservableList(newValue));

        ImageView imgInsert = new ImageView(Constants.IMG_INSERT);
        imgInsert.setFitHeight(18);
        imgInsert.setPreserveRatio(true);

        btnNew.setGraphic(imgInsert);
        btnNew.setOnAction(e -> showInsUpdDialog(OperationType.INSERT, null));

        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tblColActions.setCellValueFactory(new PropertyValueFactory<>("actions"));

        tblData.setItems(viewModel.getObservableList());

    }

    public void setStage(Stage stage) {
        parentStage = stage;
    }

    /**
     * Metodo que define si la ventana siempre se mostrará encima o no.
     * 
     * @param keepOnTop true si se quiere que siempre esté visible. Caso contrario, false.
     */
    private void fixWindow(boolean keepOnTop) {

        if (parentStage == null) {
            parentStage = (Stage) bpnPrincipal.getScene().getWindow();
        }

        parentStage.setAlwaysOnTop(keepOnTop);

    }

    /**
     * Metodo para cargar en el Stage el dialogo (UserKeyInsUpdView) el cual es 
     * empleado para crear o modificar un UserKey.
     * 
     * @param operationType tipo de operacion (OperationType.INSERT / OperationType.UPDATE) 
     *     que se va a ejecutar.
     * @param userKey instancia del UserKey en caso el tipo de operación se OperationType.UPDATE. 
     *     Null en caso la operación sea OperationType.INSERT.
     */
    public static void showInsUpdDialog(OperationType operationType, UserKey userKey) {

        UserKeyViewModel.getInstance().setOperationType(operationType, userKey);

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager.getFxDialog("UserKeyInsUpdView"));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles/styles.css");

            Stage stage = new Stage();
            stage.setTitle(operationType == OperationType.INSERT 
                    ? "Nuevo Registro" : "Actualizar Registro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(Constants.IMG_APP_ICON);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para cargar en el Stage el dialogo (UserKeyInsUpdView) el cual es 
     * empleado para crear o modificar un UserKey.
     * 
     * @param userKey instancia del UserKey.
     */
    public static void showDeleteDialog(UserKey userKey) {

        UserKeyViewModel viewModel = UserKeyViewModel.getInstance();
        viewModel.setOperationType(OperationType.DELETE, userKey);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Registro");
        alert.setHeaderText(null);
        alert.setContentText("Está seguro que desea eliminar el registro?");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(Constants.IMG_APP_ICON);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            viewModel.delete();
        }

    }

}
