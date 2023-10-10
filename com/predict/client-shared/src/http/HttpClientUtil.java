package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engine.WorldDto;
import engine.actions.ActionDto;
import okhttp3.*;
import services.RequestFullDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpClientUtil {
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void init() {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }

    public static void runAsyncGet(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPut(String finalUrl, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .put(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, Callback callback, RequestBody body) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static Object fromJsonToObject(ResponseBody body, Object obj) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(body.string(), obj.getClass());
    }

    public static String fromObjectToJson(Object obj) throws IOException {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static List<WorldDto> fromJsonToListOfWorldDto(ResponseBody body) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(body.string(), new TypeToken<List<WorldDto>>(){}.getType());
    }

    public static List<RequestFullDto> fromJsonToListOfRequestFullDto(ResponseBody body) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(body.string(), new TypeToken<List<RequestFullDto>>(){}.getType());
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
