package simulation;

import def.World;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import simulation.statistics.Statistics;
import utils.object.Grid;

import java.util.List;
import java.util.Map;

public class InvokeKit {
    private EntityInstance entityInstance;
    private EnvironmentInstance env;
    private Map<String, List<EntityInstance>> entities;
    private World world;
    private Grid grid;
    private List<EntityInstance> toCreate;
    private int ticks;

    private Context context;
    Statistics statistics;

    public InvokeKit(EntityInstance entityInstance, EnvironmentInstance env, Map<String, List<EntityInstance>> entities, World world, Grid grid, List<EntityInstance> toCreate, int ticks, Statistics statistics) {
        this.entityInstance = entityInstance;
        this.env = env;
        this.entities = entities;
        this.world = world;
        this.grid = grid;
        this.toCreate = toCreate;
        this.ticks = ticks;
        this.statistics = statistics;
    }

    public InvokeKit(InvokeKit oldIK, Context context) {
        this.entityInstance = oldIK.getEntityInstance();
        this.env = oldIK.getEnv();
        this.entities = oldIK.getEntities();
        this.world = oldIK.getWorld();
        this.grid = oldIK.getGrid();
        this.toCreate = oldIK.getToCreate();
        this.ticks = oldIK.getTicks();
        this.statistics = oldIK.getStatistics();
        this.context = context;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public InvokeKit(EntityInstance entityInstance, EnvironmentInstance env, Map<String, List<EntityInstance>> entities, World world, Grid grid, List<EntityInstance> toCreate, int ticks, Statistics statistics, Context context) {
        this.entityInstance = entityInstance;
        this.env = env;
        this.entities = entities;
        this.world = world;
        this.grid = grid;
        this.toCreate = toCreate;
        this.ticks = ticks;
        this.context = context;
        this.statistics = statistics;
    }

    public Context getContext() {
        return context;
    }

    public EntityInstance getEntityInstance() {
        return entityInstance;
    }

    public EnvironmentInstance getEnv() {
        return env;
    }

    public Map<String, List<EntityInstance>> getEntities() {
        return entities;
    }

    public World getWorld() {
        return world;
    }

    public Grid getGrid() {
        return grid;
    }

    public List<EntityInstance> getToCreate() {
        return toCreate;
    }

    public int getTicks() {
        return ticks;
    }
}
