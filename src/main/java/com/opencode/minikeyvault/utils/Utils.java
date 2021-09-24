package com.opencode.minikeyvault.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * class: Utils. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
@Slf4j
public class Utils {

    private static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    private Utils() {
        throw new IllegalStateException(Utils.class.getName());
    }

    private static Clipboard clipboard = Clipboard.getSystemClipboard();
    private static ClipboardContent content = new ClipboardContent();
    
    /**
     * Metodo que coloca en el porta papeles la cadena de texto recibida.
     * 
     * @param value cadena de texto que se desea colocar en el porta papeles.
     */
    public static void copyToClipboard(String value) {
        content.putString(value);
        clipboard.setContent(content);
    }
    
    /**
     * Metodo para generar un objeto File del archivo indicado.
     * 
     * @param fileName nombre del archivo del cual se quiere generar el objeto.
     * @return objeto File que hace referencia al archivo indicado.
     * @throws InvalidParameterException error en caso no se indique el nombre del archivo.
     * @throws FileNotFoundException errror en caso el archivo indicado no exista.
     */
    public static File getFile(String fileName)
            throws InvalidParameterException, FileNotFoundException {
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new InvalidParameterException("Se debe indicar el nombre del archivo.");
        }

        File file = new File(fileName);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        return file;
    }

    /**
     * Metodo para capturar el contenido de un archivo.
     * 
     * @param fileName nombre del archivo del cual se quiere capturar el contenido.
     * @return cadena de texto con el contenido del archivo.
     * @throws InvalidParameterException error en caso no se indique el nombre del archivo.
     * @throws IOException error en caso ocurriera un problema al leer el archivo.
     */
    public static String getFileContent(String fileName) 
            throws InvalidParameterException, IOException {

        if (StringUtils.isBlank(fileName)) {
            throw new InvalidParameterException("Se debe indicar el nombre del archivo.");
        }

        String content = "";

        try {
            content = new String(Files.readAllBytes(new File(fileName).toPath()),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException(String.format("Ocurrio un error al leer "
                    + "el archivo '%s'", fileName));
        }

        return content;
    }

    /**
     * Mapea los datos del archivo indicado al objeto especificado.
     * 
     * @param <T> corresponde al tipo de objeto que sera devuelto.
     * @param fileName nombre del archivo que contiene los datos a ser mapeados al objeto.
     * @param clazz clase a partir de la cual se creará el objeto a ser devuelto.
     * @return objeto instanciado con los datos del archivo. Null si hubo un error.
     */
    public static <T> T mapFileToObject(String fileName, Class<T> clazz) {

        try {
            return mapper.readValue(Utils.getFile(fileName), clazz);
        } catch (JsonParseException e) {
            log.error("Formato JSON incorrecto: {}", e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Ocurrieron errores al hacer el mapeo de los campos: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Ocurrieron errores al intentar leer el archivo: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Mapea los datos del archivo indicado a una coleccion (List) con el objeto 
     * especificado.
     * 
     * @param <T> corresponde al tipo de objeto que sera devuelto.
     * @param fileName nombre del archivo que contiene los datos a ser mapeados al objeto.
     * @param clazz clase a partir de la cual se creará el objeto a ser devuelto.
     * @return coleccion con el objeto instanciado con los datos del archivo.
     */
    public static <T> List<T> mapFileToObjectList(String fileName, Class<T> clazz) {

        try {
            return mapper.readValue(Utils.getFile(fileName), 
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonParseException e) {
            log.error("Formato JSON incorrecto (Se requiere un arreglo): {}", e.getMessage());
        } catch (JsonMappingException e) {
            log.error("Ocurrieron errores al hacer el mapeo de los campos: {}", e.getMessage());
        } catch (IOException e) {
            log.error("Ocurrieron errores al intentar leer el archivo: {}", e.getMessage());
        }

        return new ArrayList<>();
    }
    
}
