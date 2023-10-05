package def;

import generic.objects.Range;
import utils.func.Convert;
import utils.func.RandomGenerator;

import java.io.Serializable;

public class Property implements Serializable {
    private String name;
    private Range range;
    private Boolean isRandom;
    private String type;
    private String init;

    public Property(String name, Range range, Boolean isRandom, String init, String type) {
        this.name = name;
        this.range = range;
        this.isRandom = isRandom;
        this.init = init;
        this.type = type;
    }



    public Property(String name, Range range, String type) {
        this.name = name;
        this.range = range;
        this.type = type;
    }

    public Property(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public void setRandom(Boolean random) {
        isRandom = random;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Range getRange() {
        return range;
    }

    public Boolean getRandom() {
        return isRandom;
    }

    public Object generateValue() {
        if(type.equals(PropertyType.DECIMAL) || type.equals(PropertyType.FLOAT)) {
            if(init != null && !isRandom) {
                if(type.equals(PropertyType.DECIMAL)) return Convert.stringToInteger(init);
                else if(type.equals(PropertyType.FLOAT)) return Convert.stringToDouble(init);
            } else {
                if(isRandom && range != null) {
                    if(type.equals(PropertyType.DECIMAL)) return RandomGenerator.getInt(range);
                    else if(type.equals(PropertyType.FLOAT)) return RandomGenerator.getDouble(range);
                }
            }
        } else if (type.equals(PropertyType.BOOLEAN)) {
            if(init != null && !isRandom) {
                return Convert.stringToBoolean(init);
            } else {
                if(isRandom) {
                    return RandomGenerator.getBoolean();
                }
            }
        } else if (type.equals(PropertyType.STRING)) {
            if(init != null && !isRandom) {
                return init;
            } else {
                if(isRandom) {
                    return RandomGenerator.getString();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", range=" + range +
                ", isRandom=" + isRandom +
                ", init=" + init +
                '}';
    }
}
