package data.dto;

import data.dto.actions.*;
import data.dto.actions.condition.MultipleConditionDto;
import data.dto.actions.condition.SingleConditionDto;
import def.Entity;
import def.Property;
import def.Rule;
import def.World;
import def.action.ActionType;
import def.action.Calculation;
import def.action.Proximity;
import def.action.Replace;
import def.action.condition.Condition;
import def.action.condition.MultipleCondition;
import def.action.condition.SingleCondition;
import utils.object.Grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldDto implements Serializable {
    private EnvironmentDto environment;
    private List<EntityDto> entities;
    private List<RuleDto> rules;
    private TerminationDto termination;
    private GridDto gridDto;

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
            List<ActionDto> actions = new ArrayList<>();
            r.getActions().forEach(a -> {
                String secEntity = (a.getSecondaryEntity().isPresent() ? a.getSecondaryEntity().get().getName() : null);
                if(a.getType().equals(ActionType.INCREASE) || a.getType().equals(ActionType.DECREASE)) {
                    actions.add(new AritmeticActionDto(a.getType(), a.getEntity(),
                            secEntity,
                            a.getProperty().getName(), a.getBy()));
                } else if(a.getType().equals(ActionType.KILL)) {
                    actions.add(new ActionDto(a.getType(), a.getEntity(), secEntity));
                } else if(a.getType().equals(ActionType.SET)) {
                    actions.add(new ActionPropDto(a.getType(), a.getEntity(), secEntity, a.getProperty().getName()));
                } else if (a.getType().equals(ActionType.PROXIMITY)) {
                    actions.add(new ProximityDto(a.getType(), a.getEntity(), secEntity,
                            ((Proximity)a).getTargetEntity(), ((Proximity)a).getEnvDepth(), ((Proximity)a).getActions().size()));
                } else if (a.getType().equals(ActionType.CALCULATION)) {
                    actions.add(new CalculationDto(a.getType(), a.getEntity(),secEntity,
                            ((Calculation)a).getResultProp().getName(), ((Calculation)a).getCalcType(), ((Calculation)a).getArg1(), ((Calculation)a).getArg2()));
                } else if (a.getType().equals(ActionType.CONDITION)) {
                    int thenAmount = (((Condition)a).getThenActions() != null) ? ((Condition)a).getThenActions().size() : 0;
                    int elseAmount = (((Condition)a).getElseActions() != null) ? ((Condition)a).getElseActions().size() : 0;
                    if(a instanceof SingleCondition) {
                        actions.add(new SingleConditionDto(a.getType(),
                                a.getEntity(), secEntity,
                                a.getProperty().getName(),
                                thenAmount, elseAmount,
                                ((SingleCondition)a).getOperator(), ((SingleCondition)a).getValue()));
                    } else if (a instanceof MultipleCondition) {
                        actions.add(new MultipleConditionDto(a.getType(),
                                a.getEntity(), secEntity,
                                thenAmount, elseAmount,
                                ((MultipleCondition)a).getLogical(), ((MultipleCondition)a).getConditions().size()));
                    }
                } else if (a.getType().equals(ActionType.REPLACE)) {
                    actions.add(new ReplaceDto(a.getType(), a.getEntity(), secEntity, ((Replace)a).getCreateEntity(), ((Replace)a).getMode()));
                }
            });
            this.rules.add(new RuleDto(r.getName(), r.getProbability(), r.getTicks(), actions));
        }

        this.gridDto = new GridDto(world.getGrid().getRows(), world.getGrid().getColumns());

        this.termination = new TerminationDto(world.getTermination().getSeconds(), world.getTermination().getTicks(), world.getTermination().isByUser());
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

    public GridDto getGridDto() {
        return gridDto;
    }
}
