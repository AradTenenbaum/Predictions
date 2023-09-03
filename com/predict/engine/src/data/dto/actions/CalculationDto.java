package data.dto.actions;

import def.Property;
import def.action.Calculation;

import java.util.Map;

public class CalculationDto extends ActionDto {
    private String resultProp;
    private Calculation.TYPES calcType;
    private String arg1;
    private String arg2;

    public CalculationDto(String type, String entity, String secondaryEntity, String resultProp, Calculation.TYPES calcType, String arg1, String arg2) {
        super(type, entity, secondaryEntity);
        this.resultProp = resultProp;
        this.calcType = calcType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Result Property", resultProp);
        parameters.put("Calculation type", calcType.toString());
        parameters.put("Arg1", arg1);
        parameters.put("Arg2", arg2);
        return parameters;
    }
}
