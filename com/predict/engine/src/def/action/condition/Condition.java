package def.action.condition;

import def.Property;
import def.World;
import def.action.Action;
import def.action.ActionType;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import simulation.InvokeKit;
import utils.exception.SimulationException;
import utils.object.Grid;

import java.util.List;
import java.util.Map;

public class Condition extends Action {
    protected List<Action> thenActions;
    protected List<Action> elseActions;

    public static String SINGLE = "single";
    public static String MULTIPLE = "multiple";
    public static enum TYPE {
        OUTER, INNER
    }

    public Condition(String entity, Property property, List<Action> thenActions, List<Action> elseActions) {
        super(ActionType.CONDITION, entity, property);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    public Condition() {
        super();
    }

    public Boolean isTrue(EntityInstance entityInstance, EnvironmentInstance env)throws SimulationException {
        return null;
    }

    @Override
    public void invoke(InvokeKit invokeKit) throws SimulationException {
        EntityInstance entityInstance = invokeKit.getEntityInstance();
        EnvironmentInstance env = invokeKit.getEnv();

        if(isTrue(entityInstance, env)) {
            thenActions.forEach(action -> {
                try {
                    action.invoke(invokeKit);
                } catch (SimulationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            if(elseActions != null) {
                elseActions.forEach(action -> {
                    try {
                        action.invoke(invokeKit);
                    } catch (SimulationException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Override
    public String toString() {
        return "Condition{" +
                "thenAction=" + thenActions +
                ", elseAction=" + elseActions +
                '}';
    }
}
