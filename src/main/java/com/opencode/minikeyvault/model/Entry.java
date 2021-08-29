package com.opencode.minikeyvault.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class: Entry. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

    private int id;
    private String application;
    private String description;
    private String userName;
    private String password;

}
