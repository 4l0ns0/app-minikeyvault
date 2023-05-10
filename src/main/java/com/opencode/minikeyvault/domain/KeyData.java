package com.opencode.minikeyvault.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** class: KeyData. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-05 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyData {

    private int keyId;
    private String application;
    private String description;
    private String userName;
    private String password;

}
