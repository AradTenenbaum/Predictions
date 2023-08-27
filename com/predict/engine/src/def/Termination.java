package def;

import java.io.Serializable;

public class Termination implements Serializable {
    public static enum REASONS {
        TICKS, SECONDS
    }
    private int ticks;
    private int seconds;

    public Termination(int ticks, int seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return "Termination{" +
                "ticks=" + ticks +
                ", seconds=" + seconds +
                '}';
    }
}
