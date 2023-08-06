package com.predict.engine.data.validation;

import com.predict.engine.def.Entity;
import com.predict.engine.def.Environment;
import com.predict.engine.def.Function;
import com.predict.engine.def.PropertyType;
import com.predict.engine.utils.exceptions.ValidationException;

import java.util.Map;

public class Validation {
    public static void isTypeValid(Environment environment, Map<String, Entity> entities, String entity, String property, String value) throws ValidationException {
        String type = entities.get(entity).getProperties().get(property).getType();
        String func = Function.whichFunction(value);
        if(func != null) {
            if(func.equals(Function.ENVIRONMENT)) {
                String envVar = Function.getEnvVarName(value);
                if(!environment.getProperties().get(envVar).getType().equals(type)) {
                    throw new ValidationException("'" + property + "' from '"+entity+"' is of type '"+type+"' but '"+envVar+"' is of type '"+environment.getProperties().get(envVar).getType()+"'");
                }
            }
            else if(func.equals(Function.RANDOM)) {
                if(!type.equals(PropertyType.DECIMAL)) {
                    throw new ValidationException("'" + property+"' from '"+entity+"' is of type '"+type+"' so cannot receive random int");
                }
            }
        }
        else {
            try {
                isValueFromType(type, value);
            } catch (ValidationException e) {
                throw new ValidationException("'" + property+"' from '"+entity+ " " + e.getMessage());
            }
        }
    }

    public static void isValidByRange(String value, double from, double to) throws ValidationException {
        if(!isNumeric(value)) throw new ValidationException("'" + value + "' is not a number");
        Double number = Double.parseDouble(value);
        if(number > to || number < from) throw new ValidationException("'" + value + "' is not in the range: " + from + "-" + to);
    }

    public static void isValueFromType(String type, String value) throws ValidationException {
        if(type.equals(PropertyType.DECIMAL) || type.equals(PropertyType.FLOAT)) {
            if(!isNumeric(value)) throw new ValidationException("'" + value + "' is not of type '" + type + "'");
        }
        else if(type.equals(PropertyType.BOOLEAN)) {
            if(!isBoolean(value)) throw new ValidationException("'" + value + "' is not of type '" + type + "'");
        }
    }

    // TODO: test the function, the current file not contain a calculation
    public static void calculationValidation(Environment environment, Map<String, Entity> entities, String entity, String resultProp, String arg1, String arg2) throws ValidationException {
        String type = entities.get(entity).getProperties().get(resultProp).getType();
        String func1 = Function.whichFunction(arg1);
        String func2 = Function.whichFunction(arg2);

        if(!type.equals(PropertyType.DECIMAL) && !type.equals(PropertyType.FLOAT)) {
            throw new ValidationException("'" + resultProp + "' of '" + entity + "' is of type '" + type + "' and needs to be decimal/float for calculation");
        }
        if(!arg1.equals(resultProp)) {
            if(func1 != null) {
                if(func1.equals(Function.ENVIRONMENT)) {
                    String envVar = Function.getEnvVarName(arg1);
                    if(!environment.getProperties().get(envVar).getType().equals(PropertyType.FLOAT) && !environment.getProperties().get(envVar).getType().equals(PropertyType.DECIMAL)) {
                        throw new ValidationException("'"+envVar+"' is of type '"+environment.getProperties().get(envVar).getType()+"' and needs to be decimal/float for calculation");
                    }
                }
            }
            else {
                if(!isNumeric(arg1)) throw new ValidationException("'" + arg1 + "' needs to be decimal/float for calculation");
            }
        }
        if(!arg2.equals(resultProp)) {
            if(func2 != null) {
                if(func2.equals(Function.ENVIRONMENT)) {
                    String envVar = Function.getEnvVarName(arg2);
                    if(!environment.getProperties().get(envVar).getType().equals(PropertyType.FLOAT) && !environment.getProperties().get(envVar).getType().equals(PropertyType.DECIMAL)) {
                        throw new ValidationException("'"+envVar+"' is of type '"+environment.getProperties().get(envVar).getType()+"' and needs to be decimal/float for calculation");
                    }
                }
            }
            else {
                if(!isNumeric(arg2)) throw new ValidationException("'" + arg2 + "' needs to be decimal/float for calculation");
            }
        }
    }

    public static boolean isNumeric(String str) {
        // TODO: check if type is decimal and value is float
        try {
            Double.parseDouble(str); // Use Integer.parseInt() for integers
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBoolean(String str) {
        return (str.equals("true") || str.equals("false"));
    }

    public static void isEntityPropertyExists(Map<String, Entity> entities, String entity, String property) throws ValidationException {
        if(!entities.containsKey(entity)) throw new ValidationException("no such entity '" + entity + "'");
        Entity e = entities.get(entity);
        if(!e.getProperties().containsKey(property)) throw new ValidationException("no such property '" + property + "'");
    }

    public static void isEntityExists(Map<String, Entity> entities, String entity) throws ValidationException {
        if(!entities.containsKey(entity)) throw new ValidationException("no such entity '" + entity + "'");
    }

    public static void isPropertyValidNumber(Map<String, Entity> entities, String entity, String property) throws ValidationException {
        if(!entities.get(entity).getProperties().get(property).getType().equals(PropertyType.DECIMAL) && !entities.get(entity).getProperties().get(property).getType().equals(PropertyType.FLOAT))
            throw new ValidationException("'" + property+"' from '"+entity+ "' should be decimal/float for this action");
    }

}
