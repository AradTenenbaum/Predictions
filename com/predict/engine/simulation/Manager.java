package com.predict.engine.simulation;

import com.predict.engine.data.dto.WorldDto;
import com.predict.engine.def.World;

public class Manager {
    private World currentWorld;
    private WorldDto sharedWorld;
    private Boolean isValidWorld;

    public Manager() {
        this.isValidWorld = false;
    }

    public void setCurrentWorld(World currentWorld) {
        this.currentWorld = currentWorld;
    }

    public void setSharedWorld(WorldDto sharedWorld) {
        this.sharedWorld = sharedWorld;
    }

    public void setValidWorld(Boolean validWorld) {
        isValidWorld = validWorld;
    }
}
