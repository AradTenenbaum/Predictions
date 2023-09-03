package def.action;

import def.action.condition.Condition;

import java.util.Optional;

public class SecondaryEntity {
    public static int ALL = 0;
    private String name;
    private int count;
    private Optional<Condition> condition;

    public SecondaryEntity(String name, int count, Condition condition) {
        this.name = name;
        this.count = count;
        this.condition = Optional.ofNullable(condition);
    }

    public boolean isAll() {
        return count == ALL;
    }

    public Optional<Condition> getCondition() {
        return condition;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
