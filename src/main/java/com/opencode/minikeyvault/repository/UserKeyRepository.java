package com.opencode.minikeyvault.repository;

import com.opencode.minikeyvault.entity.UserKey;
import java.util.List;

/** class: UserKeyRepository. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2023-04-8 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface UserKeyRepository {

    String COL_USERKEY_ID = "userkey_id";
    String COL_APPLICATION = "application";
    String COL_DESCRIPTION = "description";
    String COL_USERNAME = "username";
    String COL_PASSWORD = "password";

    /**
     * Devuelve el registro cuyo id se indica como parametro.
     */
    UserKey get(int userKeyId);

    /**
     * Devuelve todos los registros de la tabla desde la base de datos.
     */
    List<UserKey> list(String filter);

    /**
     * Inserta el registro en la base de datos.
     */
    UserKey insert(UserKey userKey);

    /**
     * Actualiza el registro en la base de datos.
     */
    UserKey update(UserKey userKey);

    /**
     * Elimina el registro en la base de datos.
     */
    boolean delete(int userKeyId);

}
