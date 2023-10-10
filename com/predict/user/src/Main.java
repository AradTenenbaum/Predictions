import components.login.LoginController;
import http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: remove when finished
        HttpClientUtil.init();
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/components/login/login.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        LoginController loginController = loader.getController();

        loginController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Predictions User");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
    }
}
