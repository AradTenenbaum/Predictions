package com.predict.engine.def.action.condition;

import com.predict.engine.def.Function;
import com.predict.engine.def.PropertyType;
import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.condition.Condition;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;
import com.predict.engine.utils.func.Convert;
import com.predict.engine.utils.func.RandomGenerator;
import com.predict.engine.utils.object.Range;

import java.util.List;

public class SingleCondition extends Condition {
    private final static String BT ="bt";
    private final static String LT ="lt";
    private final static String EQUALS = "=";
    private final static String NOT ="!=";
    private String operator;
    private String value;

    public SingleCondition(String entity, String property, List<Action> thenActions, List<Action> elseActions, String operator, String value) {
        super(entity, property, thenActions, elseActions);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Boolean isTrue(EntityInstance entityInstance, EnvironmentInstance env) {
        String val = Function.getFuncInput(value, entityInstance.getPropertyType(property), env);
        Object propValue;

        switch (operator) {
            case BT:
                if(PropertyType.isTypeNumber(entityInstance.getPropertyType(property))) {
                    if(entityInstance.getPropertyType(property).equals(PropertyType.DECIMAL)) {
                        // TODO: change convert to somthing better - somtimes gets a double
                        Integer number = (Integer) entityInstance.getPropertyValue(property);
                        return number > Convert.stringToInteger(val);
                    } else if (entityInstance.getPropertyType(property).equals(PropertyType.FLOAT)) {
                        Double number = (Double) entityInstance.getPropertyValue(property);
                        return number > Convert.stringToDouble(val);
                    }
                }
                break;
            case LT:
                if(PropertyType.isTypeNumber(entityInstance.getPropertyType(property))) {
                    if(entityInstance.getPropertyType(property).equals(PropertyType.DECIMAL)) {
                        Integer number = (Integer) entityInstance.getPropertyValue(property);
                        return number < Convert.stringToInteger(val);
                    } else if (entityInstance.getPropertyType(property).equals(PropertyType.FLOAT)) {
                        Double number = (Double) entityInstance.getPropertyValue(property);
                        return number < Convert.stringToDouble(val);
                    }
                }
                break;
            case EQUALS:
                propValue = entityInstance.getPropertyValue(property);
                return ("" + propValue).equals(val);
            case NOT:
                propValue = entityInstance.getPropertyValue(property);
                return !("" + propValue).equals(val);
            default:
                break;
        }

        return false;
    }


    @Override
    public String toString() {
        return "SingleCondition{" +
                "operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                ", thenAction=" + thenActions +
                ", elseAction=" + elseActions +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
