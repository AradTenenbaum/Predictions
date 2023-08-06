package com.predict.engine.def.action.condition;

import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.ActionType;

public class Condition extends Action {
    protected Action thenAction;
    protected Action elseAction;

    public static String SINGLE = "single";
    public static String MULTIPLE = "multiple";
    public static enum TYPE {
        OUTER, INNER
    }

    public Condition(String entity, String property, Action thenAction, Action elseAction) {
        super(ActionType.CONDITION, entity, property);
        this.thenAction = thenAction;
        this.elseAction = elseAction;
    }

    public Condition() {
        super();
    }

    @Override
    public String toString() {
        return "Condition{" +
                "thenAction=" + thenAction +
                ", elseAction=" + elseAction +
                '}';
    }
}
