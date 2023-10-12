package utils;

import components.pages.management.ManagementController;
import components.pages.management.ManagementDependencies;
import components.pages.management.simulation.actions.ActionController;
import components.pages.management.simulation.actions.ActionDependencies;
import components.pages.management.simulation.entity.EntityController;
import components.pages.management.simulation.environment.EnvController;
import components.pages.management.simulation.grid.GridController;
import components.pages.results.statistics.StatisticsController;
import engine.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;

public class Navigate {
    private final static String MANAGEMENT_PATH = "/components/pages/management/management.fxml";
    private final static String ALLOCATIONS_PATH = "/components/pages/allocations/allocations.fxml";
    private final static String EXECUTION_HISTORY_PATH = "/components/pages/results/results.fxml";
    private final static String STATISTICS_PATH = "/components/pages/results/statistics/statistics.fxml";
    private final static String ACTION_PATH = "/components/pages/management/simulation/actions/action.fxml";
    private final static String GRID_PATH = "/components/pages/management/simulation/grid/grid.fxml";
    private final static String ENVIRONMENT_PATH = "/components/pages/management/simulation/environment/environment.fxml";
    private final static String ENTITY_PATH = "/components/pages/management/simulation/entity/entity.fxml";

    private static void fitParent(Pane parent, Pane child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, GridPane child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, TableView child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, HBox child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }
    private static void fitParent(Pane parent, VBox child) {
        child.prefWidthProperty().bind(parent.widthProperty());
        child.prefHeightProperty().bind(parent.heightProperty());
    }

    public static void execHistory(Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(EXECUTION_HISTORY_PATH));
            Pane component = loader.load();
            fitParent(placeholder, component);
            fitParent(component, (HBox) component.getChildren().get(0));

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void management(Pane placeholder, ManagementDependencies managementDependencies) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(MANAGEMENT_PATH));
            GridPane component = loader.load();
            ManagementController managementController = loader.getController();
            managementController.setManagementDependencies(managementDependencies);
            fitParent(placeholder, component);

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void allocations(Pane placeholder) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigate.class.getResource(ALLOCATIONS_PATH));
            TableView component = loader.load();
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
