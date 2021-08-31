package com.opencode.minikeyvault;

import com.opencode.minikeyvault.model.db.Db;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ResourceManager;
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

        Parent root = FXMLLoader.load(ResourceManager.getFxView("UserKeyShowView"));

        //FXMLLoader loader = new FXMLLoader(ResourceManager.getFxView("UserKeyShowView"));
        //Parent root = loader.load();
        //UserKeyShowView controller = loader.getController();
        //controller.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle(Constants.APP_NAME);
        stage.getIcons().add(Constants.IMG_APP_ICON);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
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
