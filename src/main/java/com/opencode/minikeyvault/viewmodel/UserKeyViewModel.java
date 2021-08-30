package com.opencode.minikeyvault.viewmodel;

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

        observableList.setAll(lstData.stream().filter(e -> {
            if (filter != null) {
                return e.getApplication().toLowerCase().contains(filter.toLowerCase());
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
                .getAll().stream().map(e -> new TableData(e.getId(), e.getApplication(),
                        e.getDescription(), e.getUserName(), e.getPassword()))
                .collect(Collectors.toList());

    }

    public void save(boolean isNew) {

        if (isNew) {
            userKeyModel.insert(UserKeyConverter.convert(this));
        } else {
            userKeyModel.update(UserKeyConverter.convert(this));
        }

        reset();
        fillDataList();
        fillObservableList(filterProperty().get());
    }

    public boolean delete(int userKeyId) {
        return userKeyModel.delete(userKeyId);
    }

    private void reset() {
        application.set("");
        description.set("");
        userName.set("");
        password.set("");
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
