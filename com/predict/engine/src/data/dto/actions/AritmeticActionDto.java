package data.dto.actions;

import def.Property;

import java.util.Map;

public class AritmeticActionDto extends ActionPropDto{
    private String by;

    public AritmeticActionDto(String type, String entity, String secondaryEntity, String property, String by) {
        super(type, entity, secondaryEntity, property);
        this.by = by;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> parameters = super.getParams();
        parameters.put("By", by);
        return parameters;
    }
}
