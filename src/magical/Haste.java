package magical;

import general.Field;
import general.Modifier;
import processing.core.PImage;
import units.Soldier;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;

public class Haste extends Spell {
    public static final float RANGE = P.getFloat("haste range") * sm.getWidth();
    private static final float AMOUNT = P.getFloat("haste amount");
    private static final int DURATION = P.getInt("haste duration");

    public Haste(PImage castingImage) {
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
                for (Soldier soldier : unit.soldiers) {
                    soldier.totalAttackTime.addModifier(new Modifier(this.getClass(), false, 1 / AMOUNT, DURATION));
                    soldier.speed.addModifier(new Modifier(this.getClass(), false, AMOUNT, DURATION));
                }
            }
        });
    }
}
