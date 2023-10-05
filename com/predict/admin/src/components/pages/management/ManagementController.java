package components.pages.management;

import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.Navigate;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

public class ManagementController {

    @FXML
    private Label filePathLabel;

    @FXML
    private TreeView<?> treeView;

    @FXML
    private ListView<?> threadPoolList;

    private SimpleStringProperty filePathProp;

    private File currentFile;

    ManagementDependencies managementDependencies;

    public ManagementController() {
        filePathProp = new SimpleStringProperty("no loaded file");
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

    @FXML
    private void initialize() {
        filePathLabel.textProperty().bind(filePathProp);
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

    @FXML
    void selectItemInTree(ContextMenuEvent event) {

    }
}
