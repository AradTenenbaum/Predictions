package com.predict.engine.def;

public class Property {
    private String name;
    private Range range;
    private Boolean isRandom;
    private String type;
    private String init;

    public Property(String name, Range range, Boolean isRandom, String init, String type) {
        this.name = name;
        this.range = range;
        this.isRandom = isRandom;
        this.init = init;
        this.type = type;
    }



    public Property(String name, Range range, String type) {
        this.name = name;
        this.range = range;
        this.type = type;
    }

    public Property(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public void setRandom(Boolean random) {
        isRandom = random;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Range getRange() {
        return range;
    }

    public Boolean getRandom() {
        return isRandom;
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
