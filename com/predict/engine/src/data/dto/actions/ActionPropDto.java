package data.dto.actions;

import java.util.HashMap;
import java.util.Map;

public class ActionPropDto extends ActionDto {
    private String property;

    public ActionPropDto(String type, String entity, String secondaryEntity, String property) {
        super(type, entity, secondaryEntity);
        this.property = property;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("Property", property);
        return parameters;
    }
}
