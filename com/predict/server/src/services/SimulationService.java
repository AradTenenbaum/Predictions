package services;

import def.Termination;
import def.World;
import engine.CreateSimulationDto;
import engine.SimulationDto;
import engine.TerminationDto;
import engine.WorldDto;
import engine.statistics.EntityStatisticsDto;
import engine.statistics.PropertyStatisticsDto;
import engine.statistics.StatisticsDto;
import generic.objects.Point;
import simulation.Manager;
import simulation.Simulation;
import simulation.statistics.Statistics;
import utils.Constants;
import utils.exception.SimulationException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulationService {
    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue tasks;
    private Map<String, World> worlds;
    private List<Request> requests;
    private Map<String, Simulation> simulations;
    private Manager manager;

    public SimulationService() {
        tasks = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS, tasks);
        worlds = new HashMap<>();
        requests = new ArrayList<>();
        manager = new Manager();
        simulations = new HashMap<>();
    }

    public void setThreads(int number) {
        threadPoolExecutor = new ThreadPoolExecutor(number, Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS, tasks);
    }

    public void getThreads() {
        System.out.println(threadPoolExecutor);
        threadPoolExecutor.getQueue().forEach(runnable -> {
            System.out.println(runnable);
        });
    }

    public void addWorld(World newWorld) {
        worlds.put(newWorld.getName(), newWorld);
    }

    public boolean hasWorld(String worldName) {
        return worlds.containsKey(worldName);
    }

    public List<WorldDto> getWorldDtos() {
        List<WorldDto> worldDtos = new ArrayList<>();
        worlds.forEach((s, world) -> {
            worldDtos.add(new WorldDto(world));
        });
        return  worldDtos;
    }

    public List<String> getWorlds() {
        List<String> worldNames = new ArrayList<>();
        worlds.forEach((s, world) -> {
            worldNames.add(s);
        });
        return worldNames;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public List<RequestFullDto> getRequests() {
        List<RequestFullDto> requestFullDtos = new ArrayList<>();

        requests.forEach(request -> requestFullDtos.add(new RequestFullDto(request.getSimulationName(), request.getRuns(), request.getTicks(), request.getSeconds(), request.isStopByUser(), request.getUsername(), request.getId(), request.getStatus().getValue())));

        return requestFullDtos;
    }

    public List<RequestFullDto> getUserRequests(String username) {
        List<RequestFullDto> requestFullDtos = new ArrayList<>();

        requests.stream().filter(request -> request.getUsername().equals(username)).collect(Collectors.toList())
                .forEach(request -> requestFullDtos.add(new RequestFullDto(request.getSimulationName(), request.getRuns(), request.getTicks(), request.getSeconds(), request.isStopByUser(), request.getUsername(), request.getId(), request.getStatus().getValue())));

        return requestFullDtos;
    }

    public void approveRequest(int id) {
        Optional<Request> requestById = requests.stream().filter(request -> request.getId() == id).findFirst();
        if(requestById.isPresent()) {
            requestById.get().setStatus(Request.STATUS.APPROVED);
        }
    }

    public void declineRequest(int id) {
        Optional<Request> requestById = requests.stream().filter(request -> request.getId() == id).findFirst();
        if(requestById.isPresent()) {
            requestById.get().setStatus(Request.STATUS.DECLINED);
        }
    }

    public boolean isUserOwnRequest(String username, int requestId) {
        Optional<Request> requestById = requests.stream().filter(request -> request.getId() == requestId).findFirst();
        return requestById.map(request -> request.getUsername().equals(username)).orElse(false);
    }

    public boolean isRequestApproved(int requestId) {
        Optional<Request> requestById = requests.stream().filter(request -> request.getId() == requestId).findFirst();
        return requestById.map(request -> request.getStatus().equals(Request.STATUS.APPROVED)).orElse(false);
    }

    public Simulation createSimulation(CreateSimulationDto createSimulationDto) throws SimulationException {
        Optional<Request> requestById = requests.stream().filter(request -> request.getId() == createSimulationDto.getRequestId()).findFirst();
        if(requestById.isPresent()) {
            World world = worlds.get(requestById.get().getSimulationName());
            Simulation s = manager.createSimulationFromReceived(createSimulationDto.getEnvironment(), createSimulationDto.getPopulations(), world);
            s.setTermination(new Termination(requestById.get().getTicks(), requestById.get().getSeconds(), requestById.get().isStopByUser()));
            s.setRequestId(requestById.get().getId());
            requestById.get().runSimulationOnRequest();
            return s;
        } else {
            return null;
        }
    }

    public void runSimulation(Simulation s) {
        simulations.put(s.getId().toString(), s);
        threadPoolExecutor.execute(() -> {
            while (!s.isStopped()) {
                s.runTick();
            }
            System.out.println("Ended simulation " + s.getId());
        });
    }

    public void stopSimulationById(String simulationId) {
        Simulation s = simulations.get(simulationId);
        s.stop();
    }

    public void pauseSimulationById(String simulationId) {
        Simulation s = simulations.get(simulationId);
        s.pause();
    }

    public void playSimulationById(String simulationId) {
        Simulation s = simulations.get(simulationId);
        s.resume();
    }

    private StatisticsDto buildStatisticsDto(Statistics statistics) {
        Map<String, EntityStatisticsDto> entityStatisticsDto = new HashMap<>();
        statistics.getEntityStatistics().forEach((entName, entityStatistics) -> {
            Map<String, PropertyStatisticsDto> propertiesStatisticsDto = new HashMap<>();
            entityStatistics.getPropertyStatistics().forEach((propName, propertyStatistics) -> {
                propertiesStatisticsDto.put(propName, new PropertyStatisticsDto(propertyStatistics.getEntAmountPerValue(), propertyStatistics.getAverageValue(), propertyStatistics.getAverageChangeTicks(), propertyStatistics.isNumeric()));
            });
            List<Point> smallEntityAmountInTimelineList = new ArrayList<>();
            int every = entityStatistics.getAmountInTimeline().size()/20;
            AtomicInteger i = new AtomicInteger();

            entityStatistics.getAmountInTimeline().forEach(point -> {
                if(i.get()%every == 0) {
                    smallEntityAmountInTimelineList.add(point);
                }
                i.getAndIncrement();
            });
            entityStatisticsDto.put(entName, new EntityStatisticsDto(entityStatistics.getAliveAmount(), entityStatistics.getDeadAmount(),smallEntityAmountInTimelineList, propertiesStatisticsDto));
        });
        return new StatisticsDto(entityStatisticsDto);
    }

    public SimulationDto getSimulation(String simulationId) {
        Simulation s = simulations.get(simulationId);
        SimulationDto.MODES mode = (s.isStopped() ? SimulationDto.MODES.STATS : SimulationDto.MODES.RUNTIME);
        StatisticsDto statisticsDto = (s.isStopped() ? buildStatisticsDto(s.getStatistics()) : new StatisticsDto());
        return new SimulationDto(s.getId(), (int) s.getRunTime(), s.getTicks(), s.getProgress(), s.getEntitiesPopulations(), mode, statisticsDto, new TerminationDto(s.getTermination()));
    }

    public boolean isSimulationExists(String id) {
        return simulations.containsKey(id);
    }

    public boolean isSimulationOwnedByUser(String username, String simulationId) {
        if(isSimulationExists(simulationId)) {
            int requestId = simulations.get(simulationId).getRequestId();
            Optional<Request> requestById = requests.stream().filter(request -> request.getId() == requestId).findFirst();
            if(requestById.isPresent()) return requestById.get().getUsername().equals(username);
            return false;
        }
        return false;
    }

    public List<String> getUserSimulationIds(String username) {
        List<String> simulationIds = new ArrayList<>();

        simulations.forEach((s, simulation) -> {
            if(username.equals(Constants.ADMIN)) {
                if(simulation.isStopped()) {
                    simulationIds.add(s);
                }
            } else {
                Optional<Request> requestById = requests.stream().filter(request -> request.getId() == simulation.getRequestId()).findFirst();
                if(requestById.isPresent()) {
                    if(requestById.get().getUsername().equals(username)) {
                       simulationIds.add(s);
                    }
                }
            }
        });

        return simulationIds;
    }
}
