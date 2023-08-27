package def.action.condition;

import def.Property;
import def.World;
import def.action.Action;
import def.action.ActionType;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
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

    public Boolean isTrue(EntityInstance entityInstance, EnvironmentInstance env) {

        return true;
    }

    @Override
    public void invoke(EntityInstance entityInstance, EnvironmentInstance env, Map<String, List<EntityInstance>> entities, World world, Grid grid) throws SimulationException {
        if(isTrue(entityInstance, env)) {
            thenActions.forEach(action -> {
                try {
                    action.invoke(entityInstance, env, entities, world, grid);
                } catch (SimulationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            if(elseActions != null) {
                elseActions.forEach(action -> {
                    try {
                        action.invoke(entityInstance, env, entities, world, grid);
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
