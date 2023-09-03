package data.dto.actions.condition;

import data.dto.actions.ActionDto;
import def.Property;

import java.util.Map;

public class ConditionDto extends ActionDto {
    private int thenActionsAmount;
    private int elseActionAmount;

    public ConditionDto(String type, String entity, String secondaryEntity, int thenActionsAmount, int elseActionAmount) {
        super(type, entity, secondaryEntity);
        this.thenActionsAmount = thenActionsAmount;
        this.elseActionAmount = elseActionAmount;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Then actions amount", String.valueOf(thenActionsAmount));
        parameters.put("Else actions amount", String.valueOf(elseActionAmount));
        return parameters;
    }
}
