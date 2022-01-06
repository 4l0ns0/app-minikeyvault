package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ResourceManager;
import java.awt.Desktop;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/** 
 * class: AboutView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-10 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class AboutView implements Initializable {

    @FXML BorderPane bpnPrincipal;

    @FXML Label txtTitle;
    @FXML Label txtVersion;
    @FXML Label txtDetail;
    @FXML Hyperlink lnkRepository;
    @FXML Label txtDevBy;
    @FXML Button btnClose;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        String name = "";
        String version = "";
        String url = "";

        try {
            Properties properties = new Properties();
            properties.load(ResourceManager.getResourceAsStream(null, "app-info.properties"));

            name = properties.getProperty("name");
            version = properties.getProperty("version");
            url = properties.getProperty("url");
        } catch (Exception e) {
            log.error("Ocurrio un error al capturar la información de la "
                    + "aplicación desde el 'app-info.properties': " + e.getMessage());
        }

        txtTitle.setText(name + " para el almacenamiento de credenciales.");
        txtVersion.setText("Última version: " + version);
        txtDetail.setText(new StringBuilder()
                .append("Este producto puede ser usado de forma gratuita con fines personales ")
                .append("y/o empresariales. A su vez, este puede ser compartido, prestado y ")
                .append("distribuido sin límite alguno, pero siempre respetando la autoría del ")
                .append("mismo. Para fines de revisión o colaboración, el código fuente ")
                .append("se encuentra disponible en el siguiente repositorio:").toString());
        lnkRepository.setText(url);
        txtDevBy.setText("Desarrollado por: Henry Navarro (henrynavarro.pe@gmail.com)");

        lnkRepository.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URL(lnkRepository.getText()).toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnClose.setOnAction(e -> {
            Stage stage = (Stage) bpnPrincipal.getScene().getWindow();
            stage.close();
        });

    }

}
