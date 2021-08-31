package com.opencode.minikeyvault.model;

import com.opencode.minikeyvault.domain.UserKey;
import java.util.List;

/** interface: IUserKeyModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface IUserKeyModel {

    List<UserKey> getAll();

    UserKey getOne(int userKeyId);

    UserKey insert(UserKey userKey);

    UserKey update(UserKey userKey);

    boolean delete(int userKeyId);

}
