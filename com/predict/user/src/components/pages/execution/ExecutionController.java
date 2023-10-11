package components.pages.execution;

import engine.CreateSimulationDto;
import engine.EnvironmentDto;
import engine.PropertyDto;
import engine.WorldDto;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.Navigate;
import utils.Url;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ExecutionController implements Initializable {

    @FXML
    private ListView<String> entitiesList;

    @FXML
    private ListView<String> envList;
    private Pane placeholder;
    private String worldName;
    private WorldDto worldDto;
    private Map<String, String> envVars;
    private Map<String, Integer> populations;
    private int requestId;

    public ExecutionController() {
        envVars = new HashMap<>();
        populations = new HashMap<>();
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
        fetchWorld();
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void initEnvAndPopulations() {
        worldDto.getEntities().forEach(entityDto -> {
            populations.put(entityDto.getName(), 0);
        });

        worldDto.getEnvironment().getProperties().forEach(propertyDto -> {
            envVars.put(propertyDto.getName(), "RANDOM");
        });
    }

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    private void fetchWorld() {
        String fixedUrl = Url.addFirstQueryParam(Constants.SIMULATION_URL, "world", worldName);
        HttpClientUtil.runAsyncGet(fixedUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> ErrorDialog.send(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    worldDto = (WorldDto) HttpClientUtil.fromJsonToObject(response.body(), new WorldDto());
                    initEnvAndPopulations();
                    Platform.runLater(() -> setDisplay());
                }
                response.body().close();
            }
        });
    }

    public void setDisplay() {
        entitiesList.getItems().clear();
        envList.getItems().clear();

        populations.forEach((name, population) -> {
            entitiesList.getItems().add(name + ":" + population);
        });

        envVars.forEach((name, value) -> {
            envList.getItems().add(name + ":" + value);
        });
    }

    private void setEnvDialog(String selectedItem) {
        Dialog<String> dialog = new Dialog<>();

        String metadata[] = selectedItem.split(":");

        dialog.setTitle("Edit " + metadata[0]);
        dialog.setHeaderText("Edit environment variable "+ metadata[0] +":");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);
        Platform.runLater(textField::requestFocus);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    envVars.put(metadata[0], textField.getText());
                    setDisplay();
                } catch (Exception e) {
                    ErrorDialog.send(e.getMessage());
                }
                return textField.getText();
            }
            return null;
        });

        dialog.show();
    }

    private void setPopulationDialog(String selectedItem) {
        Dialog<String> dialog = new Dialog<>();

        String metadata[] = selectedItem.split(":");

        dialog.setTitle("Edit population of " + metadata[0]);
        dialog.setHeaderText("Edit population of "+ metadata[0] +":");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);
        Platform.runLater(textField::requestFocus);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    populations.put(metadata[0], Integer.parseInt(textField.getText()));
                    setDisplay();
                } catch (NumberFormatException e) {
                    ErrorDialog.send("Cannot convert to number");
                }
                catch (Exception e) {
                    ErrorDialog.send(e.getMessage());
                }
                return textField.getText();
            }
            return null;
        });

        dialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entitiesList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = entitiesList.getSelectionModel().getSelectedItem();

                if(selectedItem != null) setPopulationDialog(selectedItem);
            }
        });

        envList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedItem = envList.getSelectionModel().getSelectedItem();

                if(selectedItem != null) setEnvDialog(selectedItem);
            }
        });
    }

    @FXML
    void startSimulation(ActionEvent event) throws IOException {
        CreateSimulationDto createSimulationDto = new CreateSimulationDto(envVars, populations, requestId);
        String jsonData = HttpClientUtil.fromObjectToJson(createSimulationDto);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonData);

        HttpClientUtil.runAsyncPost(Constants.SIMULATION_RUN_URL, new Callback() {
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
                    Platform.runLater(() -> {
                        Navigate.results(placeholder);
                    });
                }
                response.body().close();
            }
        }, requestBody);
    }

    @FXML
    void clearParameters(ActionEvent event) {
        initEnvAndPopulations();
        setDisplay();
    }
}
