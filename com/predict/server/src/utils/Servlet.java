package utils;

import com.google.gson.Gson;
import generic.SimpleRequestObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class Servlet {
    public static void success(HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_OK);
            SimpleRequestObject obj = new SimpleRequestObject("Success");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
            throw new RuntimeException(e);
        }
    }

    public static void generalError(HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            SimpleRequestObject obj = new SimpleRequestObject("Something went wrong");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
