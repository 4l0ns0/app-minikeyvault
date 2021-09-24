package com.opencode.minikeyvault.view.dto;

import com.opencode.minikeyvault.utils.Constants.ResultType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** class: InitilizationInfo. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-09-12 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitDetail {

    private ResultType type;
    private String message;
    private Map<String, String> detail;

}
