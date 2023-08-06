package com.predict.engine.data.dto;

import com.predict.engine.def.Range;

public class PropertyDto {
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
}
