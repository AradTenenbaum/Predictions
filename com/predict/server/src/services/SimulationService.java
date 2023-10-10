package services;

import def.World;
import engine.WorldDto;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class SimulationService {
    private ExecutorService executorService;
    private ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue tasks;
    private Map<String, World> worlds;
    private List<Request> requests;

    public SimulationService() {
        tasks = new LinkedBlockingQueue<>();
        executorService = Executors.newFixedThreadPool(1);
        threadPoolExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS, tasks);
        worlds = new HashMap<>();
        requests = new ArrayList<>();
    }

    public void setThreads(int number) {
        this.executorService = Executors.newFixedThreadPool(number);
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
}
