package com.predict.engine.def;

import com.predict.engine.ins.EntityInstance;
import com.predict.engine.ins.environment.EnvironmentInstance;
import com.predict.engine.utils.exception.SimulationException;
import com.predict.engine.utils.func.Convert;
import com.predict.engine.utils.func.RandomGenerator;
import com.predict.engine.utils.object.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    // TODO: check valid conversions between values
    public static String ENVIRONMENT =  "environment";
    public static String RANDOM =  "random";

    private static List<String> funcNames = new ArrayList<>(Arrays.asList(ENVIRONMENT, RANDOM));

    public static String whichFunction(String value) {
        for(String func : funcNames) {
            if(value.startsWith(func)) return func;
        }
        return null;
    }

    public static String getEnvVarName(String value) {
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
            String funcArg = Function.getEnvVarName(val);
            if(func.equals(Function.RANDOM)) val = "" + RandomGenerator.getRandom(type, new Range(0, Convert.stringToDouble(funcArg)));
            else if(func.equals(Function.ENVIRONMENT)) val = (String) env.getProperty(funcArg).getValue();
        }
        return val;
    }
}
