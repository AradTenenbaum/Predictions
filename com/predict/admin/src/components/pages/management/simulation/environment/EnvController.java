package components.pages.management.simulation.environment;

import engine.EnvironmentDto;
import engine.PropertyDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EnvController {

    @FXML
    private TableView<PropertyDto> propTable;

    @FXML
    private TableColumn<PropertyDto, String> nameCol;

    @FXML
    private TableColumn<PropertyDto, String> typeCol;

    private EnvironmentDto currentEnv;

    public void setCurrentEnv(EnvironmentDto currentEnv) {
        this.currentEnv = currentEnv;
        setDisplay();
    }

    private void setDisplay() {
        nameCol.setCellValueFactory(
                new PropertyValueFactory<PropertyDto, String>("name")
        );
        typeCol.setCellValueFactory(
                new PropertyValueFactory<PropertyDto, String>("type")
        );

        final ObservableList<PropertyDto> data =
                FXCollections.observableArrayList(
                        currentEnv.getProperties()
                );

        propTable.setItems(data);
    }
}
