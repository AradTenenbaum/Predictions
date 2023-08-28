package def.action.condition;

import def.Property;
import def.action.Action;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import utils.exception.SimulationException;

import java.util.List;

public class MultipleCondition extends Condition {
    private static final String AND = "and";
    private static final String OR = "or";
    private List<Condition> conditions;
    private String logical;

    public MultipleCondition(String entity, Property property, List<Action> thenActions, List<Action> elseActions, List<Condition> conditions, String logical) {
        super(entity, property, thenActions, elseActions);
        this.conditions = conditions;
        this.logical = logical;
    }

    @Override
    public Boolean isTrue(EntityInstance entityInstance, EnvironmentInstance env)throws SimulationException {
        if(logical.equals(AND)) {
            for(Condition c : conditions) {
                if(!c.isTrue(entityInstance, env)) return false;
            }
            return true;
        }
        else if(logical.equals(OR)) {
            for(Condition c : conditions) {
                if(c.isTrue(entityInstance, env)) return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "conditions=" + conditions +
                ", logical='" + logical + '\'' +
                ", thenAction=" + thenActions +
                ", elseAction=" + elseActions +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
