package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Helpers {
    public final static String COMPONENTS_PATH = "/components";
    public final static String SIMULATION_PATH = COMPONENTS_PATH + "/simulation";
    public final static String DETAILS_PATH = COMPONENTS_PATH + "/details/details.fxml";
    public final static String EXECUTION_PATH = COMPONENTS_PATH + "/execution/execution.fxml";
    public final static String RESULTS_PATH = COMPONENTS_PATH + "/results/results.fxml";
    public final static String STATISTICS_PATH = COMPONENTS_PATH + "/statistics/statistics.fxml";
    public final static String RUNTIME_PATH = COMPONENTS_PATH + "/runtime/runtime.fxml";
    public final static String ENTITY_PATH = SIMULATION_PATH + "/entity/entity.fxml";
    public final static String ENV_PATH = SIMULATION_PATH + "/environment/environment.fxml";
    public final static String ACTION_PATH = SIMULATION_PATH + "/actions/action.fxml";
    public final static String GRID_PATH = SIMULATION_PATH + "/grid/grid.fxml";
    public final static String TERMINATION_PATH = SIMULATION_PATH + "/termination/termination.fxml";


    public void loadComponent(String fxmlFile, Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane component = loader.load();
            fitParent(placeholder, component);
            component.getChildren().forEach(node -> {
                if (node instanceof SplitPane) {
                    SplitPane p = (SplitPane) node;
                    p.prefWidthProperty().bind(component.widthProperty());
                    p.prefHeightProperty().bind(component.heightProperty());
                } else if (node instanceof VBox) {
                    VBox p = (VBox) node;
                    p.prefWidthProperty().bind(component.widthProperty());
                    p.prefHeightProperty().bind(component.heightProperty());
                }

            });

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fitParent(Pane parent, Pane child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
}
