package components.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import simulation.Simulation;
import simulation.statistics.EntityStatistics;
import simulation.statistics.PropertyStatistics;
import utils.SimpleItem;

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

    public StatisticsController() {
        tableStatisticsItems = new ArrayList<>();
    }

    public void setCurrentSimulation(Simulation currentSimulation) {
        this.currentSimulation = currentSimulation;
        setDisplay();
    }

    private void setDisplay() {
        title.setText("Simulation: " + currentSimulation.getId());
        ticksLabel.setText("Ticks: " + currentSimulation.getTicks());
        runtimeLabel.setText("Runtime: " + currentSimulation.getRunTime() + " sec");
        setGraph();
        setTree();
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
