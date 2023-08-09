package com.predict.engine.utils.func;

public class Convert {
    public static Double stringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Can't convert '" + value +"' to double");
            return null;
        }
    }

    public static Integer stringToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Can't convert '" + value +"' to double");
            return null;
        }
    }

    public static Boolean stringToBoolean(String value) {
        return (value.equals("true") || value.equals("false") ? (value.equals("true")) : null);
    }
}
