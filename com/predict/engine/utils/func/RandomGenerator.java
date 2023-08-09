package com.predict.engine.utils.func;

import com.predict.engine.def.PropertyType;
import com.predict.engine.utils.object.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomGenerator {
    private static String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789().-_,?!";
    public static String getString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static Integer getInt(Range range) {
        Random random = new Random();
        int max = (int)range.getTo();
        int min = (int)range.getFrom();
        return random.nextInt(max - min + 1) + min;
    }

    public static Double getDouble(Range range) {
        Random random = new Random();
        Double max = range.getTo();
        Double min = range.getFrom();
        return min + (max - min) * random.nextDouble();
    }

    public static Boolean getBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static String getRandom(String type, Range range) {
        if (type.equals(PropertyType.DECIMAL)) {
            return "" + getInt(range);
        } else if (type.equals(PropertyType.FLOAT)) {
            return "" + getDouble(range);
        } else if (type.equals(PropertyType.BOOLEAN)) {
            return "" + getBoolean();
        } else if(type.equals(PropertyType.STRING)) {
            return getString(10);
        }
        return null;
    }
}
