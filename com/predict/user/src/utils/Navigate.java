package utils;

import components.login.LoginController;
import components.main.MainController;
import components.pages.details.simulation.actions.ActionController;
import components.pages.details.simulation.actions.ActionDependencies;
import components.pages.details.simulation.entity.EntityController;
import components.pages.details.simulation.environment.EnvController;
import components.pages.details.simulation.grid.GridController;
import components.pages.execution.ExecutionController;
import components.pages.requests.RequestsController;
import components.pages.results.ResultController;
import components.pages.results.runtime.RuntimeController;
import components.pages.results.statistics.StatisticsController;
import engine.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Navigate {
    private final static String MAIN_PATH = "/components/main/main.fxml";
    private final static String REQUESTS_PATH = "/components/pages/requests/requests.fxml";
    private final static String DETAILS_PATH = "/components/pages/details/details.fxml";
    private final static String EXECUTION_PATH = "/components/pages/execution/execution.fxml";
    private final static String RESULTS_PATH = "/components/pages/results/results.fxml";
    private final static String RUNTIME_PATH = "/components/pages/results/runtime/runtime.fxml";
    private final static String STATISTICS_PATH = "/components/pages/results/statistics/statistics.fxml";
    private final static String ACTION_PATH = "/components/pages/details/simulation/actions/action.fxml";
    private final static String GRID_PATH = "/components/pages/details/simulation/grid/grid.fxml";
    private final static String ENVIRONMENT_PATH = "/components/pages/details/simulation/environment/environment.fxml";
    private final static String ENTITY_PATH = "/components/pages/details/simulation/entity/entity.fxml";

    private static void fitParent(Pane parent, VBox child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, HBox child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, Pane child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, SplitPane child) {
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
            RequestsController requestsController = loader.getController();
            requestsController.setPlaceholder(placeholder);
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void details(Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(DETAILS_PATH));
            SplitPane component = loader.load();
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void action(Pane placeholder, ActionDependencies actionDependencies) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(ACTION_PATH));
            VBox component = loader.load();
            ActionController actionController = loader.getController();
            actionController.setActionDependencies(actionDependencies);
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void grid(Pane placeholder, GridDto gridDto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(GRID_PATH));
            VBox component = loader.load();
            GridController gridController = loader.getController();
            gridController.setGrid(gridDto);
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void environment(Pane placeholder, EnvironmentDto environmentDto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(ENVIRONMENT_PATH));
            Pane component = loader.load();
            EnvController envController = loader.getController();
            envController.setCurrentEnv(environmentDto);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void entity(Pane placeholder, EntityDto entityDto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(ENTITY_PATH));
            Pane component = loader.load();
            EntityController entityController = loader.getController();
            entityController.setCurrentEntity(entityDto);
            fitParent(placeholder, component);
            fitParent(component, (VBox) component.getChildren().get(0));

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void execution(Pane placeholder, String simulationName, int requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(EXECUTION_PATH));
            Pane component = loader.load();
            ExecutionController executionController = loader.getController();
            executionController.setPlaceholder(placeholder);
            executionController.setWorldName(simulationName);
            executionController.setRequestId(requestId);

            fitParent(placeholder, component);
            fitParent(component, (VBox) component.getChildren().get(0));

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void results(Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(RESULTS_PATH));
            Pane component = loader.load();
            ResultController resultController = loader.getController();
            resultController.setPlaceholder(placeholder);

            fitParent(placeholder, component);
            fitParent(component, (HBox) component.getChildren().get(0));

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runtime(Pane placeholder, VBox simulationPlaceholder, SimulationDto simulationDto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(RUNTIME_PATH));
            Pane component = loader.load();
            RuntimeController runtimeController = loader.getController();
            runtimeController.setPlaceholder(placeholder);
            runtimeController.setSimulationPlaceholder(simulationPlaceholder);
            runtimeController.setSimulationDto(simulationDto);

            fitParent(simulationPlaceholder, component);

            simulationPlaceholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void statistics(VBox simulationPlaceholder, SimulationDto simulationDto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(STATISTICS_PATH));
            Pane component = loader.load();
            StatisticsController statisticsController = loader.getController();
            statisticsController.setSimulationDto(simulationDto);

            fitParent(simulationPlaceholder, component);

            simulationPlaceholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
