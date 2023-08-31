package components.main;

import components.details.DetailsController;
import def.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import simulation.Manager;
import utils.Helpers;

import javax.naming.Binding;

public class MainController {
    @FXML Label filePathLabel;
    @FXML Button queueManagementBtn;
    @FXML Button loadFileBtn;
    @FXML Pane placeholder;

    private Helpers helpers;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileLoaded;
    private Stage primaryStage;
    private DetailsController detailsController;

    private Manager manager;

    @FXML
    private void showDetails() {
        helpers.loadComponent(Helpers.DETAILS_PATH, placeholder);
    }

    @FXML
    private void showExecution() {
        helpers.loadComponent(Helpers.EXECUTION_PATH, placeholder);
    }

    @FXML
    private void showResults() {
        helpers.loadComponent(Helpers.RESULTS_PATH, placeholder);
    }

    public MainController() {
        manager = new Manager();
        selectedFileProperty = new SimpleStringProperty();
        isFileLoaded = new SimpleBooleanProperty();
        helpers = new Helpers();
    }

    public void setDetailsController(DetailsController detailsController) {
        this.detailsController = detailsController;
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(Bindings.when(isFileLoaded).then(selectedFileProperty).otherwise("File is not loaded"));
//        showDetails();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        try {
            data.source.File.fetchDataFromFile(absolutePath, manager);
            helpers.loadComponent(Helpers.DETAILS_PATH, placeholder);
            detailsController.setWorldDto(manager.getSharedWorld());
            isFileLoaded.set(true);
            selectedFileProperty.set(absolutePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
