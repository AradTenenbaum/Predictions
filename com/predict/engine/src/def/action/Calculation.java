package def.action;

import def.Function;
import def.Property;
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

public class Calculation extends Action {
    public enum TYPES {
        MULT, DIV
    }
    private Property resultProp;
    private TYPES calcType;
    private String arg1;
    private String arg2;

    public Calculation(String entity, Property resultProp, TYPES calcType, String arg1, String arg2, SecondaryEntity secondaryEntity) {
        super(ActionType.CALCULATION, entity, secondaryEntity);
        this.resultProp = resultProp;
        this.calcType = calcType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public void invoke(InvokeKit invokeKit) throws SimulationException {
        EntityInstance entityInstance;
        if(invokeKit.getEntityInstance().getName().equals(entity)) {
            entityInstance = invokeKit.getEntityInstance();
        }
        else if(invokeKit.getContext().getSecondEntity().getName().equals(entity)) {
            entityInstance = invokeKit.getContext().getSecondEntity();
        }
        else {
            throw new SimulationException("No such entity " + entity);
        }
        EnvironmentInstance env = invokeKit.getEnv();
        int ticks = invokeKit.getTicks();
        Context context = invokeKit.getContext();

        Object v1 = arg1;
        Object v2 = arg2;
        String resultPropType = resultProp.getType();
        if(entityInstance.hasProperty(arg1)) {
            v1 = entityInstance.getPropertyValue(arg1);
        }
        if(entityInstance.hasProperty(arg2)) {
            v2 = entityInstance.getPropertyValue(arg2);
        }

        if(Function.whichFunction(arg1) != null) {
            v1 = Convert.stringToDouble(Function.getFuncInput(arg1, resultPropType, env, context));
        }
        if(Function.whichFunction(arg2) != null) {
            v2 = Convert.stringToDouble(Function.getFuncInput(arg2, resultPropType, env, context));
        }
        if(calcType == TYPES.MULT) {
            Double result = Double.parseDouble(v1.toString()) * Double.parseDouble(v2.toString());
            if(resultProp.getRange().on(result)) {
                if(PropertyType.isDecimal(resultPropType)) {
                    entityInstance.setProperty(resultProp.getName(),result.intValue(), ticks);
                } else {
                    entityInstance.setProperty(resultProp.getName(),result, ticks);
                }
            }
        } else if (calcType == TYPES.DIV) {
            Double result = Double.valueOf(v1.toString()) / Double.valueOf(v2.toString());
            if(resultProp.getRange().on(result)) entityInstance.setProperty(resultProp.getName(), (PropertyType.isDecimal(resultPropType) ? result.intValue() : result), ticks);
        } else {
            throw new SimulationException("no such calculation " + calcType);
        }
    }

    @Override
    public String toString() {
        return "Calculation{" +
                "resultProp='" + resultProp + '\'' +
                ", calcType=" + calcType +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
