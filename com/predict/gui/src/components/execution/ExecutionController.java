package components.execution;

import components.results.ResultController;
import data.dto.WorldDto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.TasksManager;
import logic.tasks.RunSimulationTask;
import simulation.Manager;
import utils.Helpers;
import utils.exception.SimulationException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionController implements Initializable {

    @FXML
    private ListView<String> entitiesList;

    @FXML
    private ListView<String> envList;

    @FXML
    private Button clearBtn;

    @FXML
    private Button startBtn;
    private Manager manager;
    private Pane placeholder;
    private TasksManager tasksManager;
    private Helpers helpers;

    public ExecutionController() {
        helpers = new Helpers();
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        setDisplay();
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setDisplay() {
        entitiesList.getItems().clear();
        envList.getItems().clear();

        manager.getSharedWorld().getEntities().forEach(entityDto -> {
            entitiesList.getItems().add(entityDto.getName() + ":" + entityDto.getPopulation());
        });

        manager.getSharedWorld().getEnvironment().getProperties().forEach(propertyDto -> {
            String envValue = (manager.isRandomEnvVar(propertyDto.getName()) ? "Random" : manager.getEnvValue(propertyDto.getName()));
            envList.getItems().add(propertyDto.getName() + ":" + envValue);
        });
    }

    private void setEnvDialog(String selectedItem) {
        Dialog<String> dialog = new Dialog<>();

        String metadata[] = selectedItem.split(":");
        System.out.println(metadata[0] + " " + metadata[1]);

        dialog.setTitle("Edit " + metadata[0]);
        dialog.setHeaderText("Edit environment variable "+ metadata[0] +":");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);
        Platform.runLater(textField::requestFocus);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                manager.setEnvVar(metadata[0], textField.getText());
                setDisplay();
                return textField.getText();
            }
            return null;
        });

        dialog.show();
    }

    private void setPopulationDialog(String selectedItem) {
        Dialog<String> dialog = new Dialog<>();

        String metadata[] = selectedItem.split(":");
        System.out.println(metadata[0] + " " + metadata[1]);

        dialog.setTitle("Edit population of " + metadata[0]);
        dialog.setHeaderText("Edit population of "+ metadata[0] +":");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);
        Platform.runLater(textField::requestFocus);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                manager.setPopulation(metadata[0], Integer.parseInt(textField.getText()));
                setDisplay();
                return textField.getText();
            }
            return null;
        });

        dialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entitiesList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = entitiesList.getSelectionModel().getSelectedItem();

                setPopulationDialog(selectedItem);
            }
        });

        envList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = envList.getSelectionModel().getSelectedItem();

                setEnvDialog(selectedItem);
            }
        });
    }

    public void loadResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.RESULTS_PATH));
            Pane component = loader.load();
            ResultController resultController = loader.getController();
            resultController.setManager(manager);
            resultController.setTasksManager(tasksManager);
            tasksManager.runOne();
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

    @FXML
    void startSimulation(ActionEvent event) {
        try {
            loadResults();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void clearParameters(ActionEvent event) {
        manager.clearEnv();
        setDisplay();
    }
}
