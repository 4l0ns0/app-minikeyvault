package com.opencode.minikeyvault.utils;

import com.opencode.minikeyvault.model.Entry;
import com.opencode.minikeyvault.model.TableData;
import com.opencode.minikeyvault.repository.Db;
import com.opencode.minikeyvault.repository.dao.DaoEntry;
import com.opencode.minikeyvault.repository.dao.impl.DaoEntryImpl;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * class: Keystore. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class Keystore {

    private Keystore() {
        throw new IllegalStateException(Keystore.class.getName());
    }

    /**
     * Metodo que verifica que todos los archivos de configuración que la aplicación
     * requiere para su funcionamientos se encuentren definidos. Caso contrario, 
     * los inicializa.
     */
    public static void initKeystore() {

        File file = new File(Constants.KEYSTORE_FILENAME);
        
        if (file.exists()) {
            if (!file.canRead() || !file.canWrite()) {
                log.error("No se puede leer o escribir en el {}", 
                        Constants.KEYSTORE_FILENAME);
            }
        } else {
            try {
                if (file.createNewFile()) {
                    log.info("Se creó el {}", Constants.KEYSTORE_FILENAME);
                } else {
                    log.error("No se pudo crear el {}", Constants.KEYSTORE_FILENAME);
                }
            } catch (IOException e) {
                log.error("Ocurrio un error al inicializar el {}: {}",
                        e.getMessage(), Constants.KEYSTORE_FILENAME);
            }
        }

    }
    
    /**
     * Captura el contenido del keystore (json) y lo devuelve como una colección de objetos.
     * 
     * @return lista con los registros del keystore.
     */
//    public static List<Entry> retrieveKeys() {

//        List<Entry> lstKeyValue = null;
//
//        ObjectMapper mapper = new ObjectMapper()
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        try {
//            String content = Utils.getFileContent(Constants.KEYSTORE_FILENAME);
//
//            if (StringUtils.isBlank(content)) {
//                return new ArrayList<>();
//            }
//
////            lstKeyValue = Arrays.asList(mapper.readValue(content, Entry[].class));
//            lstKeyValue = Utils.mapFileToObjectList(content, null).readValue(content, Entry[].class));
//        } catch (Exception e) {
//            log.error("Ocurrieron errores durante el proceso: " + e.getMessage());
//        }
//
//        return lstKeyValue;
        
//        return Utils.mapFileToObjectList(Constants.KEYSTORE_FILENAME, Entry.class);
//    }
    
    
    private static DaoEntry daoEntryImpl;
    
    static {
        daoEntryImpl = new DaoEntryImpl();
    }
    
    /**
     * Metodo para convertir los valores del keystore en el modelo de datos
     * requerido para el tableview.
     * 
     * @return colección con los valores del keystore.
     */
    public static List<TableData> retrieveData() {
        return daoEntryImpl.getAll()
            .stream()
            .map(e -> new TableData(e.getId(), e.getApplication(), 
                    e.getDescription(), e.getUserName(), e.getPassword()))
            .collect(Collectors.toList());
    }

}
