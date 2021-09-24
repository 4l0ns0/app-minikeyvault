package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.model.db.Datasource;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/** class: BackupView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-11 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class BackupView implements Initializable {

    @FXML TextField txtGeneratePath;
    @FXML Button btnGenerate;
    @FXML TextField txtRestorePath;
    @FXML Button btnRestore;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        btnGenerate.setOnAction(e -> Datasource.generateBackup());

    }

}
