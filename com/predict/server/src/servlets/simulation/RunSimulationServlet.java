package servlets.simulation;

import engine.CreateSimulationDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.RequestDto;
import services.SimulationService;
import simulation.Simulation;
import utils.Auth;
import utils.Servlet;
import utils.exception.ValidationException;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RunSimulationServlet", urlPatterns = {"/simulation/run"})
public class RunSimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = Auth.user(req, resp);
            if(username == null) return;

            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            CreateSimulationDto createSimulationDto = (CreateSimulationDto) Servlet.fromJsonToObject(req.getReader(), new CreateSimulationDto());

            // check if request id is the user's request
            if(!simulationService.isUserOwnRequest(username, createSimulationDto.getRequestId())) {
                Servlet.forbidden(resp);
                return;
            }
            // check if request is of status approved
            if(!simulationService.isRequestApproved(createSimulationDto.getRequestId())) {
                Servlet.errorMessage(resp, "Request is not approved");
                return;
            }
            // create a simulation with the received details
            Simulation s = simulationService.createSimulation(createSimulationDto);

            // run the simulation as a thread
            simulationService.runSimulation(s);

            Servlet.successWithMessage(resp, "Running simulation " + s.getId());
        }  catch (Exception e) {
            Servlet.generalError(resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = Auth.user(req, resp);
            if(username == null) return;

            SimulationService simulationService = Servlet.getSimulationService(getServletContext());

            List<String> userSimulationIds = simulationService.getUserSimulationIds(username);
            Servlet.successWithObject(resp, userSimulationIds);

        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
