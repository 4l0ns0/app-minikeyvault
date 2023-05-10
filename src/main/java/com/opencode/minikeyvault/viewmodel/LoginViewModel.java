package com.opencode.minikeyvault.viewmodel;

import com.opencode.minikeyvault.model.dao.MngDb;
import com.opencode.minikeyvault.model.dao.impl.LocalDbImpl;
import com.opencode.minikeyvault.utils.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * class: LoginViewModel. <br/>
 *
 * @author Henry Navarro <br/><br/>
 * <u>Cambios</u>:<br/>
 * <ul>
 * <li>2021-09-11 Creación del proyecto.</li>
 * </ul>
 * @version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginViewModel {

    private static LoginViewModel instance;

    /**
     * Metodo para obtener la instancia de clase.
     *
     * @return instancia de la clase.
     */
    @Synchronized
    public static LoginViewModel getInstance() {

        if (instance == null) {
            instance = new LoginViewModel();
        }

        return instance;
    }

    private final MngDb mngDb = new LocalDbImpl();

    private StringProperty userName = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");

    /**
     * Método que verifica el estado de inicialización del sistema.
     *
     * @return identificador con el estado del resultado de la validación.
     */
    public InitStatus checkInit() {

        if (!new File(Constants.APP_ICON_NAME).exists()) {
            try {
                Files.copy(ResourceManager.getResourceAsStream("images", Constants.APP_ICON_FILE),
                        Path.of(Constants.APP_ICON_NAME), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                log.error("No se generó el icono de la aplicación: {}", e.getMessage());
            }
        }

        ConfigFile.init();
        SecurityFile.init();

        if (!mngDb.exists()) {
            return InitStatus.INITIALIZATION_REQUIRED;
        } else {
            if (!ConfigFile.exists()) {
                return InitStatus.LOGIN_REQUIRED;
            } else {
                String encodedAuth = ConfigFile.getAuth();

                if (StringUtils.isBlank(encodedAuth)) {
                    return InitStatus.LOGIN_REQUIRED;
                } else {
                    try {
                        Encrypt.Authenticate auth = Encrypt.decryptCredentials(
                                SecurityFile.getSecret(), encodedAuth);

                        mngDb.openDatabase(auth.getUsername(), auth.getPassword());
                    } catch (Exception e) {
                        log.error("No se pudo aperturar la base de datos con las credenciales " +
                                "alojadas en el archivo de configuración: {}", e.getMessage());

                        return InitStatus.AUTHENTICATION_ERROR;
                    }

                    return InitStatus.AUTO_LOGIN_AVAILABLE;
                }
            }
        }

    }

    /**
     * Método que inicializa la base de datos.
     *
     * @param username usuario con el que se creará la base de datos.
     * @param password clave con la que se creará la base de datos.
     * @return descripción en caso de error, caso contrario null.
     */
    public String initDatabase(String username, String password) {
        return mngDb.initDatabase(username, password);
    }

    /**
     * Método que apertura la base de datos, la cual debe estar previamente inicializada.
     *
     * @param username usuario con el que se creó la base de datos.
     * @param password clave con la que se creó la base de datos.
     * @return descripción en caso de error, caso contrario null.
     */
    public String openDatabase(String username, String password) {
        return mngDb.openDatabase(username, password);
    }

    /**
     * Guarda las credenciales de autenticación en el archivo de configuración.
     *
     * @param username nombre de usuario.
     * @param password clave de usuario.
     */
    public void persistCredentials(String username, String password) {

        try {
            byte[] secureRandom = Encrypt.getSecureRandom();

            if (SecurityFile.setSecret(secureRandom)) {
                ConfigFile.setAuth(Encrypt.encryptCredentials(secureRandom, username, password));
            }
        } catch (Exception e) {
            log.error("No se pudo persistir las credenciales: {}", e.getMessage());
        }

    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty passwordProperty() {
        return password;
    }

}
