package servlets;

import com.google.gson.Gson;
import generic.SimpleRequestObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants;
import utils.Servlet;
import utils.Session;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromSession = Session.getUsername(req);
        resp.setContentType("application/json");
        Gson gson = new Gson();

        if(usernameFromSession == null) {
            String usernameFromParameter = req.getParameter(Constants.USERNAME);
            if(usernameFromParameter != null) {
                req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                System.out.println("User login new session: " + usernameFromParameter);
                try (PrintWriter out = resp.getWriter()) {
                    SimpleRequestObject obj = new SimpleRequestObject("Success");
                    String jsonResponse = gson.toJson(obj);
                    out.print(jsonResponse);
                    out.flush();
                }
            }
        } else {
            System.out.println("User login: " + usernameFromSession);
            Servlet.success(resp);
        }

    }
}
