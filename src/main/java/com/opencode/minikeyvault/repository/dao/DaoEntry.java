package com.opencode.minikeyvault.repository.dao;

import com.opencode.minikeyvault.model.Entry;
import java.util.List;

/** interface: DaoData. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface DaoEntry {

    List<Entry> getAll();

    Entry getOne(int recordId);

    Entry insert(Entry entry);

    Entry update(Entry entry);

    boolean delete(int recordId);

}
