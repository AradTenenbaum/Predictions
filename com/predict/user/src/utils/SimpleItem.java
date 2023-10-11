package utils;

import javafx.beans.property.SimpleStringProperty;

public class SimpleItem {
    private SimpleStringProperty first;
    private SimpleStringProperty second;

    public SimpleItem(String first, String second) {
        this.first = new SimpleStringProperty(first);
        this.second = new SimpleStringProperty(second);
    }

    public SimpleStringProperty getFirst() {
        return first;
    }

    public SimpleStringProperty getSecond() {
        return second;
    }

    public void setFirst(String first) {
        this.first.set(first);
    }

    public void setSecond(String second) {
        this.second.set(second);
    }
}
