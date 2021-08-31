package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    @FXML PasswordField txtPassword;

    @FXML Button btnSave;
    @FXML Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtApplication.textProperty().bindBidirectional(viewModel.applicationProperty());
        txtDescription.textProperty().bindBidirectional(viewModel.descriptionProperty());
        txtUserName.textProperty().bindBidirectional(viewModel.userNameProperty());
        txtPassword.textProperty().bindBidirectional(viewModel.passwordProperty());

        ImageView imgOk = new ImageView(Constants.IMG_OK);
        imgOk.setFitHeight(15);
        imgOk.setPreserveRatio(true);

        btnSave.setGraphic(imgOk);
        btnSave.setOnAction(this::save);

        ImageView imgCancel = new ImageView(Constants.IMG_CANCEL);
        imgCancel.setFitHeight(15);
        imgCancel.setPreserveRatio(true);

        btnCancel.setGraphic(imgCancel);
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
