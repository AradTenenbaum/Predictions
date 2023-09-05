package ins;

import def.Property;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
    private String type;
    private Object value;
    private int lastChangedTick = 0;
    private boolean isRandom;

    public PropertyInstance(String type, Object value) {
        this.type = type;
        this.value = value;
        this.isRandom = false;
    }

    public PropertyInstance(String type, Object value, int lastChangedTick) {
        this.type = type;
        this.value = value;
        this.lastChangedTick = lastChangedTick;
        this.isRandom = false;
    }

    public PropertyInstance(String type) {
        this.type = type;
        this.value = 0;
        this.lastChangedTick = 0;
        this.isRandom = true;
    }

    public PropertyInstance(PropertyInstance other) {
        this.type = other.type;
        this.value = other.value;
        this.lastChangedTick = other.lastChangedTick;
        this.isRandom = other.isRandom;
    }

    public void setValue(Object value) {
        this.value = value;
        this.isRandom = false;
    }

    public boolean isRandom() {
        return isRandom;
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
