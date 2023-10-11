package components.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.Navigate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Pane placeholder;

    private Stage primaryStage;

    private String username;

    @FXML
    private Label userLabel;

    @FXML
    void detailsClick(ActionEvent event) {
        Navigate.details(placeholder);
    }

    @FXML
    void execClick(ActionEvent event) {

    }

    @FXML
    void requestClick(ActionEvent event) {
        Navigate.requests(placeholder);
    }

    @FXML
    void resClick(ActionEvent event) {
        Navigate.results(placeholder);
    }

    public void setUsername(String username) {
        this.username = username;
        userLabel.setText("Name: " + username);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Navigate.details(placeholder);
    }
}
