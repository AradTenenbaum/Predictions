package engine;


import engine.actions.ActionDto;

public class ReplaceDto extends ActionDto {
    private String create;
    private String mode;

    public ReplaceDto(String type, String entity, String secondaryEntity, String create, String mode) {
        super(type, entity, secondaryEntity);
        this.create = create;
        this.mode = mode;
    }
}
