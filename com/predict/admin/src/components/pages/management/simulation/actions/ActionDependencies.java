package components.pages.management.simulation.actions;

import engine.actions.ActionDto;

import java.util.Map;

public class ActionDependencies {
    private Map<Object, Object> actionData;

    public ActionDependencies(Map<Object, Object> actionData) {
        this.actionData = actionData;
    }

    public Map<Object, Object> getActionData() {
        return actionData;
    }
}
