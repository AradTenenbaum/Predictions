package components.login;

import errors.ErrorDialog;
import generic.MessageObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import http.HttpClientUtil;
import utils.Constants;
import utils.Navigate;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginController {

    @FXML
    private TextField usernameField;

    private Stage primaryStage;

    private void request(String url) {
        String username = usernameField.getText();
        String finalUrl = (url + username);

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    ErrorDialog.send(e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() != HttpURLConnection.HTTP_OK) {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                } else {
                    Platform.runLater(() -> {
                        try {
                            Navigate.mainInit(primaryStage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    @FXML
    void loginClick(ActionEvent event) throws IOException {
       request(Constants.LOGIN_URL);
    }


    @FXML
    void registerClick(ActionEvent event) {
        request(Constants.REGISTER_URL);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
