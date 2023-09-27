package components.pages.management;

import javafx.stage.Stage;

public class ManagementDependencies {
    private Stage primaryStage;

    public ManagementDependencies(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
