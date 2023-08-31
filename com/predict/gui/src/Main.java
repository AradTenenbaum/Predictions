import components.details.DetailsController;
import components.main.MainController;
import components.simulation.entity.EntityController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Helpers;

import java.net.URL;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource("/components/main/main.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        FXMLLoader loaderDetails = new FXMLLoader(getClass().getResource(Helpers.DETAILS_PATH));
        loaderDetails.load();
        FXMLLoader loaderEntity = new FXMLLoader(getClass().getResource(Helpers.ENTITY_PATH));
        loaderEntity.load();

        MainController mainController = loader.getController();
        DetailsController detailsController = loaderDetails.getController();
        mainController.setDetailsController(detailsController);
        detailsController.setEntityController(loaderEntity.getController());

        mainController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Predictions");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
