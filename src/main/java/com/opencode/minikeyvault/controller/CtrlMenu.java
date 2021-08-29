package com.opencode.minikeyvault.controller;

import com.opencode.minikeyvault.model.TableData;
import com.opencode.minikeyvault.service.Service;
import com.opencode.minikeyvault.service.impl.ServiceImpl;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ResourceManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
 * class: CtrlMenu. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
//@Slf4j
public class CtrlMenu implements Initializable {

    private Service service;
    
    private List<TableData> lstData;
    private ObservableList<TableData> observableList;
    private Stage parentStage;

    @FXML BorderPane pnlMenuPanel;
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

        service = new ServiceImpl();

        chkAlwaysOnTop.setSelected(true);
        chkAlwaysOnTop.setOnAction(
                e -> fixWindow(chkAlwaysOnTop.isSelected()));

        txtFilter.textProperty().addListener(
                (observable, oldValue, newValue) -> fillTable(newValue));

        btnNew.setGraphic(ImageFactory.getImageView("action-create.png", 15));
        btnNew.setOnAction(e -> showInsUpdDialog(true));

        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tblColActions.setCellValueFactory(new PropertyValueFactory<>("actions"));

        lstData = service.retrieveData();
        observableList = FXCollections.observableArrayList();
        tblData.setItems(observableList);

        fillTable(null);

    }

    /**
     * Metodo que define si la ventana siempre se mostrará encima o no.
     * 
     * @param keepOnTop true si se quiere que siempre esté visible. Caso contrario, false.
     */
    private void fixWindow(boolean keepOnTop) {

        if (parentStage == null) {
            parentStage = (Stage) pnlMenuPanel.getScene().getWindow();
        }

        parentStage.setAlwaysOnTop(keepOnTop);
    }

    /**
     * Metodo para cargar el observable (ObservableList) con la información 
     * del Keystore capturada al iniciar el controlador.
     * 
     * @param paramFilter si null, se carga toda la informacion del keystore
     *     en el observable. Caso contrario, se aplica el filtro indicado antes
     *     de pasarlo al observable.
     */
    private void fillTable(String paramFilter) {

        if (paramFilter != null) {
            observableList.setAll(lstData.stream()
                    .filter(e -> e.getApplication().toLowerCase()
                            .contains(paramFilter.trim().toLowerCase()))
                    .collect(Collectors.toList()));
        } else {
            observableList.setAll(lstData/*service.retrieveData()*/);
        }

    }
    
    private void showInsUpdDialog(boolean forInsert) {

        try {
//            Parent root = FXMLLoader.load(ResourceManager.getFxDialog("EntryInsUpd.fxml"));

            FXMLLoader loader = new FXMLLoader(ResourceManager.getFxDialog("EntryInsUpd"));

            Scene scene = new Scene(loader.load()/*, 300, 200*/);
            scene.getStylesheets().add("/styles/styles.css");

            CtrlEntry ctrlEntry = loader./*<CtrlEntry>*/getController();
            ctrlEntry.retrieve(this, observableList, null);

            Stage stage = new Stage();
            stage.setTitle(forInsert ? "Nuevo Registro" : "Modificar Registro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(ImageFactory.getApplicationIcon());
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void reloadDataNotification() {
        lstData = service.retrieveData();
        fillTable(txtFilter.getText());
    }

}
