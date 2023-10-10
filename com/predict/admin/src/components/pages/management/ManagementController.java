package components.pages.management;

import components.pages.management.simulation.actions.ActionDependencies;
import engine.*;
import engine.actions.ActionDto;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.Navigate;
import utils.Url;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.stream.Collectors;

public class ManagementController {

    @FXML
    private Label filePathLabel;

    @FXML
    private TreeView treeView;
    private TreeItem<String> rootItem;

    @FXML
    private ListView<?> threadPoolList;

    private SimpleStringProperty filePathProp;

    private File currentFile;

    private List<WorldDto> worlds;

    ManagementDependencies managementDependencies;
    private Map<String, List<ActionDto>> actionsToRulesList;

    @FXML
    private Pane detailsDisplay;


    public ManagementController() {
        filePathProp = new SimpleStringProperty("no loaded file");
        rootItem = new TreeItem<>("Worlds");
        worlds = new ArrayList<>();
        actionsToRulesList = new HashMap<>();
    }

    @FXML
    void addFileClick(ActionEvent event) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", currentFile.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), currentFile))
                .build();

        HttpClientUtil.runAsyncPost(Constants.SIMULATION_URL, new Callback() {
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
                    System.out.println("Added successfully");
                }
                response.body().close();
                loadWorlds();
            }
        }, requestBody);

    }

    @FXML
    void loadFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(managementDependencies.getPrimaryStage());
        if (selectedFile == null) {
            return;
        }

        this.currentFile = selectedFile;

        String absolutePath = selectedFile.getAbsolutePath();

        filePathProp.set(absolutePath);
    }

    @FXML
    void setThreadsClick(ActionEvent event) {
        setThreadsDialog();
    }

    public void setManagementDependencies(ManagementDependencies managementDependencies) {
        this.managementDependencies = managementDependencies;
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
            TreeItem<String> terminationItem = new TreeItem<>("Termination");
            TreeItem<String> entitiesItem = new TreeItem<>("Entities");
            worldItem.getChildren().addAll(environmentItem, entitiesItem, rulesItem, gridItem, terminationItem);
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

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(filePathProp);
        loadWorlds();
    }

    private void setThreadsDialog() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Edit threads amount");
        dialog.setHeaderText("Set the threads amount in the thread pool");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);
        Platform.runLater(textField::requestFocus);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // todo: send put request with the number
                try {
                    int num = Integer.parseInt(textField.getText());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(num));
                    String finalUrl = (Constants.THREADS_URL);
                    HttpClientUtil.runAsyncPut(finalUrl, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Platform.runLater(() -> ErrorDialog.send(e.getMessage()));
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if(response.code() != HttpURLConnection.HTTP_OK) {
                                MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                                Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                            }
                            response.body().close();
                        }
                    }, requestBody);
                } catch (NumberFormatException e) {
                    ErrorDialog.send("Please enter a valid number");
                }
                return textField.getText();
            }
            return null;
        });

        dialog.show();
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
            } else if(item.getValue().equals("Termination")) {
                TreeItem<String> worldItem = item.getParent();
                TerminationDto terminationDto = worlds.stream().filter(worldDto -> worldItem.getValue().equals(worldDto.getName())).findFirst().get().getTermination();
                Navigate.termination(detailsDisplay, terminationDto);
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
