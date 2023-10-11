package components.pages.requests;

import engine.PropertyDto;
import engine.WorldDto;
import errors.ErrorDialog;
import generic.MessageObject;
import generic.functions.Validation;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import services.RequestDto;
import services.RequestFullDto;
import utils.Constants;
import utils.Navigate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RequestsController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField runsTextField;

    @FXML
    private TextField ticksTextField;

    @FXML
    private TextField secTextField;

    @FXML
    private CheckBox stopByUserCheckbox;

    @FXML
    private TableColumn<RequestFullDto, String> simCol;

    @FXML
    private TableColumn<RequestFullDto, String> runCol;

    @FXML
    private TableColumn<RequestFullDto, String> tickCol;

    @FXML
    private TableColumn<RequestFullDto, String> secCol;

    @FXML
    private TableColumn<RequestFullDto, String> stopByUserCol;

    @FXML
    private TableColumn<RequestFullDto, String> statusCol;

    @FXML
    private TableColumn<RequestFullDto, Void> execCol;

    @FXML
    private TableView<RequestFullDto> reqTable;

    private List<RequestFullDto> requestFullDtos;
    private Pane placeholder;

    public void setPlaceholder(Pane placeholder) {
        this.placeholder = placeholder;
    }

    @FXML
    void onSubmitRequest(MouseEvent event) {
        try {

            int runs;
            int ticks = -1;
            int seconds = -1;

            if(choiceBox.getValue().equals("")) {
                ErrorDialog.send("Please choose a simulation before sending the request");
                return;
            }

            if(!Validation.isInteger(runsTextField.getText())) {
                ErrorDialog.send("Runs must be a number");
                return;
            } else {
                runs = Integer.parseInt(runsTextField.getText());
            }

            if(!Validation.isInteger(ticksTextField.getText()) && !ticksTextField.getText().equals("")) {
                ErrorDialog.send("Ticks must be a number");
                return;
            } else {
                if(Validation.isInteger(ticksTextField.getText())) ticks = Integer.parseInt(ticksTextField.getText());
            }

            if(!Validation.isInteger(secTextField.getText()) && !secTextField.getText().equals("")) {
                ErrorDialog.send("Seconds field must be a number");
                return;
            } else {
                if(Validation.isInteger(secTextField.getText())) seconds = Integer.parseInt(secTextField.getText());
            }

            RequestDto requestDto = new RequestDto(choiceBox.getValue(), runs, ticks, seconds, stopByUserCheckbox.isSelected());
            String jsonData = HttpClientUtil.fromObjectToJson(requestDto);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonData);

            HttpClientUtil.runAsyncPost(Constants.REQUEST_URL, new Callback() {
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
                        fetchRequests();
                        System.out.println("Success");
                    }
                    response.body().close();
                }
            }, requestBody);
        } catch (Exception e) {
            ErrorDialog.send("An error occurred");
        }
    }

    private void fetchSimulationOptions() {
        HttpClientUtil.runAsyncGet(Constants.SIMULATION_URL, new Callback() {
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
                    List<WorldDto> worlds = HttpClientUtil.fromJsonToListOfWorldDto(response.body());
                    Platform.runLater(() -> worlds.forEach(worldDto -> choiceBox.getItems().add(worldDto.getName())));
                }
                response.body().close();
            }
        });
    }

    private void fetchRequests() {
        HttpClientUtil.runAsyncGet(Constants.REQUEST_URL, new Callback() {
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
                    requestFullDtos = HttpClientUtil.fromJsonToListOfRequestFullDto(response.body());
                    Platform.runLater(() -> {
                        setUpTableUI();
                    });
                }
                response.body().close();
            }
        });
    }

    private void setUpTableUI() {
        simCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("simulationName")
        );
        runCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("runs")
        );
        tickCol.setCellValueFactory(
                param -> new SimpleStringProperty((param.getValue().getTicks() == -1 ? "-" : String.valueOf(param.getValue().getTicks())))
        );
        secCol.setCellValueFactory(
                param -> new SimpleStringProperty((param.getValue().getSeconds() == -1 ? "-" : String.valueOf(param.getValue().getSeconds())))
        );
        stopByUserCol.setCellValueFactory(
                param -> new SimpleStringProperty(String.valueOf(param.getValue().isStopByUser()))
        );
        statusCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("status")
        );

        javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>> cellFactory = new javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>>() {
            @Override
            public TableCell<RequestFullDto, Void> call(final TableColumn<RequestFullDto, Void> param) {
                final TableCell<RequestFullDto, Void> cell = new TableCell<RequestFullDto, Void>() {

                    private final Button btn = new Button("Execute");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            RequestFullDto requestFullDto = getTableView().getItems().get(getIndex());
                            if(requestFullDto.getStatus().equals("approved")) {
                                Navigate.execution(placeholder, requestFullDto.getSimulationName(), requestFullDto.getId());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        execCol.setCellFactory(cellFactory);

        final ObservableList<RequestFullDto> data =
                FXCollections.observableArrayList(
                        requestFullDtos
                );

        reqTable.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestFullDtos = new ArrayList<>();
        fetchSimulationOptions();
        fetchRequests();
    }
}
