package components.statistics;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import simulation.Simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsController {

    @FXML
    private Label title;

    @FXML
    private Label ticksLabel;

    @FXML
    private Label runtimeLabel;

    @FXML
    private TreeView<?> treeView;

    @FXML
    private TableView<?> valuesTable;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> valueCol;

    private Simulation currentSimulation;


    @FXML
    private StackPane graphPlaceholder;

    public void setCurrentSimulation(Simulation currentSimulation) {
        this.currentSimulation = currentSimulation;
        setDisplay();
    }

    public void setDisplay() {
        title.setText("Simulation: " + currentSimulation.getId());
        ticksLabel.setText("Ticks: " + currentSimulation.getTicks());
        runtimeLabel.setText("Runtime: " + currentSimulation.getRunTime() + " sec");
        setGraph();
    }

    public void setGraph() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Instances");
        LineChart<Number, Number> graph = new LineChart<Number,Number>(xAxis,yAxis);

        currentSimulation.getStatistics().getEntityStatistics().forEach((entityName, entityStatistics) -> {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entityName);

            int every = entityStatistics.getAmountInTimeline().size()/20;
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
}
