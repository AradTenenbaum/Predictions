package data.dto.actions.condition;

import def.Property;

import java.util.Map;

public class MultipleConditionDto extends ConditionDto {
    private String logic;
    private int conditionsAmount;

    public MultipleConditionDto(String type, String entity, String secondaryEntity, int thenActionsAmount, int elseActionAmount, String logic, int conditionsAmount) {
        super(type, entity, secondaryEntity, thenActionsAmount, elseActionAmount);
        this.logic = logic;
        this.conditionsAmount = conditionsAmount;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Condition singularity", "multiple");
        parameters.put("Logic sign", logic);
        parameters.put("Conditions amount", String.valueOf(conditionsAmount));
        return parameters;
    }
}
