package com.opencode.minikeyvault;

import com.opencode.minikeyvault.model.db.Db;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.view.UserKeyShowView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * class: MainApp. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class MainApp extends Application {

    public MainApp() {
        Db.init();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(ResourceManager.getFxView("UserKeyShowView"));
        Parent root = loader.load();

        UserKeyShowView view = loader.getController();
        view.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");
        stage.setTitle(Constants.APP_NAME);
        stage.getIcons().add(ImageFactory.IMG_APP_ICON);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
