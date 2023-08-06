package com.predict.engine.def.action;

public class Action {
    protected String type;
    protected String entity;
    protected String property;
    private String by;

    public Action(String type, String entity, String property, String by) {
        this.type = type;
        this.entity = entity;
        this.property = property;
        this.by = by;
    }

    public Action(String type, String entity, String property) {
        this.type = type;
        this.entity = entity;
        this.property = property;
    }

    public Action(String type, String entity) {
        this.type = type;
        this.entity = entity;
    }

    public Action() {
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Action{" +
                "type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                ", by=" + by +
                '}';
    }
}
