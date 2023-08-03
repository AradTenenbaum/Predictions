package com.predict.engine.def;

public class MultipleCondition extends Condition {
    private Condition c1;
    private Condition c2;
    private String logical;

    public MultipleCondition(String type, Entity entity, Property property, Action thenAction, Action elseAction, Condition c1, Condition c2, String logical) {
        super(type, entity, property, thenAction, elseAction);
        this.c1 = c1;
        this.c2 = c2;
        this.logical = logical;
    }
}
