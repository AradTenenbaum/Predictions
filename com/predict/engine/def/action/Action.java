package com.predict.engine.def.action;

import com.predict.engine.def.Function;
import com.predict.engine.def.PropertyType;
import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;
import com.predict.engine.utils.func.Convert;
import com.predict.engine.utils.func.RandomGenerator;
import com.predict.engine.utils.object.Range;

public class Action {
    protected String type;
    protected String entity;
    protected String property;
    private String by;

    public Action(String type, String entity, String property, String by) {
        this.type = type;
        this.entity = entity;
        this.property = property;
        this.by = by;
    }

    public Action(String type, String entity, String property) {
        this.type = type;
        this.entity = entity;
        this.property = property;
    }

    public Action(String type, String entity) {
        this.type = type;
        this.entity = entity;
    }

    public Action() {
    }

    public String getType() {
        return type;
    }

    public String getEntity() {
        return entity;
    }

    public String getProperty() {
        return property;
    }

    public void invoke(EntityInstance entityInstance, EnvironmentInstance env) throws SimulationException {
        String propType = "";
        Object propValue = "";
        String val = "";

        // In case the action is kill, there is no property
        if(property != null) {
            propType = entityInstance.getPropertyType(property);
            propValue = entityInstance.getPropertyValue(property);
            val = Function.getFuncInput(by, propType, env);
        }

        if(type.equals(ActionType.INCREASE) || type.equals(ActionType.DECREASE)) {
            if(propType.equals(PropertyType.DECIMAL)) {
                Integer value = Convert.stringToInteger(val);
                Integer result = (type.equals(ActionType.INCREASE) ? value + (Integer)propValue : value - (Integer)propValue);
                entityInstance.setProperty(property, result);
            } else if(propType.equals(PropertyType.FLOAT)) {
                Double value = Convert.stringToDouble(val);
                Double result = (type.equals(ActionType.INCREASE) ? value + (Double) propValue : value - (Double) propValue);
                entityInstance.setProperty(property, result);
            } else {
                throw new SimulationException("type " + propType +" is not valid for this action");
            }
        } else if(type.equals(ActionType.KILL)) {
            entityInstance.kill();
        } else if(type.equals(ActionType.SET)) {
            if(PropertyType.isDecimal(propType)) {
                entityInstance.setProperty(property, Convert.stringToInteger(val));
            } else if(PropertyType.isFloat(propType)) {
                entityInstance.setProperty(property, Convert.stringToDouble(val));
            } else if (PropertyType.isBoolean(propType)) {
                entityInstance.setProperty(property, Convert.stringToBoolean(val));
            } else {
                entityInstance.setProperty(property, val);
            }
        } else {
            throw new SimulationException("action " + type +" is not a valid action");
        }
    }

    @Override
    public String toString() {
        return "Action{" +
                "type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                ", by=" + by +
                '}';
    }
}
