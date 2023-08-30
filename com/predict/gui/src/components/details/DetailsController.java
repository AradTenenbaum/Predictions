package components.details;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import utils.Helpers;

import java.util.HashMap;
import java.util.Map;

public class DetailsController {
    @FXML
    TreeView treeView;
    @FXML
    Pane detailsDisplay;

    private Map<String, String> itemToPath;
    private Helpers helpers;
    private SimpleStringProperty selectedItem;

    public DetailsController() {
        selectedItem = new SimpleStringProperty();
        helpers = new Helpers();
        itemToPath = new HashMap<>();
    }

    @FXML
    private void initialize() {
        // TODO: bind selected items
        TreeItem<String> rootItem = new TreeItem<>("Simulation");
//        itemToPath.put("Simulation", Helpers);
        TreeItem<String> environmentItem = new TreeItem<>("Environment");
//        itemToPath.put("Environment", Helpers);
        TreeItem<String> entitiesItem = new TreeItem<>("Entities");
        TreeItem<String> entityItem1 = new TreeItem<>("Entity1");
        TreeItem<String> entityItem2 = new TreeItem<>("Entity2");
        TreeItem<String> entityItem3 = new TreeItem<>("Entity3");
        itemToPath.put("Entity1", Helpers.ENTITY_PATH);
        TreeItem<String> rulesItem = new TreeItem<>("Rules");
//        itemToPath.put("Rules", Helpers);
        TreeItem<String> gridItem = new TreeItem<>("Grid");
//        itemToPath.put("Grid", Helpers);
        TreeItem<String> terminationItem = new TreeItem<>("Termination");
//        itemToPath.put("Termination", Helpers);

        rootItem.getChildren().addAll(environmentItem, entitiesItem, rulesItem, gridItem, terminationItem);
        entitiesItem.getChildren().addAll(entityItem1, entityItem2, entityItem3);

        treeView.setRoot(rootItem);
    }

    @FXML
    public void selectItem() {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(itemToPath.containsKey(item.getValue())) {
                helpers.loadComponent(itemToPath.get(item.getValue()), detailsDisplay);
            }
            System.out.println(item.getValue());
        }
    }
}
