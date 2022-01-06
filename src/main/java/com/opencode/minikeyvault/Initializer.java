package com.opencode.minikeyvault;

import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.view.KeyDataMenuView;
import com.opencode.minikeyvault.viewmodel.InitViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * class: Initializer. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class Initializer extends Application {

    private final InitViewModel viewModel = InitViewModel.getInstance();

    /**
     * Constructor.
     */
    public Initializer() {
        viewModel.checkAppIco();
        viewModel.checkConfigFile();
        viewModel.checkDatabase();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(ResourceManager.getFxView("KeyDataMenuView"));

        Parent root = loader.load();

        KeyDataMenuView view = loader.getController();
        view.setStage(stage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(ResourceManager.getCssStyle("styles.css").toString());

        stage.setTitle(Constants.APP_NAME);
        stage.getIcons().add(ImageFactory.IMG_APP_ICON);
        stage.setScene(scene);
        stage.setMinWidth(569); // de acuerdo al fxml
        stage.setMinHeight(277); // de acuerdo al fxml
        stage.show();

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

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
