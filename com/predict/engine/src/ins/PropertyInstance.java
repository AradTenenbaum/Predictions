package ins;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
    private String type;
    private Object value;
    private int lastChangedTick = 0;

    public PropertyInstance(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public PropertyInstance(String type, Object value, int lastChangedTick) {
        this.type = type;
        this.value = value;
        this.lastChangedTick = lastChangedTick;
    }

    public int getLastChangedTick() {
        return lastChangedTick;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PropertyInstance{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
