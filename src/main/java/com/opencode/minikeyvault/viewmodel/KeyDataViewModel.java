package com.opencode.minikeyvault.viewmodel;

import com.opencode.minikeyvault.model.dao.UserKeyModel;
import com.opencode.minikeyvault.model.dao.impl.UserKeyModelImpl;
import com.opencode.minikeyvault.view.commons.KeyDataRow;
import com.opencode.minikeyvault.view.dto.KeyData;
import com.opencode.minikeyvault.viewmodel.converter.KeyDataConverter;
import java.util.stream.Collectors;
import javafx.animation.PauseTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import lombok.Synchronized;

/** 
 * class: KeyDataViewModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class KeyDataViewModel {

    private static KeyDataViewModel instance;

    /**
     * Metodo para obtener la instancia de clase.
     * 
     * @return instancia de la clase.
     */
    @Synchronized
    public static KeyDataViewModel getInstance() {

        if (instance == null) {
            instance = new KeyDataViewModel();
        }

        return instance;
    }

    /**
     * class: OperationType. <br/>
     * @author Henry Navarro <br/><br/>
     *          <u>Cambios</u>:<br/>
     *          <ul>
     *          <li>2021-08-30 Creación del proyecto.</li>
     *          </ul>
     * @version 1.0
     */
    public enum OperationType {
        INSERT, UPDATE, DELETE
    }

    private UserKeyModel userKeyModel = new UserKeyModelImpl();

    private ObservableList<KeyDataRow> observableList = FXCollections.observableArrayList();

    private IntegerProperty keyId = new SimpleIntegerProperty(0);
    private StringProperty application = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty userName = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");

    private StringProperty filter = new SimpleStringProperty("");
    private StringProperty userMessage = new SimpleStringProperty("");

    private OperationType operationType;
    private PauseTransition messageTransition;

    /**
     * Constructor.
     */
    private KeyDataViewModel() {
        
        messageTransition = new PauseTransition(Duration.seconds(8));
        messageTransition.setOnFinished(event -> userMessage.set(""));

        fillObservableList(null);
    }
    
    /**
     * Metodo para filtrar la información del observable.
     * 
     * @param filter si null, no se aplica ningún filtro. Caso contrario, se aplica 
     *     el filtro indicado.
     */
    public void fillObservableList(String filter) {

        observableList.setAll(userKeyModel
              .getAll(filter).stream()
              .map(e -> new KeyDataRow(KeyDataConverter.convert(e)))
              .collect(Collectors.toList()));

    }

    /**
     * Define el tipo de operación a realizar.
     * 
     * @param operationType tipo de operacion (OperationType.INSERT, 
     *     OperationType.UPDATE, OperationType.DELETE)
     * @param keyData instancia del KeyData en caso el tipo de operación se OperationType.UPDATE 
     *     o OperationType.DELETE. Null en caso la operación sea OperationType.INSERT.
     */
    public void setOperationType(OperationType operationType, KeyData keyData) {

        clean();

        this.operationType = operationType;

        if ((operationType == OperationType.UPDATE || operationType == OperationType.DELETE) 
                && keyData != null) {
            this.keyId.set(keyData.getKeyId());
            this.application.set(keyData.getApplication());
            this.description.set(keyData.getDescription());
            this.userName.set(keyData.getUserName());
            this.password.set(keyData.getPassword());
        }

    }

    /**
     * Metodo para guardar los cambios realizados en las operaciones
     * de creación y modificación de registros.
     */
    public void insUpd() {

        String message = "Ocurrió un error. Revise el log para ver el detalle.";

        switch (operationType) {
            case INSERT:
                if (userKeyModel.insert(KeyDataConverter.convert(this)) != null) {
                    message = "El registro de creó correctamente.";
                }

                break;
            case UPDATE:
                if (userKeyModel.update(KeyDataConverter.convert(this)) != null) {
                    message = "El registro de modificó correctamente.";
                }

                break;
            default:
                break;
        }

        updateReference();
        setUserMessage(message);

    }

    /**
     * Metodo para eliminar un registro.
     */
    public void delete() {

        String message = "Ocurrió un error. Revise el log para ver el detalle.";

        if (userKeyModel.delete(KeyDataConverter.convert(this).getUserkeyId())) {
            message = "El registro se eliminó correctamente";
        }

        updateReference();
        setUserMessage(message);

    }

    /**
     * Metodo que define el mensaje del resultado de la operación y luego del 
     * intervalo de tiempo establecido lo desaparece.
     * 
     * @param message mensaje a mostrar.
     */
    private void setUserMessage(String message) {

        userMessage.set(message);
        messageTransition.play();

    }
    
    /**
     * Metodo para limpiar las referencias de los properties.
     */
    public void clean() {

        keyId.set(0);
        application.set("");
        description.set("");
        userName.set("");
        password.set("");

    }

    /**
     * Actualiza los objetos referenciados desde la capa de la vista.
     */
    private void updateReference() {

        fillObservableList(filterProperty().get());
        clean();

    }
    
    public ObservableList<KeyDataRow> getObservableList() {
        return observableList;
    }

    public StringProperty filterProperty() {
        return filter;
    }

    public IntegerProperty idProperty() {
        return keyId;
    }

    public StringProperty applicationProperty() {
        return application;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty getUserMessage() {
        return userMessage;
    }
    
}
