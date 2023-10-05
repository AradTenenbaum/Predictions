package servlets.simulation;

import data.source.File;
import def.World;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import services.SimulationService;
import utils.Auth;
import utils.Servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SimulationServlet", urlPatterns = {"/simulation"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class SimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Auth.admin(req, resp);
        SimulationService simulationService = Servlet.getSimulationService(getServletContext());

        Collection<Part> parts = req.getParts();
        Optional<Part> part = parts.stream().findFirst();
        if (part.isPresent()) {
            InputStream inputStream = part.get().getInputStream();
            try {
                World world = File.createWorldFromInputStream(inputStream);

                if(simulationService.hasWorld(world.getName())) {
                    Servlet.errorMessage(resp, "Simulation with the name " + world.getName() + " is already exists");
                }

                simulationService.addWorld(world);
                Servlet.success(resp);
                System.out.println("Admin added world " + world.getName());
            } catch (Exception e) {
                Servlet.errorMessage(resp, e.getMessage());
            }
        } else {
            Servlet.errorMessage(resp, "Invalid file");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Auth.admin(req, resp);
        SimulationService simulationService = Servlet.getSimulationService(getServletContext());

        List<String> simNames = simulationService.getWorlds();
        Servlet.successWithObject(resp, simNames);

    }
}
