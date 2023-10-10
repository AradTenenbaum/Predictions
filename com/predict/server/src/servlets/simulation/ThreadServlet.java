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

@WebServlet(name = "ThreadServlet", urlPatterns = {"/simulation/thread"})
public class ThreadServlet extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Auth.admin(req, resp);
            Integer num = (Integer)Servlet.fromJsonToObject(req.getReader(), new Integer(0));
            SimulationService simulationService = Servlet.getSimulationService(getServletContext());
            simulationService.setThreads(num);
            System.out.println("Thread pool set to " + num);
        } catch (IOException e) {
            Servlet.generalError(resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Auth.admin(req, resp);
        SimulationService simulationService = Servlet.getSimulationService(getServletContext());

        simulationService.getThreads();
        Servlet.success(resp);
    }
}
