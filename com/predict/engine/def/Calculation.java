package com.predict.engine.def;

public class Calculation extends Action {
    private Property resultProp;
    private String type;
    private int arg1;
    private int arg2;

    public Calculation(String type, Entity entity, Property property, Property resultProp, String type1, int arg1, int arg2) {
        super(type, entity, property);
        this.resultProp = resultProp;
        this.type = type1;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
