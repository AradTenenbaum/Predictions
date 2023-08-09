package com.predict.engine.def.action;

import com.predict.engine.def.Function;
import com.predict.engine.def.PropertyType;
import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.ActionType;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;
import com.predict.engine.utils.func.Convert;

public class Calculation extends Action {
    public static enum TYPES {
        NO_TYPE, MULT, DIV
    }
    private String resultProp;
    private TYPES calcType;
    private String arg1;
    private String arg2;

    public Calculation(String entity, String resultProp, TYPES calcType, String arg1, String arg2) {
        super(ActionType.CALCULATION, entity);
        this.resultProp = resultProp;
        this.calcType = calcType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public void invoke(EntityInstance entityInstance, EnvironmentInstance env) throws SimulationException {
        Object v1 = arg1;
        Object v2 = arg2;
        String resultPropType = entityInstance.getPropertyType(resultProp);
        if(entityInstance.hasProperty(arg1)) {
            v1 = entityInstance.getPropertyValue(arg1);
        }
        if(entityInstance.hasProperty(arg2)) {
            v2 = entityInstance.getPropertyValue(arg2);
        }

        if(Function.whichFunction(arg1) != null) {
            v1 = Convert.stringToDouble(Function.getFuncInput(arg1, resultPropType, env));
        }
        if(Function.whichFunction(arg2) != null) {
            v2 = Convert.stringToDouble(Function.getFuncInput(arg2, resultPropType, env));
        }
        if(calcType == TYPES.MULT) {
            Double result = Double.parseDouble(v1.toString()) * Double.parseDouble(v2.toString());
            if(PropertyType.isDecimal(resultPropType)) {
                entityInstance.setProperty(resultProp,result.intValue());
            } else {
                entityInstance.setProperty(resultProp,result);
            }
        } else if (calcType == TYPES.DIV) {
            Double result = Double.valueOf(v1.toString()) / Double.valueOf(v2.toString());
            entityInstance.setProperty(resultProp, (PropertyType.isDecimal(resultPropType) ? result.intValue() : result));
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
