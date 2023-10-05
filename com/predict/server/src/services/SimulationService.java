package services;

import def.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationService {
    private ExecutorService executorService;
    private Map<String, World> worlds;

    public SimulationService() {
        executorService = Executors.newFixedThreadPool(1);
        worlds = new HashMap<>();
    }

    public void setThreads(int number) {
        this.executorService = Executors.newFixedThreadPool(number);
    }

    public void addWorld(World newWorld) {
        worlds.put(newWorld.getName(), newWorld);
    }

    public boolean hasWorld(String worldName) {
        return worlds.containsKey(worldName);
    }

    public List<String> getWorlds() {
        List<String> worldNames = new ArrayList<>();
        worlds.forEach((s, world) -> {
            worldNames.add(s);
        });
        return worldNames;
    }
}
