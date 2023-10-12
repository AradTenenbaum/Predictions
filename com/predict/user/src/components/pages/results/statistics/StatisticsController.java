package components.pages.results.statistics;

import engine.SimulationDto;
import engine.statistics.EntityStatisticsDto;
import engine.statistics.PropertyStatisticsDto;
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
import utils.SimpleItem;

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
    @FXML
    private StackPane graphPlaceholder;
    @FXML
    private Button rerunBtn;
    private SimulationDto simulationDto;


    public StatisticsController() {
        tableStatisticsItems = new ArrayList<>();
    }

    public void setSimulationDto(SimulationDto simulationDto) {
        this.simulationDto = simulationDto;
        setDisplay();
    }

    private void setDisplay() {
        title.setText("Simulation: " + simulationDto.getId());
        ticksLabel.setText("Ticks: " + simulationDto.getTicks());
        runtimeLabel.setText("Runtime: " + simulationDto.getRunTimeSeconds() + " sec");
        setGraph();
        setTree();
    }


    private void setGraph() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Instances");
        LineChart<Number, Number> graph = new LineChart<Number,Number>(xAxis,yAxis);

        simulationDto.getStatistics().getEntityStatisticsDto().forEach((entityName, entityStatisticsDto) -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entityName);

            entityStatisticsDto.getAmountInTimeline().forEach(point -> {
                series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
            });

            graph.getData().add(series);
        });

        graphPlaceholder.getChildren().setAll(graph);
    }

    private void setTree() {
        TreeItem<String> root = new TreeItem<>("Entities");
        simulationDto.getStatistics().getEntityStatisticsDto().forEach((entityName, entityStatisticsDto) -> {
            TreeItem<String> entity = new TreeItem<>(entityName);
            entityStatisticsDto.getPropertiesStatisticsDto().forEach((propertyName, propertyStatisticsDto) -> {
                TreeItem<String> property = new TreeItem<>(propertyName);
                entity.getChildren().add(property);
            });
            root.getChildren().add(entity);
        });
        treeView.setRoot(root);
    }

    private void displayEntityData(String entityName) {
        tableStatisticsItems.clear();
        EntityStatisticsDto entityStatisticsDto = simulationDto.getStatistics().getEntityStatisticsDto().get(entityName);
        tableStatisticsItems.add(new SimpleItem("Alive amount", String.valueOf(entityStatisticsDto.getAliveAmount())));
        tableStatisticsItems.add(new SimpleItem("Dead amount", String.valueOf(entityStatisticsDto.getDeadAmount())));
        final ObservableList<SimpleItem> data =
                FXCollections.observableArrayList(
                        tableStatisticsItems
                );

        valuesTable.getItems().clear();
        valuesTable.getItems().addAll(data);
    }

    private void displayPropertyData(String entityName, String propertyName) {
        tableStatisticsItems.clear();
        PropertyStatisticsDto propertyStatisticsDto = simulationDto.getStatistics().getEntityStatisticsDto().get(entityName).getPropertiesStatisticsDto().get(propertyName);
        tableStatisticsItems.add(new SimpleItem("AVG ticks value const", String.valueOf(propertyStatisticsDto.getAvgChangeTicks())));
        propertyStatisticsDto.getEntAmountPerValue().forEach((value, number) -> {
            tableStatisticsItems.add(new SimpleItem("Amount with value " + value, String.valueOf(number)));
        });

        if(propertyStatisticsDto.isNumeric()) {
            tableStatisticsItems.add(new SimpleItem("AVG value ", String.valueOf(propertyStatisticsDto.getAvgValue())));
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

    @FXML
    void rerunSimulation(ActionEvent event) {

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
