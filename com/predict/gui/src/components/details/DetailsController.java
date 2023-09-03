package components.details;

import components.simulation.actions.ActionController;
import components.simulation.entity.EntityController;
import components.simulation.environment.EnvController;
import components.simulation.grid.GridController;
import components.simulation.termination.TerminationController;
import data.dto.*;
import data.dto.actions.ActionDto;
import def.Termination;
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
import utils.object.Grid;
import utils.object.Range;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Map<String, List<ActionDto>> actionsToRulesList;

    public DetailsController() {
        actionsToRulesList = new HashMap<>();
        rootItem = new TreeItem<>("World");
        helpers = new Helpers();
    }

    public void setWorldDto(WorldDto worldDto) {
        this.worldDto = worldDto;
        setDisplay();
    }

    private void setDisplay() {
        TreeItem<String> environmentItem = new TreeItem<>("Environment");
        TreeItem<String> rulesItem = new TreeItem<>("Rules");
        TreeItem<String> gridItem = new TreeItem<>("Grid");
        TreeItem<String> terminationItem = new TreeItem<>("Termination");
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
                actionsToRulesList.put(ruleDto.getName(), new ArrayList<>());
                ruleDto.getActions().forEach(actionDto -> {
                    actionsToRulesList.get(ruleDto.getName()).add(actionDto);
                    TreeItem<String> actionItem = new TreeItem<>((actionsToRulesList.get(ruleDto.getName()).size()) + ". " + actionDto.getType());
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

    public void loadAction(String fxmlFile, Pane placeholder, ActionDto actionDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox component = loader.load();
            ActionController actionController = loader.getController();
            actionController.setCurrentAction(actionDto);
            helpers.fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGrid(String fxmlFile, Pane placeholder, GridDto grid) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox component = loader.load();
            GridController gridController = loader.getController();
            gridController.setGrid(grid);
            helpers.fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTermination(String fxmlFile, Pane placeholder, TerminationDto terminationDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox component = loader.load();
            TerminationController terminationController = loader.getController();
            terminationController.setCurrentTermination(terminationDto);
            helpers.fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        setDisplay();
    }

    private int getIndexFromTreeItemString(String treeItemString) {
        Pattern pattern = Pattern.compile("^\\d+");

        Matcher matcher = pattern.matcher(treeItemString);

        if(matcher.find()) {
            String numberString = matcher.group();

            try {
                int number = Integer.parseInt(numberString);
                return number-1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    @FXML
    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(item.getParent() != null && actionsToRulesList.containsKey(item.getParent().getValue())) {
                loadAction(Helpers.ACTION_PATH, detailsDisplay, actionsToRulesList.get(item.getParent().getValue()).get(getIndexFromTreeItemString(item.getValue())));
            }
            else if(item.getValue().equals("Environment")) {
                loadEnv(Helpers.ENV_PATH, detailsDisplay, worldDto.getEnvironment());
            } else if(item.getValue().equals("Grid")) {
                loadGrid(Helpers.GRID_PATH, detailsDisplay, worldDto.getGridDto());
            } else if(item.getValue().equals("Termination")) {
                loadTermination(Helpers.TERMINATION_PATH, detailsDisplay, worldDto.getTermination());
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
