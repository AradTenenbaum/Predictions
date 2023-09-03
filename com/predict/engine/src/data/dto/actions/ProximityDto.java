package data.dto.actions;

import def.Property;

import java.util.Map;

public class ProximityDto extends ActionDto {
    private String targetEntity;
    private String envDepth;
    private int actionsAmount;

    public ProximityDto(String type, String entity, String secondaryEntity, String targetEntity, String envDepth, int actionsAmount) {
        super(type, entity, secondaryEntity);
        this.targetEntity = targetEntity;
        this.envDepth = envDepth;
        this.actionsAmount = actionsAmount;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Target entity", targetEntity);
        parameters.put("Environment depth", envDepth);
        parameters.put("Actions amount", String.valueOf(actionsAmount));
        return parameters;
    }
}
