package com.predict.engine.def.action.condition;

import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.condition.Condition;

import java.util.List;

public class MultipleCondition extends Condition {
    private List<Condition> conditions;
    private String logical;

    public MultipleCondition(String entity, String property, Action thenAction, Action elseAction, List<Condition> conditions, String logical) {
        super(entity, property, thenAction, elseAction);
        this.conditions = conditions;
        this.logical = logical;
    }

    @Override
    public String toString() {
        return "MultipleCondition{" +
                "conditions=" + conditions +
                ", logical='" + logical + '\'' +
                ", thenAction=" + thenAction +
                ", elseAction=" + elseAction +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
