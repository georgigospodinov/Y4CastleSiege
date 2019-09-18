package units;

import main.Run;
import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;

public class Archer extends Soldier {
    public static final int MORAL_CHANGE = P.getInt("archer spared moral change");
    public static final PImage SELECTOR_IMAGE = sm.loadImageProp("archer selector image");
    public static final PImage PICKED_UP_IMAGE = SELECTOR_IMAGE.copy();

    public static final PImage[] WALK;
    public static final PImage[] ATTACK;

    private static final float ATTACK_DAMAGE = P.getFloat("archer attack damage");
    private static final float TOTAL_ATTACK_TIME = P.getFloat("archer total attack time");
    private static final float SPEED = P.getFloat("archer movement speed");
    private static final float ATTACK_RANGE_FACTOR = P.getFloat("archer attack range factor");
    public static final float HEALTH = P.getFloat("archer health");

    static {
        WALK = Run.loadImages(P.getString("archer walk north animation"));
        ATTACK = Run.loadImages(P.getString("archer attack north animation"));
        onImplementingClassInit(SELECTOR_IMAGE, PICKED_UP_IMAGE, WALK, ATTACK);
    }

    @Override
    public PImage[] getWalkImages() {
        return WALK;
    }

    @Override
    public PImage[] getAttackImages() {
        return ATTACK;
    }

    public Archer(Float x, Float y) {
        super(x, y, ATTACK_DAMAGE, TOTAL_ATTACK_TIME, SPEED, ATTACK_RANGE_FACTOR);
    }

}
