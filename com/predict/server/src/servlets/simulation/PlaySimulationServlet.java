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

@WebServlet(name = "PlaySimulationServlet", urlPatterns = {"/simulation/play"})
public class PlaySimulationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            String simulationId = req.getParameter("id");

            if(!Servlet.simulationActionValidation(req, resp, simulationId, simulationService)) return;

            // play simulation
            simulationService.playSimulationById(simulationId);

            Servlet.success(resp);
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
