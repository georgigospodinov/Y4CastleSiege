package general;

import java.util.Objects;

import static main.ScreenManager.sm;

public class Modifier {
    public static final int PERMANENT = -1;
    public final Class SOURCE;
    public final boolean FLAT;
    public final float MOD;
    public final int END_TIME;

    public Modifier(Class source, boolean flat, float mod, int duration) {
        SOURCE = source;
        FLAT = flat;
        MOD = mod;
        if (duration != PERMANENT)
            END_TIME = duration + sm.millis();
        else END_TIME = PERMANENT;
    }

    public Float apply(float f) {
        if (END_TIME != PERMANENT && END_TIME <= sm.millis()) return null;
        else return FLAT ? f + MOD : f * MOD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modifier)) return false;
        Modifier modifier = (Modifier) o;
        return Objects.equals(SOURCE, modifier.SOURCE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SOURCE);
    }

    @Override
    public String toString() {
        return "{FLAT=" + FLAT +
                ", MOD=" + MOD +
                "}";
    }
}
