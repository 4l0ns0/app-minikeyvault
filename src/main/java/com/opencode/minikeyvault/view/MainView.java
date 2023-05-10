package com.opencode.minikeyvault.view;

import com.opencode.minikeyvault.utils.ImageFactory;
import com.opencode.minikeyvault.utils.ImageFactory.FontAwesome;
import com.opencode.minikeyvault.utils.ResourceManager;
import com.opencode.minikeyvault.utils.Utils;
import com.opencode.minikeyvault.view.commons.KeyDataRow;
import com.opencode.minikeyvault.domain.KeyData;
import com.opencode.minikeyvault.viewmodel.MainViewModel;
import com.opencode.minikeyvault.viewmodel.MainViewModel.OperationType;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * class: MainView. <br/>
 * @author Henry Navarro <br/><br/>
 *          <u>Cambios</u>:<br/>
 *          <ul>
 *          <li>2021-08-21 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class MainView implements Initializable {

    private final MainViewModel mainViewModel = MainViewModel.getInstance();

    private Stage parentStage;
    private Tooltip tooltip;
    private PauseTransition tooltipTransition;

    @FXML BorderPane bpnPrincipal;

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

        ImageFactory.setIcon(mnuClose, FontAwesome.FA_SIGN_OUT);
        mnuClose.setOnAction(e -> parentStage.close());

        ImageFactory.setIcon(mnuBackup, FontAwesome.FA_DATABASE);
        mnuBackup.setOnAction(e -> showBackupView());
        
        ImageFactory.setIcon(mnuAbout, FontAwesome.FA_QUESTION_CIRCLE_O);
        mnuAbout.setOnAction(e -> showAboutView());

        txtFilter.textProperty().bindBidirectional(mainViewModel.filterProperty());
        txtFilter.textProperty().addListener((observable, oldValue, newValue) 
                -> mainViewModel.fillObservableList(newValue));

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

        lblTotalRecords.textProperty().bind(Bindings.size(mainViewModel
                .getObservableList()).asString());

        lblMessage.textProperty().bind(mainViewModel.getUserMessage());

        tblColApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        tblColUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblColPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        tblData.setEditable(false);
        tblData.setFocusTraversable(false);
        tblData.setItems(mainViewModel.getObservableList());
        tblData.setRowFactory(tableView -> {
            final TableRow<KeyDataRow> tableRow = new TableRow<>();

            tableRow.hoverProperty().addListener(listener -> {
                KeyDataRow keyDataRow = tableRow.getItem();

                if (keyDataRow != null && tableRow.isHover()) {
                    setCellBehavior(keyDataRow.getUserName(), tableRow.getIndex());
                    setCellBehavior(keyDataRow.getPassword(), tableRow.getIndex());
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
     * @param hbox instancia del componente de la celda. 
     * @param rowIndex fila a la que corresponde el componente.
     */
    private void setCellBehavior(HBox hbox, int rowIndex) {

        PasswordField pwd = (PasswordField) hbox.getChildren().get(0);
        Label img = (Label) hbox.getChildren().get(1);

        hbox.setOnMouseEntered(e -> img.setPrefWidth(15));
        hbox.setOnMouseExited(e -> img.setPrefWidth(0));

        pwd.setOnMousePressed(e -> {
            tblData.requestFocus();
            tblData.getSelectionModel().select(rowIndex);
        });

        pwd.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                if (e.isControlDown()) {
                    btnLock.setSelected(true);
                }

                Utils.copyToClipboard(pwd.getText());
                showTooltipMessage(e.getScreenX(), e.getScreenY());
            }
        });

        img.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                if (e.isControlDown()) {
                    btnLock.setSelected(true);
                }

                Utils.copyToClipboard(pwd.getText());
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

        mainViewModel.setOperationType(operationType, keyData);

        if (operationType == OperationType.INSERT 
                || operationType == OperationType.UPDATE) {
            showView("CrudDlg", operationType == OperationType.INSERT
                  ? "Nuevo Registro" : "Actualizar Registro", true, false, true);
        } else if (operationType == OperationType.DELETE) {
            btnLock.setSelected(false);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Registro");
            alert.setHeaderText(null);
            alert.setContentText("Está seguro que desea eliminar el registro '" 
                    + keyData.getApplication() + "'?");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(ImageFactory.IMG_APP_ICON);

            centerParentStage(stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                mainViewModel.delete();
            }
        }

    }

    /**
     * Muestra el diálogo para generar / importar Backups.
     */
    private void showBackupView() {
        showView("Backup", "Generar / Restaurar Backup", true, false, true);
    }

    /**
     * Muestra el dialogo 'Acerca de...'
     */
    private void showAboutView() {
        showView("About", "Acerca de Mini Key Vault", true, false, true);
    }

    /**
     * Metodo para cargar la vista solicitada.
     * 
     * @param fxmlViewName nombre de la vista (FXML) sin extensión.
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

        btnLock.setSelected(false);

        try {
            FXMLLoader loader = new FXMLLoader(ResourceManager
                    .getFxView(fxmlViewName));

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(ResourceManager.getCssStyle("styles.css").toString());

            Stage stage = new Stage();
            stage.setTitle(title);
            
            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            stage.getIcons().add(ImageFactory.IMG_APP_ICON);
            stage.setResizable(resizable);
            stage.setScene(scene);

            centerParentStage(stage);

            if (showAndWait) {
                stage.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Muestra el tooltip con el mensaje de 'Texto copiado'
     * e inicia la transición para ocultar el diálogo.
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
     * y detiene transición para ocultar el diálogo.
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

        parentStage.show();
        parentStage.hide();

    }

    /**
     * Metodo que centrara el stage con relación al padre.
     *
     * @param stage stage que se desea centrar.
     */
    public void centerParentStage(Stage stage) {

        // Se calcula la posición central de Stage padre
        double x = parentStage.getX() + parentStage.getWidth() / 2d;
        double y = parentStage.getY() + parentStage.getHeight() / 2d;

        // Se oculta el stage durante la reubicación.
        stage.setOnShowing(ev -> stage.hide());

        // Se reubica y muestra el stage
        stage.setOnShown(ev -> {
            stage.setX(x - stage.getWidth() / 2d);
            stage.setY(y - stage.getHeight() / 2d);
            stage.show();
        });

    }
    
}
