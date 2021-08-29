package com.opencode.minikeyvault.service;

import com.opencode.minikeyvault.model.Entry;
import com.opencode.minikeyvault.model.TableData;
import java.util.List;

/** interface: Service. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface Service {

    List<TableData> retrieveData();

    Entry get(int entryId);

    Entry insert(Entry entry);

    Entry update(Entry entry);

    boolean delete(int entryId);

}
