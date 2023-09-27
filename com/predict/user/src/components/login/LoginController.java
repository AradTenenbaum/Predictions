package components.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Navigate;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    private Stage primaryStage;

    @FXML
    void loginClick(ActionEvent event) throws IOException {
        System.out.println(usernameField.getText());

        Navigate.mainInit(primaryStage);
    }


    @FXML
    void registerClick(ActionEvent event) {
        System.out.println(usernameField.getText());

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
