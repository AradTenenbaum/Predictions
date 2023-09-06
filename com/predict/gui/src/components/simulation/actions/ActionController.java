package components.simulation.actions;

import data.dto.PropertyDto;
import data.dto.actions.ActionDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.SimpleItem;

import java.util.ArrayList;
import java.util.List;

public class ActionController {

    @FXML
    private Label actionLabel;

    @FXML
    private TableColumn<SimpleItem, String> nameCol;

    @FXML
    private TableColumn<SimpleItem, String> valueCol;

    @FXML
    private TableView<SimpleItem> detailsTable;

    private ActionDto currentAction;
    private List<SimpleItem> simpleItems;

    public void setCurrentAction(ActionDto currentAction) {
        this.currentAction = currentAction;
        setDisplay();
    }

    private void setDisplay() {
        simpleItems = new ArrayList<>();
        currentAction.getParams().forEach((s, s2) -> {
            simpleItems.add(new SimpleItem(s, s2));
        });
        actionLabel.setText(currentAction.getType());
        nameCol.setCellValueFactory(
                param -> param.getValue().getFirst()
        );
        valueCol.setCellValueFactory(
                param -> param.getValue().getSecond()
        );

        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        simpleItems
                );

        detailsTable.getItems().addAll(data);

    }

}
