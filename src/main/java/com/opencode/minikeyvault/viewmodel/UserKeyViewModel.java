package com.opencode.minikeyvault.viewmodel;

import com.opencode.minikeyvault.domain.UserKey;
import com.opencode.minikeyvault.model.IUserKeyModel;
import com.opencode.minikeyvault.model.impl.UserKeyModel;
import com.opencode.minikeyvault.view.commons.TableData;
import com.opencode.minikeyvault.viewmodel.converter.UserKeyConverter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Synchronized;

/** class: UserKeyViewModel. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-27 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class UserKeyViewModel {

    private static UserKeyViewModel instance;

    /**
     * Metodo para obtener la instancia de clase.
     * 
     * @return instancia de la clase.
     */
    @Synchronized
    public static UserKeyViewModel getInstance() {

        if (instance == null) {
            instance = new UserKeyViewModel();
        }

        return instance;
    }

    private IUserKeyModel userKeyModel = new UserKeyModel();

    private List<TableData> lstData = new ArrayList<>();
    private ObservableList<TableData> observableList = FXCollections.observableArrayList();
    private StringProperty filter = new SimpleStringProperty("");

    private IntegerProperty id = new SimpleIntegerProperty(0);
    private StringProperty application = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty userName = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");

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

    private OperationType operationType;

    private UserKeyViewModel() {
        fillDataList();
        fillObservableList(null);
    }

    /**
     * Metodo para filtrar la información del observable.
     * 
     * @param filter si null, no se aplica ningún filtro. Caso contrario, se aplica 
     *     el filtro indicado.
     */
    public void fillObservableList(String filter) {

        observableList.setAll(lstData.stream()
                .filter(e -> {
                    if (filter != null) {
                        return e.getApplication().toLowerCase()
                                .contains(filter.toLowerCase());
                    }

                    return true;
                })
                .sorted(Comparator.comparing(TableData::getApplication))
                .collect(Collectors.toList()));

    }

    /**
     * Se capturan todos los registros desde la base de datos.
     */
    private void fillDataList() {

        lstData = userKeyModel
                .getAll().stream().map(TableData::new)
                .collect(Collectors.toList());

    }

    /**
     * Define el tipo de operación a realizar.
     * 
     * @param operationType tipo de operacion (OperationType.INSERT, 
     *     OperationType.UPDATE, OperationType.DELETE)
     * @param userKey instancia del UserKey en caso el tipo de operación se OperationType.UPDATE 
     *     o OperationType.DELETE. Null en caso la operación sea OperationType.INSERT.
     */
    public void setOperationType(OperationType operationType, UserKey userKey) {

        this.operationType = operationType;

        if (operationType != OperationType.INSERT && userKey != null) {
            this.id.set(userKey.getId());
            this.application.set(userKey.getApplication());
            this.description.set(userKey.getDescription());
            this.userName.set(userKey.getUserName());
            this.password.set(userKey.getPassword());
        }

    }

    /**
     * Metodo para guardar los cambios realizados en las operaciones
     * de creación y modificación de registros.
     */
    public void insUpd() {

        switch (operationType) {
            case INSERT:
                userKeyModel.insert(UserKeyConverter.convert(this));
                break;
            case UPDATE:
                userKeyModel.update(UserKeyConverter.convert(this));
                break;
            default:
                break;
        }

        updateReference();

    }

    /**
     * Metodo para eliminar un registro.
     */
    public void delete() {

        userKeyModel.delete(UserKeyConverter.convert(this).getId());
        updateReference();

    }
    
    /**
     * Metodo para limpiar las referencias de los properties.
     */
    public void clean() {

        id.set(0);
        application.set("");
        description.set("");
        userName.set("");
        password.set("");

    }

    /**
     * Actualiza los objetos referenciados desde la capa de la vista.
     */
    private void updateReference() {

        fillDataList();
        fillObservableList(filterProperty().get());
        clean();

    }
    
    public ObservableList<TableData> getObservableList() {
        return observableList;
    }

    public StringProperty filterProperty() {
        return filter;
    }

    public IntegerProperty idProperty() {
        return id;
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

}
