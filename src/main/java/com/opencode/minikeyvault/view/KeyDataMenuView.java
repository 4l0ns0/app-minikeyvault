package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.utils.Utils;
import com.opencode.minikeyvault.view.commons.KeyDataRow;
import com.opencode.minikeyvault.view.dto.KeyData;
import com.opencode.minikeyvault.viewmodel.InitViewModel;
import com.opencode.minikeyvault.viewmodel.KeyDataViewModel;
import com.opencode.minikeyvault.viewmodel.KeyDataViewModel.OperationType;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * class: KeyDataMenuView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class KeyDataMenuView implements Initializable {

    private final InitViewModel initViewModel = InitViewModel.getInstance();
    private final KeyDataViewModel keyDataViewModel = KeyDataViewModel.getInstance();

    private Stage parentStage;
    private Tooltip tooltip;
    private PauseTransition tooltipTransition;

    @FXML BorderPane bpnPrincipal;

    @FXML CheckMenuItem chkAlwaysOnTop;
    @FXML CheckMenuItem chkFixItWhenCopyUser;
    @FXML CheckMenuItem chkFixItWhenCopyPassword;

    @FXML MenuItem mnuClose;
    @FXML MenuItem mnuBackup;
    @FXML MenuItem mnuHelp;
    @FXML MenuItem mnuAbout;

    @FXML TextField txtFilter;

    @FXML Button btnInsert;
    @FXML Button btnUpdate;
    @FXML Button btnDelete;
    @FXML ToggleButton btnLock;
    @FXML Label lblTotalRecords;
    @FXML Label lblMessage;

    @FXML TableView<KeyDataRow> tblData;
    @FXML TableColumn<KeyDataRow, ?> tblColApplication;
    @FXML TableColumn<KeyDataRow, ?> tblColUserName;
    @FXML TableColumn<KeyDataRow, ?> tblColPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        tooltip = new Tooltip("Texto copiado");

        tooltipTransition = new PauseTransition(Duration.seconds(3));
        tooltipTransition.setOnFinished(event -> tooltip.hide());

        mnuClose.setOnAction(e -> parentStage.close());
        mnuBackup.setOnAction(e -> showBackupView());
        mnuAbout.setOnAction(e -> showAboutView());

        txtFilter.textProperty().bindBidirectional(keyDataViewModel.filterProperty());
        txtFilter.textProperty().addListener((observable, oldValue, newValue) 
                -> keyDataViewModel.fillObservableList(newValue));

        ImageFactory.setIcon(btnInsert, FontAwesome.FA_FILE_O);
        btnInsert.setOnAction(e -> showIudView(OperationType.INSERT));

        ImageFactory.setIcon(btnUpdate, FontAwesome.FA_PENSIL);
        btnUpdate.setOnAction(e -> showIudView(OperationType.UPDATE));
        btnUpdate.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tblData.getSelectionModel().getSelectedItem() == null, 
                tblData.getSelectionModel().selectedItemProperty()));

        ImageFactory.setIcon(btnDelete, FontAwesome.FA_TRASH_O);
        btnDelete.setOnAction(e -> showIudView(OperationType.DELETE));
        btnDelete.disableProperty().bind(Bindings.createBooleanBinding(
                () -> tblData.getSelectionModel().getSelectedItem() == null, 
                tblData.getSelectionModel().selectedItemProperty()));

        ImageFactory.setIcon(btnLock, FontAwesome.FA_TOGGLE_OFF);
        btnLock.setTooltip(new Tooltip("Se activa al copiar valores \n"
                + "de la tabla (Ctrl + Dbl. Click)"));
        btnLock.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    parentStage.setAlwaysOnTop(newValue);
                    ImageFactory.setIcon(btnLock, newValue.booleanValue() 
                            ? FontAwesome.FA_TOGGLE_ON 
                            : FontAwesome.FA_TOGGLE_OFF);
                });

        lblTotalRecords.textProperty().bind(Bindings.size((keyDataViewModel
                .getObservableList())).asString());

        lblMessage.textProperty().bind(keyDataViewModel.getUserMessage());

        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        tblData.setEditable(false);
        tblData.setFocusTraversable(false);
        tblData.setItems(keyDataViewModel.getObservableList());
        tblData.setRowFactory(tableView -> {

            final TableRow<KeyDataRow> tableRow = new TableRow<>();

            tableRow.hoverProperty().addListener(listener -> {

                final KeyDataRow keyDataRow = tableRow.getItem();

                if (tableRow.isHover() && keyDataRow != null) {
                    setCellBehavior(
                            (PasswordField) keyDataRow.getUserName()
                                .getChildren().get(0),
                            (Label) keyDataRow.getUserName()
                                .getChildren().get(1),
                            tableRow.getIndex());

                    setCellBehavior(
                            (PasswordField) keyDataRow.getPassword()
                                .getChildren().get(0),
                            (Label) keyDataRow.getPassword()
                                .getChildren().get(1),
                            tableRow.getIndex());

                }
            });

            return tableRow;
        });

        Platform.runLater(() -> txtFilter.requestFocus());

    }

    /**
     * Metodo que define el comportamiento de los botones alojados en las celdas de 
     * las columnas usuario y password.
     * 
     * @param passwordField objeto que contiene la información de la celda. 
     * @param image imagen que muestra el icono de copiar.
     * @param rowIndex fila a la que corresponden los valores.
     */
    private void setCellBehavior(PasswordField passwordField, Label image, int rowIndex) {

        passwordField.setOnMouseEntered(e -> image.setVisible(true));
        passwordField.setOnMouseExited(e -> image.setVisible(false));
        passwordField.setOnMousePressed(e -> {
            tblData.requestFocus();
            tblData.getSelectionModel().select(rowIndex);
        });
        passwordField.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) 
                    && e.getClickCount() == 2) {
                if (e.isControlDown()) {
                    btnLock.setSelected(true);
                }

                Utils.copyToClipboard(passwordField.getText());
                showTooltipMessage(e.getScreenX(), e.getScreenY());
            }
        });

    }

    /**
     * Muestra la vista IUD (Insert, Update o Delete) que corresponda según 
     * el tipo de operación indicada.
     * 
     * @param operationType tipo de operación a realizar.
     */
    public void showIudView(OperationType operationType) {

        KeyData keyData = null;

        if (operationType == OperationType.UPDATE
                || operationType == OperationType.DELETE) {
            keyData = tblData.getSelectionModel().getSelectedItem().getKeyData();
        }

        btnLock.setSelected(false);
        keyDataViewModel.setOperationType(operationType, keyData);

        if (operationType == OperationType.INSERT 
                || operationType == OperationType.UPDATE) {
            showView("KeyDataInsUpdView", operationType == OperationType.INSERT 
                  ? "Nuevo Registro" : "Actualizar Registro", true, false, true);
        } else if (operationType == OperationType.DELETE) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Registro");
            alert.setHeaderText(null);
            alert.setContentText("Está seguro que desea eliminar el registro '" 
                    + keyData.getApplication() + "'?");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(ImageFactory.IMG_APP_ICON);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                keyDataViewModel.delete();
            }
        }

    }

    /**
     * Muestra el dialogo para generar / importar Backups.
     */
    private void showBackupView() {
        showView("BackupView", "Generar / Restaurar Backup", true, false, true);
    }

    /**
     * Muestra el dialogo 'Acerca de...'
     */
    private void showAboutView() {
        showView("AboutView", "Acerca de Mini Key Vault", true, false, true);
    }

    /**
     * Metodo para cargar la vista solicitada.
     * 
     * @param fxmlViewName nombre de la vista (FXML) sin extensió.
     * @param title titulo para la ventana.
     * @param modal true si se quiere que la ventana sea de tipo modal, 
     *     caso contario false.
     * @param resizable true si se desea que la ventan se pueda redimensionar,
     *     caso contrario false.
     * @param showAndWait true si se desea que al abrir la nueva ventana,
     *     esta se tenga que cerrar para poder interactuar con la ventana principal.
     */
    private void showView(String fxmlViewName, String title, boolean modal, 
            boolean resizable, boolean showAndWait) {

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager
                    .getFxDialog(fxmlViewName));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles/styles.css");

            Stage stage = new Stage();
            stage.setTitle(title);
            
            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            stage.getIcons().add(ImageFactory.IMG_APP_ICON);
            stage.setScene(scene);
            stage.setResizable(resizable);
            
            if (showAndWait) {
                stage.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Muestra el tooltip con el mensaje de 'Texto copiado'
     * e inicia la transición para ocultar el dialogo.
     * 
     * @param x posición horizontal.
     * @param y posición vertical.
     */
    private void showTooltipMessage(double x, double y) {

        hideTooltipMessage();

        tooltip.show(parentStage, x + 10.0, y + 5.0);
        tooltipTransition.play();

    }

    /**
     * Oculta el tooltip con el mensaje de 'Texto copiado'
     * y detiene transición para ocultar el dialogo. 
     */
    private void hideTooltipMessage() {
        tooltip.hide();
        tooltipTransition.stop();
    }
    
    /**
     * Metodo para recibir el Stage desde la clase que invoca la vista.
     * 
     * @param stage stage al que pertenece la vista.
     */
    public void setStage(Stage stage) {

        parentStage = stage;
        parentStage.focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.booleanValue()) {
                        btnLock.setSelected(false);
                    } else {
                        hideTooltipMessage();
                    }
                });

    }

}
