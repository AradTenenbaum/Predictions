package def.action.condition;

import def.Function;
import def.Property;
import def.PropertyType;
import def.action.Action;
import def.action.SecondaryEntity;
import ins.EntityInstance;
import ins.environment.EnvironmentInstance;
import simulation.Context;
import utils.exception.SimulationException;
import utils.func.Convert;

import java.util.List;

public class SingleCondition extends Condition {
    private final static String BT ="bt";
    private final static String LT ="lt";
    private final static String EQUALS = "=";
    private final static String NOT ="!=";
    private String operator;
    private String value;

    public SingleCondition(String entity, Property property, List<Action> thenActions, List<Action> elseActions, String operator, String value, SecondaryEntity secondaryEntity) {
        super(entity, property, thenActions, elseActions, secondaryEntity);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Boolean isTrue(EntityInstance entityInstance, EnvironmentInstance env, Context context) throws SimulationException {
        EntityInstance currEntityInstance = entityInstance;
        if(context != null && entity.equals(context.getSecondEntity().getName())) currEntityInstance = context.getSecondEntity();
        String val = Function.getFuncInput(value, property.getType(), env, context);
        Object propValue = Function.getPropertyIfFunction(property.getName(), currEntityInstance);

        System.out.println("Invoke condition: " + this);
        System.out.println("Instance: " + currEntityInstance);

        switch (operator) {
            case BT:
                if(PropertyType.isTypeNumber(property.getType())) {
                    if(property.getType().equals(PropertyType.DECIMAL)) {
                        Integer number = (Integer) propValue;
                        return number > Convert.stringToInteger(val);
                    } else if (currEntityInstance.getPropertyType(property.getName()).equals(PropertyType.FLOAT)) {
                        Double number = (Double) propValue;
                        return number > Convert.stringToDouble(val);
                    }
                }
                break;
            case LT:
                if(PropertyType.isTypeNumber(property.getType())) {
                    if(property.getType().equals(PropertyType.DECIMAL)) {
                        Integer number = (Integer) propValue;
                        return number < Convert.stringToInteger(val);
                    } else if (property.getType().equals(PropertyType.FLOAT)) {
                        Double number = (Double) propValue;
                        return number < Convert.stringToDouble(val);
                    }
                }
                break;
            case EQUALS:
                propValue = propValue;
                return ("" + propValue).equals(val);
            case NOT:
                propValue = propValue;
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
