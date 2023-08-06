package com.predict.engine.def.action.condition;

import com.predict.engine.def.action.Action;
import com.predict.engine.def.action.condition.Condition;

public class SingleCondition extends Condition {
    private String operator;
    private String value;

    public SingleCondition(String entity, String property, Action thenAction, Action elseAction, String operator, String value) {
        super(entity, property, thenAction, elseAction);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SingleCondition{" +
                "operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                ", thenAction=" + thenAction +
                ", elseAction=" + elseAction +
                ", type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}
