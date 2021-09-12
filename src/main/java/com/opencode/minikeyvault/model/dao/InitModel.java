package com.opencode.minikeyvault.model.dao;

/** class: InitModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-11 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public interface InitModel {

    boolean initConfigFile();

    boolean initDatabase();

}
