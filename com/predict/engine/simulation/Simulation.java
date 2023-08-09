package com.predict.engine.simulation;

import com.predict.engine.ins.EntityInstance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Simulation {
    private static int idGenerator = 1;
    private int id;
    private Map<String, List<EntityInstance>> entities;
    private Date runDate;

    public Simulation(Map<String, List<EntityInstance>> entities, Date runDate) {
        this.id = idGenerator++;
        this.entities = entities;
        this.runDate = runDate;
    }

    public int getId() {
        return id;
    }

    public Map<String, List<EntityInstance>> getEntities() {
        return entities;
    }

    public String getFormattedRunDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        return formatter.format(runDate);
    }
}
