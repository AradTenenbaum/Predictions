package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import utils.Auth;
import utils.Constants;
import utils.Servlet;

import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = Servlet.getUserService(getServletContext());

        if(userService.isAdminConnected()) {
            Servlet.forbidden(resp);
            return;
        } else {
            userService.adminConnected();
        }

        req.getSession(true).setAttribute(Constants.USERNAME, Constants.ADMIN);
        Servlet.success(resp);
    }
}
