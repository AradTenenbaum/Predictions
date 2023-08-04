package com.predict.engine.def;

public class Calculation extends Action {
    public static enum TYPES {
        NO_TYPE, MULT, DIV
    }
    private String resultProp;
    private TYPES calcType;
    private String arg1;
    private String arg2;

    public Calculation(String entity, String resultProp, TYPES calcType, String arg1, String arg2) {
        super(ActionType.CALCULATION, entity);
        this.resultProp = resultProp;
        this.calcType = calcType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        return "Calculation{" +
                "resultProp='" + resultProp + '\'' +
                ", calcType=" + calcType +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
