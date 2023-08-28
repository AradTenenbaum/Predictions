package simulation;

import ins.EntityInstance;

import java.util.ArrayList;

public class Context {
    EntityInstance secondEntity;

    public Context(EntityInstance secondEntity) {
        this.secondEntity = secondEntity;
    }

    public EntityInstance getSecondEntity() {
        return secondEntity;
    }
}
