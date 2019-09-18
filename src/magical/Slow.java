package magical;

import general.Field;
import general.Modifier;
import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;

public class Slow extends Spell {

    public static final float RANGE = P.getFloat("slow range") * sm.getWidth();
    private static final float SLOW_FACTOR = P.getFloat("slow factor");
    private static final int DURATION = P.getInt("slow duration");

    public Slow(PImage castingImage) {
        super(castingImage);
    }

    @Override
    public void takeEffect() {
        sm.noFill();
        sm.strokeWeight(3);
        sm.stroke(170, 40, 0);
        sm.ellipseMode(CENTER);
        sm.ellipse(targetLocation.x, targetLocation.y, RANGE, RANGE);
        Field.level.defences.forEach(defence -> {
            double dist = defence.distTo(targetLocation);
            if (dist <= RANGE) {
                defence.totalAttackTime.addModifier(new Modifier(this.getClass(), false, SLOW_FACTOR, DURATION));
                defence.attackDamage.addModifier(new Modifier(this.getClass(), false, 1 / SLOW_FACTOR, DURATION));
            }
        });
    }
}
