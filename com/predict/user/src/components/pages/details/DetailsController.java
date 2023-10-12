package components.pages.details;

import components.pages.details.simulation.actions.ActionDependencies;
import engine.*;
import engine.actions.ActionDto;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
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
import java.util.*;
import java.util.stream.Collectors;

public class DetailsController implements Initializable {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Pane detailsDisplay;
    private TreeItem<String> rootItem;
    private List<WorldDto> worlds;
    private Map<String, List<ActionDto>> actionsToRulesList;

    public DetailsController() {
        rootItem = new TreeItem<>("Worlds");
        actionsToRulesList = new HashMap<>();
    }

    private void updateWorldsInUI() {
        treeView.setRoot(rootItem);
        rootItem.getChildren().clear();
        worlds.forEach(worldDto -> {
            TreeItem<String> worldItem = new TreeItem<>(worldDto.getName());
            rootItem.getChildren().add(worldItem);
            TreeItem<String> environmentItem = new TreeItem<>("Environment");
            TreeItem<String> rulesItem = new TreeItem<>("Rules");
            TreeItem<String> gridItem = new TreeItem<>("Grid");
            TreeItem<String> entitiesItem = new TreeItem<>("Entities");
            worldItem.getChildren().addAll(environmentItem, entitiesItem, rulesItem, gridItem);
            worldDto.getEntities().forEach(entityDto -> {
                TreeItem<String> entity = new TreeItem<>(entityDto.getName());
                entitiesItem.getChildren().add(entity);
            });

            worldDto.getRules().forEach(ruleDto -> {
                TreeItem<String> ruleItem = new TreeItem<>(ruleDto.getName());
                actionsToRulesList.put(ruleDto.getName(), new ArrayList<>());
                ruleDto.getActions().forEach(actionDto -> {
                    actionsToRulesList.get(ruleDto.getName()).add(actionDto);
                    TreeItem<String> actionItem = new TreeItem<>(actionDto.getType());
                    ruleItem.getChildren().add(actionItem);
                });
                rulesItem.getChildren().add(ruleItem);
            });
        });

        rootItem.setExpanded(true);
    }

    private void loadWorlds() {
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
                    worlds = HttpClientUtil.fromJsonToListOfWorldDto(response.body());
                    Platform.runLater(() -> updateWorldsInUI());
                    System.out.println(worlds);
                }
                response.body().close();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWorlds();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    loadWorlds();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private boolean isAction(String value) {
        List<String> ActionTypes = Arrays.asList("proximity", "condition", "increase", "decrease", "set", "kill", "calculation", "replace");
        return ActionTypes.contains(value);
    }

    @FXML
    void selectItemInTree(MouseEvent event) {
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if(item != null) {
            if(isAction(item.getValue())) {
                TreeItem<String> ruleItem = item.getParent();
                TreeItem<String> worldItem = ruleItem.getParent().getParent();

                int actionIndex = ruleItem.getChildren().stream().filter(stringTreeItem -> stringTreeItem.getValue().equals(item.getValue())).collect(Collectors.toList()).indexOf(item);

                String url = Url.addQueryParam(
                        Url.addQueryParam(
                                Url.addQueryParam(
                                        Url.addFirstQueryParam(Constants.SIMULATION_URL, "world", worldItem.getValue()),
                                        "rule", ruleItem.getValue())
                                , "action", item.getValue()
                        ),
                        "actionIndex", String.valueOf(actionIndex)
                );
                HttpClientUtil.runAsyncGet(url, new Callback() {
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
                            Map<Object, Object> values = (Map<Object, Object>) HttpClientUtil.fromJsonToObject(response.body(), new HashMap<String, String>());
                            Platform.runLater(() -> {
                                Navigate.action(detailsDisplay, new ActionDependencies(values));
                            });
                        }
                        response.body().close();
                    }
                });
            } else if(item.getValue().equals("Grid")) {
                TreeItem<String> worldItem = item.getParent();
                GridDto gridDto = worlds.stream().filter(worldDto -> worldItem.getValue().equals(worldDto.getName())).findFirst().get().getGridDto();
                Navigate.grid(detailsDisplay, gridDto);
            } else if(item.getValue().equals("Environment")) {
                TreeItem<String> worldItem = item.getParent();
                EnvironmentDto environmentDto = worlds.stream().filter(worldDto -> worldItem.getValue().equals(worldDto.getName())).findFirst().get().getEnvironment();
                Navigate.environment(detailsDisplay, environmentDto);
            }
            else if(item.getParent() != null && item.getParent().getValue().equals("Entities")) {
                TreeItem<String> worldItem = item.getParent().getParent();
                EntityDto entityDto = worlds.
                        stream().
                        filter(worldDto -> worldItem.getValue().equals(worldDto.getName())).
                        findFirst().
                        get().
                        getEntities().
                        stream().
                        filter(entityDto1 -> entityDto1.getName().equals(item.getValue())).findFirst().get();
                Navigate.entity(detailsDisplay, entityDto);

            }
        }
    }
}
