package components.details;

import components.simulation.entity.EntityController;
import components.simulation.environment.EnvController;
import data.dto.EntityDto;
import data.dto.EnvironmentDto;
import data.dto.PropertyDto;
import data.dto.WorldDto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.Helpers;
import utils.object.Range;

import java.io.IOException;
import java.util.*;

public class DetailsController {
    @FXML
    TreeView treeView;
    @FXML
    Pane detailsDisplay;
    @FXML
    Pane screenContainer;
    private Helpers helpers;
    private TreeItem<String> rootItem;
    private WorldDto worldDto;

    public DetailsController() {
        rootItem = new TreeItem<>("Simulation");
        helpers = new Helpers();
    }

    public void setWorldDto(WorldDto worldDto) {
        this.worldDto = worldDto;
        setDisplay();
    }

    private void setDisplay() {
//        itemToPath.put("Simulation", Helpers);
        TreeItem<String> environmentItem = new TreeItem<>("Environment");
        TreeItem<String> rulesItem = new TreeItem<>("Rules");
//        itemToPath.put("Rules", Helpers);
        TreeItem<String> gridItem = new TreeItem<>("Grid");
//        itemToPath.put("Grid", Helpers);
        TreeItem<String> terminationItem = new TreeItem<>("Termination");
//        itemToPath.put("Termination", Helpers);
        TreeItem<String> entitiesItem = new TreeItem<>("Entities");

        rootItem.getChildren().clear();
        rootItem.getChildren().addAll(environmentItem, entitiesItem, rulesItem, gridItem, terminationItem);
        if(worldDto != null) {
            worldDto.getEntities().forEach(entityDto -> {
                TreeItem<String> entity = new TreeItem<>(entityDto.getName());
                entitiesItem.getChildren().add(entity);
            });

            worldDto.getRules().forEach(ruleDto -> {
                TreeItem<String> ruleItem = new TreeItem<>(ruleDto.getName());
                ruleDto.getActions().forEach(s -> {
                    TreeItem<String> actionItem = new TreeItem<>(s);
                    ruleItem.getChildren().add(actionItem);
                });
                rulesItem.getChildren().add(ruleItem);
            });
        }
        treeView.setRoot(rootItem);

    }

    public void loadEntity(String fxmlFile, Pane placeholder, EntityDto entityDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            EntityController entityController = loader.getController();
            entityController.setCurrentEntity(entityDto);
            helpers.fitParent(placeholder, component);
            VBox p = (VBox) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadEnv(String fxmlFile, Pane placeholder, EnvironmentDto environmentDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            EnvController envController = loader.getController();
            envController.setCurrentEnv(environmentDto);
            helpers.fitParent(placeholder, component);
            TableView p = (TableView) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        setDisplay();
    }

    @FXML
    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(item.getValue().equals("Environment")) {
                loadEnv(Helpers.ENV_PATH, detailsDisplay, worldDto.getEnvironment());
            }
            else {
                Optional<EntityDto> foundEntityDto = worldDto.getEntities().stream().filter(entityDto -> entityDto.getName().equals(item.getValue())).findFirst();
                if (foundEntityDto.isPresent()) {
                    loadEntity(Helpers.ENTITY_PATH, detailsDisplay, foundEntityDto.get());
                }
            }
        }
    }
}
