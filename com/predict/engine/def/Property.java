package com.predict.engine.def;

public class Property<T> {
    private String name;
    private Range range;
    private Boolean isRandom;
    private T init;

    public Property(String name, Range range, Boolean isRandom, T init) {
        this.name = name;
        this.range = range;
        this.isRandom = isRandom;
        this.init = init;
    }

    public Property(String name, Range range) {
        this.name = name;
        this.range = range;
    }

    public Property(String name) {
        this.name = name;
    }

    public void setInit(T init) {
        this.init = init;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", range=" + range +
                ", isRandom=" + isRandom +
                ", init=" + init +
                '}';
    }
}
