package com.opencode.minikeyvault.model.dao.impl;

import com.opencode.minikeyvault.model.dao.InitModel;
import com.opencode.minikeyvault.model.db.Datasource;
import com.opencode.minikeyvault.utils.ConfigFile;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.ResourceManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;

/** class: InitModelImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-11 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class InitModelImpl implements InitModel {

    @Override
    public boolean initAppIcon() {

        boolean result = false;

        try {
            Files.copy(ResourceManager.getResourceAsStream("images", Constants.APP_ICON_FILE), 
                    Paths.get(Constants.APP_ICON_NAME), StandardCopyOption.REPLACE_EXISTING);
            result = true;
        } catch (Exception e) {
            log.error("No generar el icono de la aplicación: {}", e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * Metodo para inicializar el archivo de propiedades.
     */
    @Override
    public boolean initConfigFile() {
        return ConfigFile.init();
    }

    /**
     * Metodo para inicializar la base de datos.
     */
    @Override
    public boolean initDatabase() {
        return Datasource.init();
    }

}
