package com.opencode.minikeyvault.viewmodel.converter;

import com.opencode.minikeyvault.domain.UserKey;
import com.opencode.minikeyvault.viewmodel.UserKeyViewModel;

/** class: UserKeyConverter. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-30 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class UserKeyConverter {

    private UserKeyConverter() {
        throw new IllegalStateException(UserKeyConverter.class.getName());
    }

    /**
     * Metodo para mapear los atributos (Properties) enlazados a los 
     * controles del dialogo, hacia el objeto que será enviado a la clase model.
     * 
     * @param viewModel instancia del viewModel.
     * @return instancia del objeto
     */
    public static UserKey convert(UserKeyViewModel viewModel) {
        return new UserKey(viewModel.idProperty().get(), 
                viewModel.applicationProperty().get(), 
                viewModel.descriptionProperty().get(), 
                viewModel.userNameProperty().get(),
                viewModel.passwordProperty().get());
    }

}
