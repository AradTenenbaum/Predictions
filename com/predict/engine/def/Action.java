package com.predict.engine.def;

public class Action {
    protected String type;
    protected Entity entity;
    protected Property property;
    private int by;

    public Action(String type, Entity entity, Property property, int by) {
        this.type = type;
        this.entity = entity;
        this.property = property;
        this.by = by;
    }

    public Action(String type, Entity entity, Property property) {
        this.type = type;
        this.entity = entity;
        this.property = property;
    }
}
