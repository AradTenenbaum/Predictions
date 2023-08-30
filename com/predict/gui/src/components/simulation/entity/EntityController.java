package components.simulation.entity;

import data.dto.EntityDto;
import data.dto.PropertyDto;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import utils.object.Range;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityController {

    @FXML
    private Label entityName;

    @FXML
    private TableView<PropertyDto> propTable;

    @FXML
    private Label populationAmount;

    @FXML
    private TableColumn<PropertyDto, String> nameCol;

    @FXML
    private TableColumn<PropertyDto, String> typeCol;

    @FXML
    private TableColumn<PropertyDto, String> rangeCol;

    @FXML
    private TableColumn<PropertyDto, String> randomCol;

    private EntityDto currentEntity;

    @FXML
    private void initialize() {
        currentEntity = new EntityDto("Person", 50, Arrays.asList(
                new PropertyDto("age", "decimal", new Range(20, 70), false),
                new PropertyDto("name", "string", null),
                new PropertyDto("money", "decimal", new Range(2000, 7000), true)
        ));

        entityName.setText(currentEntity.getName());
        populationAmount.setText(String.valueOf(currentEntity.getPopulation()));

        nameCol.setCellValueFactory(
                new PropertyValueFactory<PropertyDto, String>("name")
        );
        typeCol.setCellValueFactory(
                new PropertyValueFactory<PropertyDto, String>("type")
        );
        rangeCol.setCellValueFactory(
                param -> {
                    SimpleStringProperty prop = new SimpleStringProperty();
                    if(param.getValue().getRange() != null) prop.set(param.getValue().getRange().getFrom() + " - " + param.getValue().getRange().getTo());
                    else prop.set("-");
                    return prop;
                }
        );
        randomCol.setCellValueFactory(
                param -> new SimpleStringProperty((param.getValue().getRandom() != null) ? (param.getValue().getRandom() ? "Yes" : "No") : "-")
        );

        final ObservableList<PropertyDto> data =
                FXCollections.observableArrayList(
                        currentEntity.getProperties()
                );

        propTable.setItems(data);
    }

}
