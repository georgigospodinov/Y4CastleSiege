package general;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class Attribute {
    private final float BASE_VALUE;
    private final LinkedHashSet<Modifier> modifiers = new LinkedHashSet<>();

    public Attribute(float baseValue) {
        BASE_VALUE = baseValue;
    }

    public void addModifier(Modifier m) {
        modifiers.add(m);
    }

    public float getValue() {
        float value = BASE_VALUE;
        for (Iterator<Modifier> iterator = modifiers.iterator(); iterator.hasNext(); ) {
            Float f = iterator.next().apply(value);

            // Remove timed out modifiers.
            if (f == null)
                iterator.remove();

                // Stack successful modifications.
            else value = f;
        }

        return value;
    }

    @Override
    public String toString() {
        return "{BASE_VALUE=" + BASE_VALUE +
                ", modifiers=" + modifiers +
                "}";
    }
}
