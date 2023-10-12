package servlets.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.Request;
import services.RequestDto;
import services.SimulationService;
import utils.Auth;
import utils.Constants;
import utils.Servlet;

import java.io.IOException;

@WebServlet(name = "RequestsServlet", urlPatterns = {"/request"})
public class RequestsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Auth.user(req, resp);
        if(username == null) return;

        try {
            RequestDto requestDto = (RequestDto) Servlet.fromJsonToObject(req.getReader(), new RequestDto());

            if(requestDto.getRuns() < 1) {
                Servlet.errorMessage(resp, "Runs must be a number greater than 0");
                return;
            }

            if(requestDto.getSeconds() < -1 || requestDto.getTicks() < -1) {
                Servlet.errorMessage(resp, "Ticks and Seconds must be positive numbers or empty");
                return;
            }

            if(requestDto.getSeconds() == -1 && requestDto.getTicks() == -1 && !requestDto.isStopByUser()) {
                Servlet.errorMessage(resp, "There must be at least one termination rule");
                return;
            }

            SimulationService simulationService = Servlet.getSimulationService(getServletContext());

            if(!simulationService.hasWorld(requestDto.getSimulationName())) {
                Servlet.errorMessage(resp, "No such world");
            } else {
                simulationService.addRequest(new Request(username, requestDto.getSimulationName(), requestDto.getRuns(), requestDto.getTicks(), requestDto.getSeconds(), requestDto.isStopByUser()));
                Servlet.success(resp);
            }
        } catch (Exception e) {
            Servlet.generalError(resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Auth.user(req, resp);
        if(username == null) return;

        try {
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());

            if(username.equals(Constants.ADMIN)) {
                Servlet.successWithObject(resp, simulationService.getRequests());
            } else {
                Servlet.successWithObject(resp, simulationService.getUserRequests(username));
            }
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
