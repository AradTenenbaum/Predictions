package def;

import java.io.Serializable;

public class Termination implements Serializable {
    public static enum REASONS {
        TICKS, SECONDS, USER
    }
    private int ticks;
    private int seconds;
    private boolean isByUser;

    public Termination(int ticks, int seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
        this.isByUser = false;
    }

    public Termination(boolean isByUser) {
        this.ticks = -1;
        this.seconds = -1;
        this.isByUser = isByUser;
    }

    public int getTicks() {
        return ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isByUser() {
        return isByUser;
    }

    @Override
    public String toString() {
        return "Termination{" +
                "ticks=" + ticks +
                ", seconds=" + seconds +
                '}';
    }
}
