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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Action implements Serializable {
    protected String type;
    protected String entity;
    protected Property property;
    protected Optional<SecondaryEntity> secondaryEntity;
    private String by;


    public Action(String type, String entity, Property property, String by, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.entity = entity;
        this.property = property;
        this.by = by;
        this.secondaryEntity = Optional.ofNullable(secondaryEntity);
    }

    public Action(String type, String entity, Property property, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.entity = entity;
        this.property = property;
        this.secondaryEntity = Optional.ofNullable(secondaryEntity);
    }

    public Action(String type, String entity, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.entity = entity;
        this.secondaryEntity = Optional.ofNullable(secondaryEntity);
    }

    public Action() {
    }

    public Optional<SecondaryEntity> getSecondaryEntity() {
        return secondaryEntity;
    }

    public String getType() {
        return type;
    }

    public String getEntity() {
        return entity;
    }

    public Property getProperty() {
        return property;
    }

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

        String propType = "";
        Object propValue = "";
        String val = "";

        // In case the action is kill, there is no property
        if(property != null) {
            propType = property.getType();
            propValue = entityInstance.getPropertyValue(property.getName());
            val = Function.getFuncInput(by, propType, env, context);
        }

        if(type.equals(ActionType.INCREASE) || type.equals(ActionType.DECREASE)) {
            if(propType.equals(PropertyType.DECIMAL)) {
                Integer value = Convert.stringToInteger(val);
                Integer result = (type.equals(ActionType.INCREASE) ? value + (Integer)propValue : value - (Integer)propValue);
                if (property.getRange().on(result.doubleValue())) entityInstance.setProperty(property.getName(), result, ticks);
            } else if(propType.equals(PropertyType.FLOAT)) {
                Double value = Convert.stringToDouble(val);
                Double result = (type.equals(ActionType.INCREASE) ? value + (Double) propValue : value - (Double) propValue);
                if (property.getRange().on(result)) entityInstance.setProperty(property.getName(), result, ticks);
            } else {
                throw new SimulationException("type " + propType +" is not valid for this action");
            }
        } else if(type.equals(ActionType.KILL)) {
            entityInstance.kill();
        } else if(type.equals(ActionType.SET)) {
            if(PropertyType.isDecimal(propType)) {
                Integer result = Convert.stringToInteger(val);
                if(property.getRange().on(result.doubleValue())) entityInstance.setProperty(property.getName(), result, ticks);
            } else if(PropertyType.isFloat(propType)) {
                Double result = Convert.stringToDouble(val);
                if(property.getRange().on(result)) entityInstance.setProperty(property.getName(), result, ticks);
            } else if (PropertyType.isBoolean(propType)) {
                Boolean result = Convert.stringToBoolean(val);
                entityInstance.setProperty(property.getName(), result, ticks);
            } else {
                entityInstance.setProperty(property.getName(), val, ticks);
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
