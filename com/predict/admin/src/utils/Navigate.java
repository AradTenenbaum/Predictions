package utils;

import components.pages.management.ManagementController;
import components.pages.management.ManagementDependencies;
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
    private final static String EXECUTION_HISTORY_PATH = "/components/pages/exechistory/exechistory.fxml";

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
}
