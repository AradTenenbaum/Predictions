package components.pages.exechistory.stats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class StatsController {

    @FXML
    private Label title;

    @FXML
    private Label ticksLabel;

    @FXML
    private Label runtimeLabel;

    @FXML
    private TreeView<?> treeView;

    @FXML
    private TableView<?> valuesTable;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> valueCol;

    @FXML
    private StackPane graphPlaceholder;

    @FXML
    private Button rerunBtn;

    @FXML
    void rerunSimulation(ActionEvent event) {

    }

    @FXML
    void selectItem(MouseEvent event) {

    }

}
