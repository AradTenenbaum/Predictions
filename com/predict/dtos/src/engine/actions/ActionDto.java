package engine.actions;

import java.util.HashMap;
import java.util.Map;

public class ActionDto {
    protected String type;
    protected String entity;
    protected String secondaryEntity;

    public ActionDto(String type, String entity, String secondaryEntity) {
        this.type = type;
        this.entity = entity;
        this.secondaryEntity = secondaryEntity;
    }

    public Map<String, String> getParams() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Type", type);
        parameters.put("Entity", entity);
        if(secondaryEntity != null) parameters.put("Secondary entity", secondaryEntity);
        return parameters;
    }

    public String getType() {
        return type;
    }

    public String getEntity() {
        return entity;
    }

    public String getSecondaryEntity() {
        return secondaryEntity;
    }
}
