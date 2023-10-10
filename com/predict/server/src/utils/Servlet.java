package utils;

import com.google.gson.Gson;
//import generic.SimpleRequestObject;
import com.google.gson.reflect.TypeToken;
import engine.WorldDto;
import generic.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import services.SimulationService;
import services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.List;

public class Servlet {

    private static final Object userServiceLock = new Object();
    private static final Object simulationServiceLock = new Object();

    public static void success(HttpServletResponse resp) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_OK);
            MessageObject obj = new MessageObject("Success");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
            throw new RuntimeException(e);
        }
    }

    public static void successWithObject(HttpServletResponse resp, Object obj) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_OK);
            String json = gson.toJson(obj, obj.getClass());
            out.println(json);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(HttpURLConnection.HTTP_UNAVAILABLE);
            throw new RuntimeException(e);
        }
    }

    public static void generalError(HttpServletResponse resp) {
        errorMessage(resp, "Something went wrong");
    }

    public static void errorMessage(HttpServletResponse resp, String message) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
            MessageObject obj = new MessageObject(message);
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void forbidden(HttpServletResponse resp) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
            MessageObject obj = new MessageObject("Resource is not available for this user");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void notFound(HttpServletResponse resp) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
            MessageObject obj = new MessageObject("Resource was not found");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unauthorized(HttpServletResponse resp) {
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            resp.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
            MessageObject obj = new MessageObject("Please authenticate to the system");
            String json = gson.toJson(obj);
            out.println(json);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserService getUserService(ServletContext servletContext) {

        synchronized (userServiceLock) {
            if (servletContext.getAttribute(Constants.USER_SERVICE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.USER_SERVICE_ATTRIBUTE_NAME, new UserService());
            }
        }
        return (UserService) servletContext.getAttribute(Constants.USER_SERVICE_ATTRIBUTE_NAME);
    }

    public static SimulationService getSimulationService(ServletContext servletContext) {

        synchronized (simulationServiceLock) {
            if (servletContext.getAttribute(Constants.SIMULATION_SERVICE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Constants.SIMULATION_SERVICE_ATTRIBUTE_NAME, new SimulationService());
            }
        }
        return (SimulationService) servletContext.getAttribute(Constants.SIMULATION_SERVICE_ATTRIBUTE_NAME);
    }

    public static Object fromJsonToObject(BufferedReader reader, Object obj) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        Gson gson = new Gson();
        return gson.fromJson(requestBody.toString(), obj.getClass());
    }
}
