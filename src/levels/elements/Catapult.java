package levels.elements;

import general.Field;
import processing.core.PImage;
import processing.core.PVector;
import units.Soldier;
import units.Unit;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;

public class Catapult extends Defence {
    public static final PImage[][] images = loadOrientationImages(P.getString("catapult images"));
    private static final float ATTACK_DAMAGE = P.getFloat("catapult attack damage");
    private static final float TOTAL_ATTACK_TIME = P.getFloat("catapult total attack time");
    private static final float HEALTH = P.getFloat("catapult health");
    private static final float RADIUS = P.getFloat("catapult radius") * sm.getWidth();
    private static final int MORAL_CHANGE = P.getInt("catapult moral change");

    @Override
    public PImage[] getImages(int direction) {
        return images[direction];
    }

    @Override
    public int getMoralChange() {
        return MORAL_CHANGE;
    }

    public Catapult(Float x, Float y) {
        super(x, y, HEALTH, ATTACK_DAMAGE, TOTAL_ATTACK_TIME);
    }

    @Override
    public void attack() {
        int time = sm.millis();
        if (time - lastAttack < totalAttackTime.getValue()) {
            return;
        }

        // Display hit area.
        PVector shellLanding = combatTarget.calcCenterPoint();
        sm.stroke(255, 0, 0);
        sm.strokeWeight(4);
        sm.noFill();
        sm.ellipseMode(CENTER);
        sm.ellipse(shellLanding.x, shellLanding.y, RADIUS * 2, RADIUS * 2);

        for (Unit unit : Field.units) {
            float dmg = 0;
            for (Soldier soldier : unit.soldiers) {
                if (soldier.position.dist(shellLanding) <= RADIUS) {
                    dmg++;
                }
            }
            if (dmg > 0) {
                unit.takeDamage(dmg);
            }
        }
        lastAttack = time;
    }
}
