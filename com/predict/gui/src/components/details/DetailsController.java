package components.details;

import components.simulation.entity.EntityController;
import data.dto.EntityDto;
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
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
    private Map<String, String> itemToPath;
    private Helpers helpers;
    private SimpleStringProperty selectedItem;
    private EntityController entityController;
    private TreeItem<String> entitiesItem;
    private TreeItem<String> rootItem;
    private WorldDto worldDto;

    public DetailsController() {
        rootItem = new TreeItem<>("Simulation");
        entitiesItem = new TreeItem<>("Entities");
        selectedItem = new SimpleStringProperty();
        helpers = new Helpers();
        itemToPath = new HashMap<>();
    }

    public void setWorldDto(WorldDto worldDto) {
        this.worldDto = worldDto;
        setDisplay();
    }

    public void setEntityController(EntityController entityController) {
        this.entityController = entityController;
    }

    private void setDisplay() {
//        itemToPath.put("Simulation", Helpers);
        TreeItem<String> environmentItem = new TreeItem<>("Environment");
//        itemToPath.put("Environment", Helpers);
        TreeItem<String> rulesItem = new TreeItem<>("Rules");
//        itemToPath.put("Rules", Helpers);
        TreeItem<String> gridItem = new TreeItem<>("Grid");
//        itemToPath.put("Grid", Helpers);
        TreeItem<String> terminationItem = new TreeItem<>("Termination");
//        itemToPath.put("Termination", Helpers);


        rootItem.getChildren().addAll(environmentItem, entitiesItem, rulesItem, gridItem, terminationItem);
        if(worldDto != null) {
            worldDto.getEntities().forEach(entityDto -> {
                TreeItem<String> entity = new TreeItem<>(entityDto.getName());
                entitiesItem.getChildren().add(entity);
                itemToPath.put(entityDto.getName(), Helpers.ENTITY_PATH);
            });
        }
        treeView.setRoot(rootItem);

    }

    @FXML
    private void initialize() {
        setDisplay();
    }

    @FXML
    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(itemToPath.containsKey(item.getValue())) {
                helpers.loadComponent(itemToPath.get(item.getValue()), detailsDisplay);
                Optional<EntityDto> foundEntityDto = worldDto.getEntities().stream().filter(entityDto -> entityDto.getName().equals(item.getValue())).findFirst();
                foundEntityDto.ifPresent(entityDto -> entityController.setCurrentEntity(entityDto));
            }
        }
    }
}
