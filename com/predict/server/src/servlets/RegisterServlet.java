package servlets;

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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = Servlet.getUserService(getServletContext());
        String usernameFromParameter = req.getParameter(Constants.USERNAME);

        if(userService.isUserExists(usernameFromParameter)) {
            Servlet.errorMessage(resp, "User already exists");
        } else {
            userService.addUser(usernameFromParameter);
            System.out.println("User " + usernameFromParameter + " successfully added");
            req.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
            Servlet.success(resp);
        }
    }
}
