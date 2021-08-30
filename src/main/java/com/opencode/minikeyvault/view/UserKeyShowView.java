package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.view.commons.TableData;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

        btnNew.setGraphic(ResourceManager.getImageView("action-insert.png", 18));
        btnNew.setOnAction(e -> showInsUpdDialog(true));
        
        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tblColActions.setCellValueFactory(new PropertyValueFactory<>("actions"));

        tblData.setItems(viewModel.getObservableList());

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

    private void showInsUpdDialog(boolean forInsert) {

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager.getFxDialog("UserKeyInsUpdView"));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles/styles.css");

//            UserKeyInsUpdView userKeyInsUpdView = loader.getController();
//            userKeyInsUpdView.retrieve(this, null/*observableList*/, null);//TODO pasa el viewModel

            Stage stage = new Stage();
            stage.setTitle(forInsert ? "Nuevo Registro" : "Modificar Registro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(ResourceManager.getImage("app-icon.png"));
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
