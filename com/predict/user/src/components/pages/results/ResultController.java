package components.pages.results;

import engine.SimulationDto;
import engine.WorldDto;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.Navigate;
import utils.Url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    private ListView<String> execList;
    private Pane placeholder;
    @FXML
    private VBox simulationPlaceholder;
    private List<String> simulationIds;

    public ResultController() {
        simulationIds = new ArrayList<>();
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    public void setDisplay() {
        execList.getItems().clear();
        for(int i = 0; i < simulationIds.size(); i++) {
            execList.getItems().add(simulationIds.get(i));
        }
    }

    public void fetchSimulationIds() {
        HttpClientUtil.runAsyncGet(Constants.SIMULATION_RUN_URL, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorDialog.send(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    simulationIds = (List<String>) HttpClientUtil.fromJsonToObject(response.body(), new ArrayList<String>());
                    Platform.runLater(() -> {
                        setDisplay();
                    });
                }
                response.body().close();
            }
        });
    }

    private void displaySimulation(SimulationDto simulationDto) {
        if(simulationDto.isOnRuntimeMode()) {
            Navigate.runtime(placeholder, simulationPlaceholder, simulationDto);
        } else if(simulationDto.isOnStatsMode()) {

        }
    }

    private void fetchSimulation(String simulationId) {
        String fixedUrl = Url.addFirstQueryParam(Constants.SIMULATION_DETAILS_URL, "id", simulationId);
        HttpClientUtil.runAsyncGet(fixedUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ErrorDialog.send(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    SimulationDto simulationDto = (SimulationDto) HttpClientUtil.fromJsonToObject(response.body(), new SimulationDto());
                    Platform.runLater(() -> {
                        displaySimulation(simulationDto);
                    });
                }
                response.body().close();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        execList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String simulationId = execList.getSelectionModel().getSelectedItem();
                fetchSimulation(simulationId);
            }
        });

        fetchSimulationIds();
    }

}
