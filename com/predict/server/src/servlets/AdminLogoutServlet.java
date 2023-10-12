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

@WebServlet(name = "AdminLogoutServlet", urlPatterns = {"/admin/logout"})
public class AdminLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!Auth.admin(req, resp)) {
            return;
        }

        UserService userService = Servlet.getUserService(getServletContext());

        userService.adminLogout();

        Servlet.success(resp);
    }
}
