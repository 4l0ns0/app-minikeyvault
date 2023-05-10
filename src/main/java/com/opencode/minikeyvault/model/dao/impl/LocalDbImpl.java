package com.opencode.minikeyvault.model.dao.impl;

import com.opencode.minikeyvault.model.dao.MngDb;
import com.opencode.minikeyvault.repository.UserKeyRepository;
import com.opencode.minikeyvault.entity.UserKey;
import com.opencode.minikeyvault.repository.db.Datasource;
import com.opencode.minikeyvault.repository.impl.UserKeyRepositoryImpl;
import com.opencode.minikeyvault.utils.Constants;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/** 
 * class: UserKeyModelImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class LocalDbImpl implements MngDb {

    private UserKeyRepository userKeyRepository = new UserKeyRepositoryImpl();

    @Override
    public boolean exists() {
        return new File(Constants.DB_FILENAME).exists();
    }

    @Override
    public String initDatabase(String username, String password) {
        return Datasource.init(username, password);
    }

    @Override
    public String openDatabase(String username, String password) {
        return Datasource.open(username, password);
    }

    @Override
    public List<UserKey> retriveUserKeyList(String filter) {
        return userKeyRepository.list(filter);
    }

    @Override
    public UserKey retrieveUserKey(int userKeyId) {
        return userKeyRepository.get(userKeyId);
    }

    @Override
    public UserKey createUserKey(UserKey userKey) {
        return userKeyRepository.insert(userKey);
    }

    @Override
    public UserKey modifyUserKey(UserKey userKey) {
        return userKeyRepository.update(userKey);
    }

    @Override
    public boolean removeUserKey(int userKeyId) {
        return userKeyRepository.delete(userKeyId);
    }

}
