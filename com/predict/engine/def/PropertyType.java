package com.predict.engine.def;

public class PropertyType {
    public static String DECIMAL = "decimal";
    public static String FLOAT = "float";
    public static String BOOLEAN = "boolean";
    public static String STRING = "string";

    public static Boolean isTypeNumber(String type) {
        return type.equals(DECIMAL) || type.equals(FLOAT);
    }
    public static Boolean isDecimal(String type) {
        return type.equals(DECIMAL);
    }

    public static Boolean isFloat(String type) {
        return type.equals(FLOAT);
    }

    public static Boolean isBoolean(String type) {
        return type.equals(BOOLEAN);
    }
}
