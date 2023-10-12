package servlets.simulation;

import data.source.File;
import data.validation.Validation;
import def.World;
import engine.RuleDto;
import engine.WorldDto;
import engine.actions.ActionDto;
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
import utils.func.Convert;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "SimulationServlet", urlPatterns = {"/simulation"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class SimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!Auth.admin(req, resp)) {
            return;
        }
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
        try {
            String username = Auth.user(req, resp);
            if(username == null) return;

            String world = req.getParameter("world");
            String action = req.getParameter("action");
            String actionIndex = req.getParameter("actionIndex");
            String rule = req.getParameter("rule");

            SimulationService simulationService = Servlet.getSimulationService(getServletContext());

            List<WorldDto> worldDtos = simulationService.getWorldDtos();

            if(world != null) {
                Optional<WorldDto> worldDto = worldDtos.stream().filter(wdto -> wdto.getName().equals(world)).findFirst();
                if(worldDto.isPresent()) {
                    if(rule != null) {
                        Optional<RuleDto> ruleDto = worldDto.get().getRules().stream().filter(rdto -> rdto.getName().equals(rule)).findFirst();
                        if(ruleDto.isPresent()){
                            if(action != null && actionIndex != null) {
                                if(Validation.isInteger(actionIndex)) {
                                    int index = Convert.stringToInteger(actionIndex);
                                    List<ActionDto> actionDto = ruleDto.get().getActions().stream().filter(adto -> adto.getType().equals(action)).collect(Collectors.toList());
                                    if(actionDto.get(index) != null) {
                                        Servlet.successWithObject(resp, actionDto.get(index));
                                    } else {
                                        Servlet.notFound(resp);
                                    }
                                } else {
                                    Servlet.notFound(resp);
                                }

                            } else {
                                Servlet.successWithObject(resp, ruleDto.get());
                            }
                        } else {
                            Servlet.notFound(resp);
                        }
                    } else {
                        Servlet.successWithObject(resp, worldDto.get());
                    }
                } else {
                    Servlet.notFound(resp);
                }
            }
            else {
                Servlet.successWithObject(resp, worldDtos);
            }
        } catch (Exception e) {
            Servlet.generalError(resp);
        }
    }
}
