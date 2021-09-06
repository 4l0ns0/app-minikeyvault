package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** class: UserKeyInsUpdView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-28 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class UserKeyInsUpdView implements Initializable {

    private final UserKeyViewModel viewModel = UserKeyViewModel.getInstance();

    @FXML BorderPane bpnPrincipal;

    @FXML TextField txtApplication;
    @FXML TextField txtDescription;
    @FXML TextField txtUserName;
    @FXML PasswordField pwdPassword;
    @FXML TextField txtPassword;

    @FXML Button btnReveal;
    @FXML Button btnSave;
    @FXML Button btnCancel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtApplication.textProperty().bindBidirectional(viewModel.applicationProperty());
        txtDescription.textProperty().bindBidirectional(viewModel.descriptionProperty());
        txtUserName.textProperty().bindBidirectional(viewModel.userNameProperty());
        pwdPassword.textProperty().bindBidirectional(viewModel.passwordProperty());
        pwdPassword.setVisible(true);
        txtPassword.textProperty().bindBidirectional(viewModel.passwordProperty());
        txtPassword.setVisible(false);

        ImageFactory.setIcon(btnReveal, FontAwesome.FA_EYE, 12.0);
        btnReveal.setOnAction(e -> {
            txtPassword.setVisible(!txtPassword.isVisible());
            pwdPassword.setVisible(!pwdPassword.isVisible());
            
            ImageFactory.setIcon(btnReveal, (pwdPassword.isVisible()
                    ? FontAwesome.FA_EYE : FontAwesome.FA_EYE_SLASH), 12.0);
        });

        ImageFactory.setIcon(btnSave, FontAwesome.FA_FLOPPY_O);
        btnSave.setOnAction(this::save);
        btnSave.disableProperty().bind(
                Bindings.createBooleanBinding(() -> txtApplication.getText().trim().isEmpty(),
                        txtApplication.textProperty())
                .or(Bindings.createBooleanBinding(() -> txtUserName.getText().trim().isEmpty(),
                        txtUserName.textProperty()))
                .or(Bindings.createBooleanBinding(() -> txtPassword.getText().trim().isEmpty(),
                        txtPassword.textProperty()))
                .or(Bindings.createBooleanBinding(() -> pwdPassword.getText().trim().isEmpty(),
                        pwdPassword.textProperty()))
        );

        ImageFactory.setIcon(btnCancel, FontAwesome.FA_TIMES);
        btnCancel.setOnAction(this::close);

    }

    private void save(ActionEvent event) {

        viewModel.insUpd();
        close(null);

    }

    private void close(ActionEvent event) {

        viewModel.clean();
        Stage stage = (Stage) bpnPrincipal.getScene().getWindow();
        stage.close();

    }

}
