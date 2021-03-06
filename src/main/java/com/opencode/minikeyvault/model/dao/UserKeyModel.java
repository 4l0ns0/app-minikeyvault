package com.opencode.minikeyvault.model.dao;

import com.opencode.minikeyvault.model.entity.UserKey;
import java.util.List;

/** interface: UserKeyModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface UserKeyModel {

    List<UserKey> getAll(String filter);

    UserKey getOne(int userKeyId);

    UserKey insert(UserKey userKey);

    UserKey update(UserKey userKey);

    boolean delete(int userKeyId);

}
