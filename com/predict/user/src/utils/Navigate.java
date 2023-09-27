package utils;

import components.login.LoginController;
import components.main.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Navigate {
    private final static String MAIN_PATH = "/components/main/main.fxml";

    public static void mainInit(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = Navigate.class.getResource(MAIN_PATH);
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        MainController mainController = loader.getController();

        mainController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
    }

}
