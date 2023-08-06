package com.predict.engine.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
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
}
