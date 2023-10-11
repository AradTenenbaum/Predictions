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

@WebServlet(name = "StopSimulationServlet", urlPatterns = {"/simulation/stop"})
public class StopSimulationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = Auth.user(req, resp);
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            String simulationId = req.getParameter("id");

            // check if simulation exists
            if(!simulationService.isSimulationExists(simulationId)) {
                Servlet.errorMessage(resp, "No such simulation");
            }

            // check if the user owns the simulation
            if(!simulationService.isSimulationOwnedByUser(username, simulationId)) {
                Servlet.forbidden(resp);
            }

            // stop simulation
            simulationService.stopSimulationById(simulationId);

            Servlet.success(resp);
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
