package components.pages.details.simulation.actions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.SimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionController {

    @FXML
    private Label actionLabel;

    @FXML
    private TableColumn<SimpleItem, String> nameCol;

    @FXML
    private TableColumn<SimpleItem, String> valueCol;

    @FXML
    private TableView<SimpleItem> detailsTable;

    private ActionDependencies actionDependencies;
    private List<SimpleItem> simpleItems;

    public ActionController() {
    }

    public void setActionDependencies(ActionDependencies actionDependencies) {
        this.actionDependencies = actionDependencies;
        setDisplay();
    }

    private void setDisplay() {
        Map<Object, Object> actionData = actionDependencies.getActionData();
        simpleItems = new ArrayList<>();
        actionData.forEach((s, s2) -> {
            System.out.println(s+ " " + s2);
            simpleItems.add(new SimpleItem(String.valueOf(s), String.valueOf(s2)));
        });
        actionLabel.setText(String.valueOf(actionData.get("type")));
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
