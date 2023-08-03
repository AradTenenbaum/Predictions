package com.predict.engine.def;

public class SingleCondition<T> extends Condition {
    private String operator;
    private T value;

    public SingleCondition(String type, Entity entity, Property property, Action thenAction, Action elseAction, String operator, T value) {
        super(type, entity, property, thenAction, elseAction);
        this.operator = operator;
        this.value = value;
    }
}
