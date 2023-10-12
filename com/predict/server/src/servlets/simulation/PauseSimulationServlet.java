package servlets.simulation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.SimulationService;
import utils.Auth;
import utils.Servlet;

import java.io.IOException;

@WebServlet(name = "PauseSimulationServlet", urlPatterns = {"/simulation/pause"})
public class PauseSimulationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String simulationId = req.getParameter("id");
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());

            if(!Servlet.simulationActionValidation(req, resp, simulationId, simulationService)) return;

            // pause simulation
            simulationService.pauseSimulationById(simulationId);

            Servlet.success(resp);
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
