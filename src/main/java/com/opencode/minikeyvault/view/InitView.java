package com.opencode.minikeyvault.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/** class: InitView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-12 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class InitView implements Initializable {

    @FXML TextField txtUserName;
    @FXML TextField txtPassword;
    @FXML Button btnLogin;
    @FXML CheckBox chkRemerber;
    @FXML Button btnReveal;
    @FXML PasswordField pwdPassword;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }

}
