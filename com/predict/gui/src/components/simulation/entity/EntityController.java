package components.simulation.entity;

import data.dto.EntityDto;
import data.dto.PropertyDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


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

    public void setCurrentEntity(EntityDto currentEntity) {
        this.currentEntity = currentEntity;
        setDisplay();
    }

    private void setDisplay() {
        if(currentEntity != null) {
            entityName.setText(currentEntity.getName());
            populationAmount.setText("Population: " + currentEntity.getPopulation());

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
                    param -> new SimpleStringProperty((param.getValue().getRandom() != null) ? (param.getValue().getRandom() ? "Yes" : "No") : "No")
            );

            final ObservableList<PropertyDto> data =
                    FXCollections.observableArrayList(
                            currentEntity.getProperties()
                    );

            propTable.setItems(data);
        }
    }

    @FXML
    private void initialize() {

    }

}
