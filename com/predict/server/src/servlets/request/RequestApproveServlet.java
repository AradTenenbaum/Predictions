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

@WebServlet(name = "RequestApproveServlet", urlPatterns = {"/request/approve"})
public class RequestApproveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Auth.admin(req, resp);
            Integer requestId = Integer.valueOf(req.getParameter("id"));
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            simulationService.approveRequest(requestId);
            Servlet.success(resp);
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
