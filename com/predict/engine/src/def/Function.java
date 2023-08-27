package def;

import ins.environment.EnvironmentInstance;
import utils.func.Convert;
import utils.func.RandomGenerator;
import utils.object.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    public static String ENVIRONMENT =  "environment";
    public static String RANDOM =  "random";
    public static String TICKS = "ticks";
    private static List<String> funcNames = new ArrayList<>(Arrays.asList(ENVIRONMENT, RANDOM, TICKS));

    public static String whichFunction(String value) {
        for(String func : funcNames) {
            if(value.startsWith(func)) return func;
        }
        return null;
    }

    public static String getFunctionContent(String value) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(value);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String getFuncInput(String value, String type, EnvironmentInstance env) {
        String val = value;

        String func = Function.whichFunction(val);
        if(func != null) {
            String funcArg = Function.getFunctionContent(val);
            if(func.equals(Function.RANDOM)) val = "" + RandomGenerator.getRandom(type, new Range(0, Convert.stringToDouble(funcArg)));
            else if(func.equals(Function.ENVIRONMENT)) val = (String) env.getProperty(funcArg).getValue();
            // TODO: return the new functions inputs
        }
        return val;
    }
}
