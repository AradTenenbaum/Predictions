package components.pages.management;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import utils.Navigate;

import java.io.File;

public class ManagementController {

    @FXML
    private Label filePathLabel;

    @FXML
    private ListView<?> simulationsList;

    @FXML
    private ListView<?> threadPoolList;

    private SimpleStringProperty filePathProp;

    ManagementDependencies managementDependencies;

    public ManagementController() {
        filePathProp = new SimpleStringProperty("no loaded file");
    }

    @FXML
    void addFileClick(ActionEvent event) {

    }

    @FXML
    void loadFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(managementDependencies.getPrimaryStage());
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        filePathProp.set(absolutePath);
    }

    @FXML
    void setThreadsClick(ActionEvent event) {

    }

    public void setManagementDependencies(ManagementDependencies managementDependencies) {
        this.managementDependencies = managementDependencies;
    }

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(filePathProp);
    }
}
