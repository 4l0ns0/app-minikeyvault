package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.InitStatus;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.viewmodel.LoginViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

/** class: LoginView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-12 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class LoginView implements Initializable {

    private final LoginViewModel loginViewModel = LoginViewModel.getInstance();

    private Stage parentStage;
    private InitStatus initStatus;

    @FXML BorderPane bpnPrincipal;

    @FXML TextField txtUserName;
    @FXML TextField txtPassword;

    @FXML PasswordField pwdPassword;

    @FXML CheckBox chkRemember;

    @FXML Button btnReveal;
    @FXML Button btnOk;

    @FXML Label lblTitle;
    @FXML Label lblMessage;

    public LoginView() {
        initStatus = loginViewModel.checkInit();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        txtUserName.textProperty().bindBidirectional(loginViewModel.userNameProperty());

        pwdPassword.textProperty().bindBidirectional(loginViewModel.passwordProperty());
        pwdPassword.setVisible(true);

        txtPassword.textProperty().bindBidirectional(loginViewModel.passwordProperty());
        txtPassword.setVisible(false);

        ImageFactory.setIcon(btnReveal, ImageFactory.FontAwesome.FA_EYE, 12.0);
        btnReveal.setOnAction(e -> {
            txtPassword.setVisible(!txtPassword.isVisible());
            pwdPassword.setVisible(!pwdPassword.isVisible());

            ImageFactory.setIcon(btnReveal, pwdPassword.isVisible()
                    ? ImageFactory.FontAwesome.FA_EYE : ImageFactory.FontAwesome.FA_EYE_SLASH, 12.0);
        });

        ImageFactory.setIcon(btnOk, ImageFactory.FontAwesome.FA_SIGN_IN);
        btnOk.setOnAction(e -> execute());
        btnOk.disableProperty()
                .bind(Bindings.createBooleanBinding(
                                () -> StringUtils.isBlank(txtUserName.getText()),
                                txtUserName.textProperty())
                        .or(Bindings.createBooleanBinding(
                                () -> StringUtils.isBlank(txtPassword.getText()),
                                txtPassword.textProperty())));

    }

    /**
     * Método que ejecuta las acciones
     */
    private void execute() {

        String error = null;

        switch (initStatus) {
            case INITIALIZATION_REQUIRED:
                error = loginViewModel.initDatabase(txtUserName.getText(), pwdPassword.getText());
                break;
            case LOGIN_REQUIRED:
            case AUTHENTICATION_ERROR:
                error = loginViewModel.openDatabase(txtUserName.getText(), pwdPassword.getText());
                break;
        }

        if (error == null) {
            if (chkRemember.isSelected()) {
                loginViewModel.persistCredentials(txtUserName.getText(), pwdPassword.getText());
            }

            try {
                loadMain();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblMessage.setText(error);
        }

    }

    /**
     * Metodo para recibir el Stage desde la clase que invoca la vista.
     *
     * @param stage stage al que pertenece la vista.
     */
    public void setStage(Stage stage) throws IOException {

        parentStage = stage;

        if (initStatus == InitStatus.AUTO_LOGIN_AVAILABLE) {
            loadMain();
        } else {
            switch (initStatus) {
                case INITIALIZATION_REQUIRED:
                    lblTitle.setText("Crear usuario");

                    btnOk.setText("Crear");

                    chkRemember.setVisible(false);
                    chkRemember.setSelected(false);
                    break;
                case LOGIN_REQUIRED:
                    lblTitle.setText("Iniciar sesión");

                    btnOk.setText("Ingresar");

                    chkRemember.setVisible(true);
                    chkRemember.setSelected(false);
                    break;
                case AUTHENTICATION_ERROR:
                    lblTitle.setText("Credenciales incorrectas");

                    btnOk.setText("Ingresar");

                    chkRemember.setVisible(true);
                    chkRemember.setSelected(true);
                    break;
            }

            Scene scene = new Scene(bpnPrincipal);
            scene.getStylesheets().add(ResourceManager.getCssStyle("styles.css").toString());

            showStage(scene);
        }

    }

    private void loadMain() throws IOException {

        FXMLLoader loader = new FXMLLoader(ResourceManager.getFxView("Main"));
        Parent mainViewRoot = loader.load();

        MainView view = loader.getController();
        view.setStage(parentStage);

        Scene scene = new Scene(mainViewRoot);
        scene.getStylesheets().add(ResourceManager.getCssStyle("styles.css").toString());

        showStage(scene);

    }

    private void showStage(Scene scene) {

        parentStage.setTitle(Constants.APP_NAME);
        parentStage.getIcons().add(ImageFactory.IMG_APP_ICON);
        parentStage.setScene(scene);

        parentStage.show();

        parentStage.setMinWidth(parentStage.getWidth());
        parentStage.setMinHeight(parentStage.getHeight());
        parentStage.setResizable(initStatus == InitStatus.AUTO_LOGIN_AVAILABLE);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        parentStage.setX((primScreenBounds.getWidth() - parentStage.getWidth()) / 2);
        parentStage.setY((primScreenBounds.getHeight() - parentStage.getHeight()) / 2);

    }

}
