package com.opencode.minikeyvault.viewmodel;

import com.opencode.minikeyvault.model.dao.InitModel;
import com.opencode.minikeyvault.model.dao.impl.InitModelImpl;
import com.opencode.minikeyvault.utils.ConfigFile;
import com.opencode.minikeyvault.utils.Constants;
import com.opencode.minikeyvault.utils.Constants.ResultType;
import com.opencode.minikeyvault.view.dto.InitDetail;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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

    private final InitModel initModel = new InitModelImpl();

    private ResultType appIconStatus;
    private ResultType configFileStatus;
    private ResultType databaseStatus;

    private InitDetail initDetail;

    /**
     * Constructor.
     */
    private InitViewModel() {
        
    }

    /**
     * Verifica la existencia del icono de la aplicación en el directorio. 
     */
    public ResultType checkAppIco() {

        if (new File(Constants.APP_ICON_NAME).exists()) {
            appIconStatus = ResultType.ALREADY_INITIALIZED;
        } else {
            appIconStatus = initModel.initAppIcon() 
                    ? ResultType.INITIALIZED : ResultType.ERROR_ON_INITIALIZATION;
        }

        return appIconStatus;
    }
    
    /**
     * Verifica la existencia y correcta configuración del archivo de 
     * configuración. En caso este no exista, crea el archivo. 
     */
    public ResultType checkConfigFile() {

        if (new File(Constants.PROP_FILENAME).exists()) {
            // XXX aqui se podría añadir una revision de los atributos del properties
            configFileStatus = ResultType.ALREADY_INITIALIZED;
        } else {
            configFileStatus = initModel.initConfigFile() 
                    ? ResultType.INITIALIZED : ResultType.ERROR_ON_INITIALIZATION;
        }

        return configFileStatus;
    }

    /**
     * Verifica la existencia y correcta configuración de la base de datos. 
     * En caso el archivo no exista, crea la base de datos.
     */
    public ResultType checkDatabase() {

        if (new File(Constants.DB_FILENAME).exists()) {
            // XXX aqui se podría añadir una revision del estado de la bd
            databaseStatus = ResultType.ALREADY_INITIALIZED;
        } else {
            databaseStatus = initModel.initDatabase() 
                    ? ResultType.INITIALIZED : ResultType.ERROR_ON_INITIALIZATION;
        }

        return databaseStatus;
    }
    
    /**
     * Recupera el detalle del proceso de inicialización en caso exista.
     * 
     * @return instancia de la clase con el detalle de la inicialización.
     */
    public InitDetail retrieveInitDetail() {

        if (configFileStatus == ResultType.INITIALIZED 
                && databaseStatus == ResultType.INITIALIZED) {

            Map<String, String> mapDetail = new HashMap<>();
            mapDetail.put("Usuario", ConfigFile.getValue(Constants.PROP_KEY_DB_USERNAME));
            mapDetail.put("Password", ConfigFile.getValue(Constants.PROP_KEY_DB_PASSWORD));

            initDetail = InitDetail.builder()
                    .message("Se han inicializado los componentes del sistema. "
                            + "Tome nota del usuario y clave de la base de datos "
                            + "y guardelos en un lugar seguro.")
                    .detail(mapDetail)
                    .build();
        }

        return initDetail;
    }

}
