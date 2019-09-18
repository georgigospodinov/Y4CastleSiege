package magical;

import general.Field;
import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;

public class Heal extends Spell {
    public static final float RANGE = P.getFloat("heal range") * sm.getWidth();
    private static final float AMOUNT = P.getFloat("heal amount");

    public Heal(PImage castingImage) {
        super(castingImage);
    }

    @Override
    public void takeEffect() {
        sm.noFill();
        sm.strokeWeight(3);
        sm.stroke(170, 40, 0);
        sm.ellipseMode(CENTER);
        sm.ellipse(targetLocation.x, targetLocation.y, RANGE, RANGE);
        Field.units.forEach(unit -> {
            double d = targetLocation.dist(unit.calcCenterPoint());
            if (d <= RANGE) {
                unit.takeHeal(AMOUNT);
            }
        });
    }
}
