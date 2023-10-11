package servlets.simulation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.SimulationService;
import utils.Auth;
import utils.Constants;
import utils.Servlet;

import java.io.IOException;

@WebServlet(name = "DetailsSimulationServlet", urlPatterns = {"/simulation/details"})
public class DetailsSimulationServlet extends HttpServlet {
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
            if(!simulationService.isSimulationOwnedByUser(username, simulationId) && !username.equals(Constants.ADMIN)) {
                Servlet.forbidden(resp);
            }

            Servlet.successWithObject(resp, simulationService.getSimulation(simulationId));
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
