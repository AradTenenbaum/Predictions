package components.main;

import components.pages.management.ManagementDependencies;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.Navigate;

public class MainController {

    @FXML
    private Button detailsBtn;

    @FXML
    private Button execBtn;

    @FXML
    private Button resBtn;

    @FXML
    private Pane placeholder;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    void allocClick(ActionEvent event) {
        Navigate.allocations(placeholder);
    }

    @FXML
    void execHistoryClick(ActionEvent event) {
        Navigate.execHistory(placeholder);
    }

    @FXML
    void manageClick(ActionEvent event) {
        Navigate.management(placeholder, new ManagementDependencies(primaryStage));
    }

    @FXML
    private void initialize() {
        Navigate.management(placeholder, new ManagementDependencies(primaryStage));
    }

}
