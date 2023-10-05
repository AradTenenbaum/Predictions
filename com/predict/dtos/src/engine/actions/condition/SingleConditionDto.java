package engine.actions.condition;

import java.util.Map;

public class SingleConditionDto extends ConditionDto {
    private String operator;
    private String value;
    private String property;

    public SingleConditionDto(String type, String entity, String secondaryEntity, String property, int thenActionsAmount, int elseActionAmount, String operator, String value) {
        super(type, entity, secondaryEntity, thenActionsAmount, elseActionAmount);
        this.operator = operator;
        this.value = value;
        this.property = property;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Condition singularity", "single");
        parameters.put("Operator", operator);
        parameters.put("Property", property);
        parameters.put("Value", value);
        return parameters;
    }
}
