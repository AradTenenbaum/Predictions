package data.dto;

import generic.objects.Range;

import java.io.Serializable;

public class PropertyDto implements Serializable {
    private String name;
    private String type;
    private Range range;
    private Boolean isRandom;

    public PropertyDto(String name, String type, Range range, Boolean isRandom) {
        this.name = name;
        this.type = type;
        this.range = range;
        this.isRandom = isRandom;
    }

    public PropertyDto(String name, String type, Range range) {
        this.name = name;
        this.type = type;
        this.range = range;
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
}
