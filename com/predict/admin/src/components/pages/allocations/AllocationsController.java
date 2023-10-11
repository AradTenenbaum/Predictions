package components.pages.allocations;

import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import services.RequestFullDto;
import utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AllocationsController implements Initializable {

    @FXML
    private TableView<RequestFullDto> requestsTable;

    @FXML
    private TableColumn<RequestFullDto, Integer> reqCol;

    @FXML
    private TableColumn<RequestFullDto, String> simCol;

    @FXML
    private TableColumn<RequestFullDto, String> userCol;

    @FXML
    private TableColumn<RequestFullDto, Integer> runsCol;

    @FXML
    private TableColumn<RequestFullDto, String> terCol;

    @FXML
    private TableColumn<RequestFullDto, String> statusCol;

    @FXML
    private TableColumn<RequestFullDto, Void> approveCol;

    @FXML
    private TableColumn<RequestFullDto, Void> declineCol;

    private List<RequestFullDto> requestFullDtos;

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
        reqCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, Integer>("id")
        );
        simCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("simulationName")
        );
        runsCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, Integer>("runs")
        );
        statusCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("status")
        );
        userCol.setCellValueFactory(
                new PropertyValueFactory<RequestFullDto, String>("username")
        );

        terCol.setCellValueFactory(
                param -> {
                    String terminationRules = "";
                    if(param.getValue().isStopByUser()) {
                        terminationRules += "Stop by user";
                    }
                    if(param.getValue().getSeconds() != -1) {
                        if(!terminationRules.equals("")) terminationRules += " | ";
                        terminationRules += (param.getValue().getSeconds() + " Seconds");
                    }
                    if(param.getValue().getTicks() != -1) {
                        if(!terminationRules.equals("")) terminationRules += " | ";
                        terminationRules += (param.getValue().getTicks() + " Ticks");
                    }

                    return new SimpleStringProperty(terminationRules);
                }
        );

        javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>> cellFactoryApprove = new javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>>() {
            @Override
            public TableCell<RequestFullDto, Void> call(final TableColumn<RequestFullDto, Void> param) {
                final TableCell<RequestFullDto, Void> cell = new TableCell<RequestFullDto, Void>() {

                    private final Button btn = new Button("Approve");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            RequestFullDto requestFullDto = getTableView().getItems().get(getIndex());
                            if(requestFullDto.getStatus().equals("waiting")) {
                                HttpClientUtil.runAsyncGet((Constants.REQUEST_APPROVE_URL + "?id=" + requestFullDto.getId()), new Callback() {
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
                                        }
                                        response.body().close();
                                    }
                                });
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
        approveCol.setCellFactory(cellFactoryApprove);

        javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>> cellFactoryDecline = new javafx.util.Callback<TableColumn<RequestFullDto, Void>, TableCell<RequestFullDto, Void>>() {
            @Override
            public TableCell<RequestFullDto, Void> call(final TableColumn<RequestFullDto, Void> param) {
                final TableCell<RequestFullDto, Void> cell = new TableCell<RequestFullDto, Void>() {

                    private final Button btn = new Button("Decline");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            RequestFullDto requestFullDto = getTableView().getItems().get(getIndex());
                            if(requestFullDto.getStatus().equals("waiting")) {
                                HttpClientUtil.runAsyncGet((Constants.REQUEST_DECLINE_URL + "?id=" + requestFullDto.getId()), new Callback() {
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
                                        }
                                        response.body().close();
                                    }
                                });
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
        declineCol.setCellFactory(cellFactoryDecline);

        final ObservableList<RequestFullDto> data =
                FXCollections.observableArrayList(
                        requestFullDtos
                );

        requestsTable.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestFullDtos = new ArrayList<>();
        fetchRequests();
    }
}
