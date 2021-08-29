package com.opencode.minikeyvault.controller;

import com.opencode.minikeyvault.model.Entry;
import com.opencode.minikeyvault.model.TableData;
import com.opencode.minikeyvault.service.Service;
import com.opencode.minikeyvault.service.impl.ServiceImpl;
import com.opencode.minikeyvault.utils.ImageFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** class: CtrlEntry. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-28 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class CtrlEntry implements Initializable {

    private Service service;

    @FXML BorderPane pnlInsUpdPanel;

    @FXML TextField txtApplication;
    @FXML TextField txtDescription;
    @FXML TextField txtUserName;
    @FXML PasswordField txtPassword;

    @FXML Button btnSave;
    @FXML Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        service = new ServiceImpl();

        btnSave.setGraphic(ImageFactory.getImageView("option-ok.png", 15));
        btnSave.setOnAction(e -> save(e));

        btnCancel.setGraphic(ImageFactory.getImageView("option-cancel.png", 15));
        btnCancel.setOnAction(e -> close());

    }

//    private ObservableList<TableData> observableList;
    private CtrlMenu parent;

    public void retrieve(CtrlMenu parent, ObservableList<TableData> observable, Entry entry) {
//        observableList = observable;
        this.parent = parent;
    }

    private void save(ActionEvent event) {

//        observableList.add(null);
        Entry entry = service.insert(new Entry(0, txtApplication.getText(), 
                txtDescription.getText(), txtUserName.getText(), txtPassword.getText()));

        if (entry != null) {
            parent.reloadDataNotification();
        }

        close();

    }

    private void close() {
        Stage stage = (Stage) pnlInsUpdPanel.getScene().getWindow();
        stage.close();
    }

}
