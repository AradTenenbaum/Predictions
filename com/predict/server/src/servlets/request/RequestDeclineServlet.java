package servlets.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.SimulationService;
import utils.Auth;
import utils.Servlet;

import java.io.IOException;
@WebServlet(name = "RequestDeclineServlet", urlPatterns = {"/request/decline"})
public class RequestDeclineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if(!Auth.admin(req, resp)) {
                return;
            }
            Integer requestId = Integer.valueOf(req.getParameter("id"));
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            simulationService.declineRequest(requestId);
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
