package com.opencode.minikeyvault.service.impl;

import com.opencode.minikeyvault.model.Entry;
import com.opencode.minikeyvault.model.TableData;
import com.opencode.minikeyvault.repository.dao.DaoEntry;
import com.opencode.minikeyvault.repository.dao.impl.DaoEntryImpl;
import com.opencode.minikeyvault.service.Service;
import java.util.List;
import java.util.stream.Collectors;

/** class: ServiceImpl. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class ServiceImpl implements Service {

    private DaoEntry daoEntry = new DaoEntryImpl();

    @Override
    public List<TableData> retrieveData() {
        return daoEntry.getAll()
                .stream()
                .map(e -> new TableData(e.getId(), e.getApplication(), 
                        e.getDescription(), e.getUserName(), e.getPassword()))
                .collect(Collectors.toList());
    }

    @Override
    public Entry get(int entryId) {
        return null;
    }

    @Override
    public Entry insert(Entry entry) {
        return daoEntry.insert(entry);
    }

    @Override
    public Entry update(Entry entry) {
        return daoEntry.update(entry);
    }

    @Override
    public boolean delete(int entryId) {
        return daoEntry.delete(entryId);
    }

}
