package ins;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
    private String type;

    private Object value;

    public PropertyInstance(String type, Object value) {
        this.type = type;
        this.value = value;
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
