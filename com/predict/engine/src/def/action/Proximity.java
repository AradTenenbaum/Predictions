package def.action;

import def.World;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import utils.exception.SimulationException;
import utils.object.Grid;

import java.util.List;
import java.util.Map;

public class Proximity extends Action {
    private String targetEntity;
    private int envDepth;
    private List<Action> actions;

    public Proximity(String entity, String targetEntity, int envDepth, List<Action> actions) {
        super(ActionType.PROXIMITY, entity);
        this.targetEntity = targetEntity;
        this.envDepth = envDepth;
        this.actions = actions;
    }

    @Override
    public void invoke(EntityInstance entityInstance, EnvironmentInstance env, Map<String, List<EntityInstance>> entities, World world, Grid grid) throws SimulationException {
        System.out.println("Invoke proximity");
        entities.get(targetEntity).stream().filter(EntityInstance::getAlive).forEach(target -> {
            int xDistance = Math.abs(entityInstance.getPosition().getX() - target.getPosition().getX());
            int yDistance = Math.abs(entityInstance.getPosition().getY() - target.getPosition().getY());
            if(xDistance <= envDepth && yDistance <= envDepth) {
                System.out.println("Look here: " + entityInstance.getPosition() +" " + target.getPosition());
                actions.forEach(action -> {
                    entities.get(action.getEntity()).stream().filter(EntityInstance::getAlive).forEach(entityInstance1 -> {
                            if(entityInstance.getAlive()) {
                                try {
                                    action.invoke(entityInstance, env, entities, world, grid);
                                } catch (SimulationException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    });
                });
            }
        });
    }
}
