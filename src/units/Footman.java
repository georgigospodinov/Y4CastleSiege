package units;

import main.Run;
import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;

public class Footman extends Soldier {
    public static final int MORAL_CHANGE = P.getInt("footman spared moral change");
    public static final PImage SELECTOR_IMAGE = sm.loadImageProp("footman selector image");
    public static final PImage PICKED_UP_IMAGE = SELECTOR_IMAGE.copy();

    public static final PImage[] WALK;
    public static final PImage[] ATTACK;

    private static final float ATTACK_DAMAGE = P.getFloat("footman attack damage");
    private static final float TOTAL_ATTACK_TIME = P.getFloat("footman total attack time");
    private static final float SPEED = P.getFloat("footman movement speed");
    private static final float ATTACK_RANGE_FACTOR = P.getFloat("footman attack range factor");
    public static final float HEALTH = P.getFloat("footman health");

    static {
        WALK = Run.loadImages(P.getString("footman walk north animation"));
        ATTACK = Run.loadImages(P.getString("footman attack north animation"));
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

    public Footman(Float x, Float y) {
        super(x, y, ATTACK_DAMAGE, TOTAL_ATTACK_TIME, SPEED, ATTACK_RANGE_FACTOR);
    }
}
