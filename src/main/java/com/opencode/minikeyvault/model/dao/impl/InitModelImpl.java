package com.opencode.minikeyvault.model.dao.impl;

import com.opencode.minikeyvault.model.dao.InitModel;
import com.opencode.minikeyvault.model.db.Datasource;
import com.opencode.minikeyvault.utils.ConfigFile;

/** class: InitModelImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-11 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class InitModelImpl implements InitModel {

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
