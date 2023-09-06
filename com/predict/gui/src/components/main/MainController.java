package components.main;

import components.details.DetailsController;
import components.execution.ExecutionController;
import components.results.ResultController;
import data.dto.WorldDto;
import def.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.TasksManager;
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
    private Manager manager;
    private TasksManager tasksManager;


    @FXML
    private void showDetails() {
        loadDetails(Helpers.DETAILS_PATH, placeholder, manager.getSharedWorld());
    }

    @FXML
    private void showExecution() {
        loadExecution(Helpers.EXECUTION_PATH, placeholder, manager);
    }

    @FXML
    private void showResults() {
        loadResults(Helpers.RESULTS_PATH, placeholder);
    }

    public MainController() {
        manager = new Manager();
        selectedFileProperty = new SimpleStringProperty();
        isFileLoaded = new SimpleBooleanProperty();
        helpers = new Helpers();
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(Bindings.when(isFileLoaded).then(selectedFileProperty).otherwise("File is not loaded"));
//        showDetails();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void loadResults(String fxmlFile, Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            ResultController resultController = loader.getController();
            resultController.setManager(manager);
            resultController.setTasksManager(tasksManager);
            resultController.setPlaceholder(placeholder);
            resultController.setDisplay();
            helpers.fitParent(placeholder, component);

            HBox p = (HBox) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDetails(String fxmlFile, Pane placeholder, WorldDto worldDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            DetailsController detailsController = loader.getController();
            detailsController.setWorldDto(worldDto);
            helpers.fitParent(placeholder, component);

            SplitPane p = (SplitPane) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadExecution(String fxmlFile, Pane placeholder, Manager manager) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            ExecutionController executionController = loader.getController();
            executionController.setManager(manager);
            executionController.setTasksManager(tasksManager);
            executionController.setPlaceholder(placeholder);
            helpers.fitParent(placeholder, component);

            VBox p = (VBox) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if(tasksManager != null) {
                tasksManager.clear();
            }
            tasksManager = new TasksManager(manager);
            loadDetails(Helpers.DETAILS_PATH, placeholder,manager.getSharedWorld());
            isFileLoaded.set(true);
            selectedFileProperty.set(absolutePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
