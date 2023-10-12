import components.main.MainController;
import errors.ErrorDialog;
import generic.MessageObject;
import http.HttpClientUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.System.exit;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: remove when finished
        HttpClientUtil.init();
        HttpClientUtil.runAsyncGet(Constants.ADMIN_URL, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
                try {
                    stop();
                    exit(1);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    Platform.runLater(() -> {
                        FXMLLoader loader = new FXMLLoader();

                        URL mainFXML = getClass().getResource("/components/main/main.fxml");
                        loader.setLocation(mainFXML);
                        VBox root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        MainController mainController = loader.getController();

                        mainController.setPrimaryStage(primaryStage);

                        primaryStage.setTitle("Predictions Admin");
                        Scene scene = new Scene(root, 1050, 600);
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    });
                } else {
                    MessageObject messageObject = (MessageObject)HttpClientUtil.fromJsonToObject(response.body(), new MessageObject(""));
                    Platform.runLater(() -> ErrorDialog.send(messageObject.getMessage()));
                }
                response.body().close();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.runAsyncGet(Constants.ADMIN_LOGOUT_URL, new Callback() {
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
                    System.out.println("Admin logged out");
                }
                response.body().close();
            }
        });
        HttpClientUtil.shutdown();
    }
}
