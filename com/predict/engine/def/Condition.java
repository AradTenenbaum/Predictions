package com.predict.engine.def;

public class Condition extends Action {
    protected Action thenAction;
    protected Action elseAction;

    public Condition(String type, Entity entity, Property property, int by, Action thenAction, Action elseAction) {
        super(type, entity, property, by);
        this.thenAction = thenAction;
        this.elseAction = elseAction;
    }

    public Condition(String type, Entity entity, Property property, Action thenAction, Action elseAction) {
        super(type, entity, property);
        this.thenAction = thenAction;
        this.elseAction = elseAction;
    }
}
