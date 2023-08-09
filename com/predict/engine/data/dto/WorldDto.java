package com.predict.engine.data.dto;

import com.predict.engine.def.Entity;
import com.predict.engine.def.Property;
import com.predict.engine.def.Rule;
import com.predict.engine.def.World;
import com.predict.engine.def.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldDto {
    private EnvironmentDto environment;
    private List<EntityDto> entities;
    private List<RuleDto> rules;
    private TerminationDto termination;

    public WorldDto(World world) {
        this.entities = new ArrayList<>();
        this.rules = new ArrayList<>();
        List<PropertyDto> envProperties = new ArrayList<>();

        world.getEnvironment().getProperties().forEach((s, property) -> {
            envProperties.add(new PropertyDto(s, property.getType(), property.getRange()));
        });

        this.environment = new EnvironmentDto(envProperties);

        for(Map.Entry<String, Entity> e : world.getEntities().entrySet()) {
            List<PropertyDto> properties = new ArrayList<>();
            for(Map.Entry<String, Property> p : e.getValue().getProperties().entrySet()) {
                properties.add(new PropertyDto(p.getKey(), p.getValue().getType(), p.getValue().getRange(), p.getValue().getRandom()));
            }
            this.entities.add(new EntityDto(e.getKey(), e.getValue().getPopulation(), properties));
        }

        for(Rule r : world.getRules()) {
            List<String> actions = new ArrayList<>();
            r.getActions().forEach(a -> actions.add(a.getType()));
            this.rules.add(new RuleDto(r.getName(), r.getProbability(), r.getTicks(), actions));
        }

        this.termination = new TerminationDto(world.getTermination().getSeconds(), world.getTermination().getTicks());
    }

    public List<EntityDto> getEntities() {
        return entities;
    }

    public List<RuleDto> getRules() {
        return rules;
    }

    public TerminationDto getTermination() {
        return termination;
    }

    public EnvironmentDto getEnvironment() {
        return environment;
    }
}
