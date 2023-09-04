package data.validation;

import def.*;
import ins.PropertyInstance;
import utils.exception.ValidationException;

import java.util.Map;

public class Validation {

    public static void isTypeValid(Environment environment, Map<String, Entity> entities, String entity, String property, String value) throws ValidationException {
        String propertyFunc = Function.whichFunction(property);
        String fixedProperty = property;
        String type = "";
        if(propertyFunc != null) {
            if(propertyFunc.equals(Function.TICKS)) {
                fixedProperty = Function.getFunctionContent(property);
                type = PropertyType.DECIMAL;
            }
        }
        else {
            type = entities.get(entity).getProperties().get(fixedProperty).getType();
        }
        String func = Function.whichFunction(value);
        if(func != null) {
            isValidFunction(entities, func, environment, value, type, property, entity);
        }
        else {
            try {
                isValueFromType(type, value);
            } catch (ValidationException e) {
                throw new ValidationException("'" + property+"' from '"+entity+ " " + e.getMessage());
            }
        }
    }

    public static  void isValidFunction(Map<String, Entity> entities, String func, Environment environment, String value, String type, String property, String entity) throws ValidationException {
        if(func.equals(Function.ENVIRONMENT)) {
            String envVar = Function.getFunctionContent(value);
            if(!environment.getProperties().get(envVar).getType().equals(type)) {
                if(!(PropertyType.isDecimal(environment.getProperties().get(envVar).getType()) && PropertyType.isFloat(type))) {
                    throw new ValidationException("'" + property + "' from '"+entity+"' is of type '"+type+"' but '"+envVar+"' is of type '"+environment.getProperties().get(envVar).getType()+"'");
                }
            }
        }
        else if(func.equals(Function.RANDOM)) {
            if(!type.equals(PropertyType.DECIMAL) && !PropertyType.isFloat(type)) {
                throw new ValidationException("'" + property+"' from '"+entity+"' is of type '"+type+"' so cannot receive random int");
            }
        } else if (func.equals(Function.EVALUATE)) {
            String funcArgs[] = Function.getFunctionContent(value).split("\\.");
            if(funcArgs.length != 2) throw new ValidationException("'" + value + "' is not a valid value");
            if(!type.equals(entities.get(funcArgs[0]).getProperties().get(funcArgs[1]).getType())) {
                throw new ValidationException("'" + value + "' is not a valid type for " + type);
            }
        } else if (func.equals(Function.PERCENT)) {
            if(!type.equals(PropertyType.DECIMAL) && !type.equals(PropertyType.FLOAT)) throw new ValidationException("cannot put percent in " + type);
            String funcArgs[] = Function.getFunctionContent(value).split(",");
            if(funcArgs.length != 2) throw new ValidationException("'" + value + "' is not a valid value");
            // TODO: handle a case there is a function inside it
            String insideFunc1 = Function.whichFunction(funcArgs[0]);
            if(insideFunc1 != null) isValidFunction(entities, insideFunc1, environment, funcArgs[0], PropertyType.FLOAT, property, entity);
            else if(!isNumeric(funcArgs[0])) {
                throw new ValidationException(value + " contains not numeric elements");
            }
            String insideFunc2 = Function.whichFunction(funcArgs[1]);
            if(insideFunc2 != null)  isValidFunction(entities, insideFunc2, environment, funcArgs[1], PropertyType.FLOAT, property, entity);
            else if(!isNumeric(funcArgs[1])) {
                throw new ValidationException(value + " contains not numeric elements");
            }
        }
    }

    public static void isValidByRange(String value, double from, double to) throws ValidationException {
        if(!isNumeric(value)) throw new ValidationException("'" + value + "' is not a number");
        Double number = Double.parseDouble(value);
        if(number > to || number < from) throw new ValidationException("'" + value + "' is not in the range: " + from + "-" + to);
    }

    public static void ifEnvIsValid(Environment env, String value) throws ValidationException {
        String func = Function.whichFunction(value);
        if(func != null) {
            if(func.equals(Function.ENVIRONMENT)) {
                String arg = Function.getFunctionContent(value);
                if(!env.getProperties().containsKey(arg)) {
                    throw new ValidationException("No such env variable " + arg);
                }
            }
        }
    }

    public static void isValueFromType(String type, String value) throws ValidationException {
        if(type.equals(PropertyType.DECIMAL)) {
            if(!isInteger(value)) throw new ValidationException("'" + value + "' is not of type '" + type + "'");
        } else if(type.equals(PropertyType.FLOAT)) {
            if(!isDouble(value)) throw new ValidationException("'" + value + "' is not of type '" + type + "'");
        } else if(type.equals(PropertyType.BOOLEAN)) {
            if(!isBoolean(value)) throw new ValidationException("'" + value + "' is not of type '" + type + "'");
        } else if (type.equals(PropertyType.STRING)) {
            if(isNumeric(value)) throw new ValidationException("'" + value + "' of type string cannot be a number");
        } else {
            throw new ValidationException("'" + type + "' is not a valid type");
        }
    }

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
                    String envVar = Function.getFunctionContent(arg1);
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
                    String envVar = Function.getFunctionContent(arg2);
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

    public static void stringNoSpaceValidation(String s) throws ValidationException {
        if(s.contains(" ")) throw new ValidationException(s + " contains spaces and it is not allowed");
    }

    public static void rangeValid(double from, double to) throws ValidationException {
        if(from > to) throw new ValidationException("Range is not valid");
    }

    public static boolean isNumeric(String str) {
        return isDouble(str);
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static boolean isBoolean(String str) {
        return (str.equals("true") || str.equals("false"));
    }

    public static void isEntityPropertyExists(Map<String, Entity> entities, String entity, String property) throws ValidationException {
        String func = Function.whichFunction(property);
        String propertyFixedValue = property;
        if(func != null) {
            String content = Function.getFunctionContent(propertyFixedValue);
            if(func.equals(Function.TICKS)) {
                String propAndEnt[] = content.split("\\.");
                propertyFixedValue = propAndEnt[1];
            }
        }
        if(!entities.containsKey(entity)) throw new ValidationException("no such entity '" + entity + "'");
        Entity e = entities.get(entity);
        if(!e.getProperties().containsKey(propertyFixedValue)) throw new ValidationException("no such property '" + propertyFixedValue + "'");
    }

    public static void isEntityExists(Map<String, Entity> entities, String entity) throws ValidationException {
        if(!entities.containsKey(entity)) throw new ValidationException("no such entity '" + entity + "'");
    }

    public static void isPropertyValidNumber(Map<String, Entity> entities, String entity, String property) throws ValidationException {
        if(!entities.get(entity).getProperties().get(property).getType().equals(PropertyType.DECIMAL) && !entities.get(entity).getProperties().get(property).getType().equals(PropertyType.FLOAT))
            throw new ValidationException("'" + property+"' from '"+entity+ "' should be decimal/float for this action");
    }

    public static void checkPropValid(Property prop, String value) throws ValidationException {
        isValueFromType(prop.getType(), value);
        if(prop.getRange() != null) {
            isValidByRange(value, prop.getRange().getFrom(), prop.getRange().getTo());
        }
    }


}
