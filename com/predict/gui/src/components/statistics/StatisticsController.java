package components.statistics;

import components.execution.ExecutionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import logic.TasksManager;
import simulation.Manager;
import simulation.Simulation;
import simulation.statistics.EntityStatistics;
import simulation.statistics.PropertyStatistics;
import utils.Helpers;
import utils.SimpleItem;
import utils.exception.SimulationException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsController implements Initializable {

    @FXML
    private Label title;
    @FXML
    private Label ticksLabel;
    @FXML
    private Label runtimeLabel;
    @FXML
    private TreeView treeView;
    @FXML
    private TableView<SimpleItem> valuesTable;
    @FXML
    private TableColumn<SimpleItem, String> nameCol;
    @FXML
    private TableColumn<SimpleItem, String> valueCol;
    private List<SimpleItem> tableStatisticsItems;
    private Simulation currentSimulation;
    @FXML
    private StackPane graphPlaceholder;
    private Helpers helpers;
    private Pane placeholder;
    private Manager manager;
    private TasksManager tasksManager;
    @FXML
    private Button rerunBtn;


    public StatisticsController() {
        tableStatisticsItems = new ArrayList<>();
        helpers = new Helpers();
    }

    public void setCurrentSimulation(Simulation currentSimulation) {
        this.currentSimulation = currentSimulation;
        setDisplay();
    }

    private void setDisplay() {
        title.setText("Simulation: " + currentSimulation.getId());
        ticksLabel.setText("Ticks: " + currentSimulation.getTicks());
        runtimeLabel.setText("Runtime: " + currentSimulation.getRunTime() + " sec");
        rerunBtn.setDisable((currentSimulation.getWorldVersion() != manager.getWorldVersion()));
        setGraph();
        setTree();
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setTasksManager(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    private void setGraph() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Instances");
        LineChart<Number, Number> graph = new LineChart<Number,Number>(xAxis,yAxis);

        currentSimulation.getStatistics().getEntityStatistics().forEach((entityName, entityStatistics) -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entityName);

            int every = entityStatistics.getAmountInTimeline().size()/10;
            AtomicInteger i = new AtomicInteger();

            entityStatistics.getAmountInTimeline().forEach(point -> {
                if(i.get()%every == 0) {
                    series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
                }
                i.getAndIncrement();
            });

            graph.getData().add(series);
        });

        graphPlaceholder.getChildren().setAll(graph);
    }

    private void setTree() {
        TreeItem<String> root = new TreeItem<>("Entities");
        currentSimulation.getStatistics().getEntityStatistics().forEach((entityName, entityStatistics) -> {
            TreeItem<String> entity = new TreeItem<>(entityName);
            entityStatistics.getPropertyStatistics().forEach((propertyName, propertyStatistics) -> {
                TreeItem<String> property = new TreeItem<>(propertyName);
                entity.getChildren().add(property);
            });
            root.getChildren().add(entity);
        });
        treeView.setRoot(root);
    }

    private void displayEntityData(String entityName) {
        tableStatisticsItems.clear();
        EntityStatistics entityStatistics = currentSimulation.getStatistics().getEntity(entityName);
        tableStatisticsItems.add(new SimpleItem("Alive amount", String.valueOf(entityStatistics.getAliveAmount())));
        tableStatisticsItems.add(new SimpleItem("Dead amount", String.valueOf(entityStatistics.getDeadAmount())));
        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        tableStatisticsItems
                );

        valuesTable.getItems().clear();
        valuesTable.getItems().addAll(data);
    }

    private void displayPropertyData(String entityName, String propertyName) {
        tableStatisticsItems.clear();
        PropertyStatistics propertyStatistics = currentSimulation.getStatistics().getEntity(entityName).getProperty(propertyName);
        tableStatisticsItems.add(new SimpleItem("AVG ticks value const", String.valueOf(propertyStatistics.getAverageChangeTicks())));
        propertyStatistics.getEntAmountPerValue().forEach((value, number) -> {
            tableStatisticsItems.add(new SimpleItem("Amount with value " + value, String.valueOf(number)));
        });

        if(propertyStatistics.isNumeric()) {
            tableStatisticsItems.add(new SimpleItem("AVG value ", String.valueOf(propertyStatistics.getAverageValue())));
        }

        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        tableStatisticsItems
                );

        valuesTable.getItems().clear();
        valuesTable.getItems().addAll(data);
    }

    @FXML
    void selectItem(MouseEvent event) {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(item.getParent() != null) {
                if(item.getParent().getValue().equals("Entities")) {
                    displayEntityData(item.getValue());
                } else {
                    displayPropertyData(item.getParent().getValue(), item.getValue());
                }
            }
        }
    }

    public void loadExecution() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Helpers.EXECUTION_PATH));
            Pane component = loader.load();
            ExecutionController executionController = loader.getController();
            executionController.setManager(manager);
            executionController.setTasksManager(tasksManager);
            executionController.setPlaceholder(placeholder);
            helpers.fitParent(placeholder, component);

            VBox p = (VBox) component.getChildren().get(0);
            p.prefWidthProperty().bind(component.widthProperty());
            p.prefHeightProperty().bind(component.heightProperty());

            placeholder.getChildren().setAll(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rerunSimulation(ActionEvent event) {
        try {
            manager.setEnvironmentInstance(currentSimulation.getEnvironmentInstance(), currentSimulation.getPopulations(), currentSimulation.getWorldVersion());
            manager.setPopulations(currentSimulation.getPopulations());
            loadExecution();
        } catch (SimulationException e) {
            helpers.openErrorDialog(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameCol.setCellValueFactory(
                param -> param.getValue().getFirst()
        );
        valueCol.setCellValueFactory(
                param -> param.getValue().getSecond()
        );
    }
}
