package def;

import ins.EntityInstance;
import ins.PropertyInstance;
import ins.environment.EnvironmentInstance;
import simulation.Context;
import utils.exception.SimulationException;
import utils.func.Convert;
import utils.func.RandomGenerator;
import utils.object.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    public final static String ENVIRONMENT =  "environment";
    public final static String RANDOM =  "random";
    public final static String TICKS = "ticks";
    public final static String EVALUATE = "evaluate";
    public final static String PERCENT = "percent";

    private final static List<String> funcNames = new ArrayList<>(Arrays.asList(ENVIRONMENT, RANDOM, TICKS, EVALUATE, PERCENT));

    public static String whichFunction(String value) {
        for(String func : funcNames) {
            if(value.startsWith(func)) return func;
        }
        return null;
    }

    public static String getFunctionContent(String value) {
        int startIndex = value.indexOf('(');
        int endIndex = value.lastIndexOf(')');

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return value.substring(startIndex + 1, endIndex);
        } else {
            return "";
        }
    }

    public static String getFuncInput(String value, String type, EnvironmentInstance env, Context context) throws SimulationException
    {
        String val = value;

        String func = Function.whichFunction(val);
        if(func != null) {
            String funcArg = Function.getFunctionContent(val);
            if(func.equals(Function.RANDOM)) val = "" + RandomGenerator.getRandom(type, new Range(0, Convert.stringToDouble(funcArg)));
            else if(func.equals(Function.ENVIRONMENT)) val = (String) env.getProperty(funcArg).getValue();
            else if (func.equals(Function.EVALUATE)) {
                if(context == null) {
                    throw new SimulationException("Out of context " + funcArg);
                }
                val = context.getSecondEntity().getPropertyValue(funcArg.split("\\.")[1]).toString();
            } else if (func.equals(Function.PERCENT)) {
                String args[] = funcArg.split(",");
                String val1 = getFuncInput(args[0], type, env, context);
                String val2 = getFuncInput(args[1], type, env, context);

                val = String.valueOf(Convert.stringToDouble(val1) * (Convert.stringToDouble(val2)/100));
            }
        }
        return val;
    }

    public static Object getPropertyIfFunction(String propertyName, EntityInstance entityInstance) {
        String func = whichFunction(propertyName);
        if(func != null) {
            if(func.equals(TICKS)) {
                String funcArg = Function.getFunctionContent(propertyName);
                String splitedValues[] = funcArg.split("\\.");
                return entityInstance.getProperties().get(splitedValues[1]).getLastChangedTick();
            }
        }
        return entityInstance.getPropertyValue(propertyName);
    }
}
