package def.action;

import def.World;
import ins.EntityInstance;
import ins.PropertyInstance;
import ins.environment.EnvironmentInstance;
import simulation.InvokeKit;
import utils.exception.SimulationException;
import utils.object.Grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Replace extends Action {
    private String createEntity;
    private String mode;
    private final String SCRATCH = "scratch";
    private final String DERIVED = "derived";

    public Replace(String entity, String createEntity, String mode) {
        super(ActionType.REPLACE, entity);
        this.createEntity = createEntity;
        this.mode = mode;
    }

    @Override
    public void invoke(InvokeKit invokeKit) throws SimulationException {
        EntityInstance entityInstance = invokeKit.getEntityInstance();
        World world = invokeKit.getWorld();
        Grid grid = invokeKit.getGrid();
        List<EntityInstance> toCreate = invokeKit.getToCreate();

        entityInstance.kill();
        Map<String, PropertyInstance> properties = new HashMap<>();
        if(mode.equals(SCRATCH)) {
            world.getEntities().get(createEntity).getProperties().forEach((propertyName, property) -> {
                properties.put(propertyName, new PropertyInstance(property.getType(), property.generateValue()));
            });
        } else if(mode.equals(DERIVED)) {
            world.getEntities().get(createEntity).getProperties().forEach((propertyName, property) -> {
                if(entityInstance.hasProperty(propertyName) && entityInstance.getPropertyType(propertyName).equals(world.getEntities().get(createEntity).getProperties().get(propertyName).getType())) {
                    properties.put(propertyName, entityInstance.getProperties().get(propertyName));
                }
                else {
                    properties.put(propertyName, new PropertyInstance(property.getType(), property.generateValue()));
                }
            });
        }
        EntityInstance newEntIns = new EntityInstance(createEntity, properties);
        newEntIns.setPosition(entityInstance.getPosition());
        // update the grid as well
        grid.setPos(entityInstance.getPosition(), newEntIns.getId());
        toCreate.add(newEntIns);
    }
}
