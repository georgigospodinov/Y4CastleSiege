package units;

import main.Run;
import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;

public class Zombie extends Soldier {
    public static final PImage[] WALK;
    public static final PImage[] ATTACK;
    public static final PImage[] RISE;
    private static final int RISE_DURATION = P.getAnyInt("zombie rise duration");

    private static final float ATTACK_DAMAGE = P.getFloat("zombie attack damage");
    private static final float TOTAL_ATTACK_TIME = P.getFloat("zombie total attack time");
    private static final float SPEED = P.getFloat("zombie movement speed");
    private static final float ATTACK_RANGE_FACTOR = P.getFloat("zombie attack range factor");
    public static final float HEALTH = P.getFloat("zombie health");

    static {
        WALK = Run.loadImages(P.getString("zombie walk north animation"));
        ATTACK = Run.loadImages(P.getString("zombie attack north animation"));
        RISE = Run.loadImages(P.getString("zombie rise animation"));
        if (WALK != null)
            for (PImage w : WALK)
                w.resize((int) DEFAULT_WIDTH, (int) DEFAULT_HEIGHT);
        if (ATTACK != null)
            for (PImage a : ATTACK)
                a.resize((int) DEFAULT_WIDTH, (int) DEFAULT_HEIGHT);
        if (RISE != null)
            for (PImage r : RISE)
                r.resize((int) DEFAULT_WIDTH, (int) DEFAULT_HEIGHT);
    }

    @Override
    public PImage[] getWalkImages() {
        return WALK;
    }

    @Override
    public PImage[] getAttackImages() {
        return ATTACK;
    }

    private boolean risen = false;
    private int riseStart;

    public Zombie(Float x, Float y) {
        super(x, y, ATTACK_DAMAGE, TOTAL_ATTACK_TIME, SPEED, ATTACK_RANGE_FACTOR);
        riseStart = sm.millis();
    }

    @Override
    public void move() {
        if (risen)
            super.move();
    }

    @Override
    public void render() {
        if (risen) {
            super.render();
            return;
        }
        int elapsed = sm.millis() - riseStart;
        int index;
        if (elapsed >= RISE_DURATION) {
            index = RISE.length - 1;
            risen = true;
        }
        else index = RISE.length * elapsed / RISE_DURATION;
        sm.image(RISE[index], position.x, position.y);
    }
}
