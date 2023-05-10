package com.opencode.minikeyvault.model.dao;

import com.opencode.minikeyvault.entity.UserKey;
import java.util.List;

/** Clase: MngDb. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2023-04-8 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface MngDb {

    /**
     * Método que valida si la base de datos existe.
     *
     * @return true si existe, caso contrario false.
     */
    boolean exists();

    /**
     * Método que inicializa la base de datos.
     *
     * @param username usuario para la base de datos.
     * @param password clave para la base de datos.
     * @return detalle del error (en caso de error), caso contrario null.
     */
    String initDatabase(String username, String password);

    /**
     * Método que apertura la base de datos.
     *
     * @param username usuario para la base de datos.
     * @param password clave para la base de datos.
     * @return detalle del error (en caso de error), caso contrario null.
     */
    String openDatabase(String username, String password);

    /**
     * Recupera una lista (filtrada) de UserKey.
     *
     * @param filter nombre de los registros buscar.
     * @return colección con la data recuperada.
     */
    List<UserKey> retriveUserKeyList(String filter);

    /**
     * Recupera una instancia de UserKey.
     *
     * @param userKeyId id del registro a recuperar.
     */
    UserKey retrieveUserKey(int userKeyId);

    /**
     * Crea un nuevo UserKey.
     *
     * @param userKey data a registrar.
     */
    UserKey createUserKey(UserKey userKey);

    /**
     * Modifica un UserKey.
     *
     * @param userKey data a modificar.
     */
    UserKey modifyUserKey(UserKey userKey);

    /**
     * Elimina un UserKey.
     *
     * @param userKeyId id del registro a eliminar.
     */
    boolean removeUserKey(int userKeyId);

}
