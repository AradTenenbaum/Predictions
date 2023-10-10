package utils;

import components.login.LoginController;
import components.main.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Navigate {
    private final static String MAIN_PATH = "/components/main/main.fxml";
    private final static String REQUESTS_PATH = "/components/pages/requests/requests.fxml";

    private static void fitParent(Pane parent, VBox child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }

    public static void mainInit(Stage primaryStage, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = Navigate.class.getResource(MAIN_PATH);
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        MainController mainController = loader.getController();
        mainController.setUsername(username);
        mainController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
    }

    public static void requests(Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(REQUESTS_PATH));
            VBox component = loader.load();
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
