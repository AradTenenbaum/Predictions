package components.pages.details.simulation.actions;

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
