package com.opencode.minikeyvault.viewmodel.converter;

import com.opencode.minikeyvault.model.entity.UserKey;
import com.opencode.minikeyvault.view.dto.KeyData;
import com.opencode.minikeyvault.viewmodel.KeyDataViewModel;

/** class: KeyDataConverter. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-30 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class KeyDataConverter {

    private KeyDataConverter() {
        throw new IllegalStateException(KeyDataConverter.class.getName());
    }

    /**
     * Metodo para mapear los atributos (Properties) enlazados a los 
     * controles del dialogo, hacia el objeto que será enviado a la clase model.
     * 
     * @param viewModel instancia del viewModel.
     * @return instancia del objeto
     */
    public static UserKey convert(KeyDataViewModel viewModel) {
        return new UserKey(viewModel.idProperty().get(), 
                viewModel.applicationProperty().get(), 
                viewModel.descriptionProperty().get(), 
                viewModel.userNameProperty().get(),
                viewModel.passwordProperty().get());
    }
    
    /**
     * Metodo para mapear los atributos de la entidad UserKey al DTO KeyData.
     * 
     * @param userKey instancia del objeto.
     * @return instancia de KeyData.
     */
    public static KeyData convert(UserKey userKey) {
        return new KeyData(userKey.getUserkeyId(), userKey.getApplication(), 
                userKey.getDescription(), userKey.getUserName(), userKey.getPassword());
    }

}
