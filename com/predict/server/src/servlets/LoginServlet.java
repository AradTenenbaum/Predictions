package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import utils.Constants;
import utils.Servlet;
import utils.Session;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String usernameFromSession = Session.getUsername(req);
        UserService userService = Servlet.getUserService(getServletContext());

        if(usernameFromSession == null) {
            String usernameFromParameter = req.getParameter(Constants.USERNAME);
            if(usernameFromParameter != null) {

                if(userService.isUserExists(usernameFromParameter)) {
                    req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                    System.out.println("User login new session: " + usernameFromParameter);
                    Servlet.success(resp);
                } else {
                    Servlet.errorMessage(resp, "No such user");
                }

            } else {
                Servlet.errorMessage(resp, "Username can't be empty");
            }
        } else {
            if(userService.isUserExists(usernameFromSession)) {
                System.out.println("User login: " + usernameFromSession);
                Servlet.success(resp);
            } else {
                Servlet.errorMessage(resp, "No such user");
            }
        }

    }
}
