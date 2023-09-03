package def.action;

import def.Function;
import def.PropertyType;
import def.World;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import simulation.Context;
import simulation.InvokeKit;
import utils.exception.SimulationException;
import utils.func.Convert;
import utils.object.Grid;

import java.util.List;
import java.util.Map;

public class Proximity extends Action {
    private String targetEntity;
    private String envDepth;
    private List<Action> actions;

    public Proximity(String entity, String targetEntity, String envDepth, List<Action> actions) {
        super(ActionType.PROXIMITY, entity, null);
        this.targetEntity = targetEntity;
        this.envDepth = envDepth;
        this.actions = actions;
    }

    @Override
    public void invoke(InvokeKit invokeKit) throws SimulationException {
        EntityInstance entityInstance = invokeKit.getEntityInstance();
        Map<String, List<EntityInstance>> entities = invokeKit.getEntities();
        EnvironmentInstance env = invokeKit.getEnv();
        String val = Function.getFuncInput(envDepth, PropertyType.DECIMAL, env, null);
        int envDepthValue = Convert.stringToDouble(val).intValue();

        entities.get(targetEntity).stream().filter(EntityInstance::getAlive).forEach(target -> {
            int xDistance = Math.abs(entityInstance.getPosition().getX() - target.getPosition().getX());
            int yDistance = Math.abs(entityInstance.getPosition().getY() - target.getPosition().getY());
            if(xDistance <= envDepthValue && yDistance <= envDepthValue) {
                actions.forEach(action -> {
                    if(entityInstance.getAlive()) {
                        try {
                            action.invoke(new InvokeKit(invokeKit, new Context(target)));
                        } catch (SimulationException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
}
