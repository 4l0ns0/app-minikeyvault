package com.opencode.minikeyvault.viewmodel;

import com.opencode.minikeyvault.model.dao.InitModel;
import com.opencode.minikeyvault.model.dao.impl.InitModelImpl;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.Constants.ResultType;
import java.io.File;
import lombok.Synchronized;

/** class: InitViewModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-11 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class InitViewModel {

    private static InitViewModel instance;

    /**
     * Metodo para obtener la instancia de clase.
     * 
     * @return instancia de la clase.
     */
    @Synchronized
    public static InitViewModel getInstance() {

        if (instance == null) {
            instance = new InitViewModel();
        }

        return instance;
    }

    private InitModel initModel = new InitModelImpl();

    /**
     * Constructor.
     */
    private InitViewModel() {
        
    }

    /**
     * Verifica la existencia y correcta configuración del archivo de 
     * configuración. En caso no exista, lo crea. 
     */
    public ResultType checkConfigFile() {

        ResultType result = null;

        if (new File(Constants.PROP_FILENAME).exists()) {
            // XXX aqui se podría añadir una revision de los atributos del properties
            result = ResultType.ALREADY_INITIALIZED;
        } else {
            result = initModel.initConfigFile() 
                    ? ResultType.INITIALIZED : ResultType.ERROR_ON_INITIALIZATION;
        }

        return result;
    }

    /**
     * Verifica la existencia y correcta configuración de la base de datos. 
     * En caso no exista, la crea.
     */
    public ResultType checkDatabase() {

        ResultType result = null;

        if (new File(Constants.DB_FILENAME).exists()) {
            // XXX aqui se podría añadir una revision del estado de la bd
            result = ResultType.ALREADY_INITIALIZED;
        } else {
            result = initModel.initDatabase() 
                    ? ResultType.INITIALIZED : ResultType.ERROR_ON_INITIALIZATION;
        }

        return result;
    }

}
