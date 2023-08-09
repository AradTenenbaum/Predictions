package com.predict.engine.def.action.condition;

import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.ActionType;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;

import java.util.List;

public class Condition extends Action {
    protected List<Action> thenActions;
    protected List<Action> elseActions;

    public static String SINGLE = "single";
    public static String MULTIPLE = "multiple";
    public static enum TYPE {
        OUTER, INNER
    }

    public Condition(String entity, String property, List<Action> thenActions, List<Action> elseActions) {
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
    public void invoke(EntityInstance entityInstance, EnvironmentInstance env) throws SimulationException {
        if(isTrue(entityInstance, env)) {
            thenActions.forEach(action -> {
                try {
                    action.invoke(entityInstance, env);
                } catch (SimulationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            if(elseActions != null) {
                elseActions.forEach(action -> {
                    try {
                        action.invoke(entityInstance, env);
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
