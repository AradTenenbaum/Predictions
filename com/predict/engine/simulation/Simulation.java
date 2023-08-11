package com.predict.engine.simulation;

import com.predict.engine.data.dto.WorldDto;
import com.predict.engine.def.Termination;
import com.predict.engine.ins.EntityInstance;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Simulation implements Serializable {
    private UUID id;
    private Map<String, List<EntityInstance>> entities;
    private Date runDate;
    private WorldDto worldDto;

    private Termination.REASONS terminationReason;

    public Simulation(Map<String, List<EntityInstance>> entities, Date runDate, WorldDto worldDto) {
        this.id = UUID.randomUUID();
        this.entities = entities;
        this.runDate = runDate;
        this.worldDto = worldDto;
    }

    public UUID getId() {
        return id;
    }

    public Map<String, List<EntityInstance>> getEntities() {
        return entities;
    }

    public Termination.REASONS getTerminationReason() {
        return terminationReason;
    }

    public WorldDto getWorldDto() {
        return worldDto;
    }

    public void setTerminationReason(Termination.REASONS terminationReason) {
        this.terminationReason = terminationReason;
    }

    public String getFormattedRunDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy | HH.mm.ss");
        return formatter.format(runDate);
    }
}
