package levels.elements;

import processing.core.PImage;

import static main.Run.P;

public class Ballista extends Defence {
    public static final PImage[][] images = loadOrientationImages(P.getString("ballista images"));
    private static final float ATTACK_DAMAGE = P.getFloat("ballista attack damage");
    private static final float TOTAL_ATTACK_TIME = P.getFloat("ballista total attack time");
    private static final float HEALTH = P.getFloat("ballista health");
    private static final int MORAL_CHANGE = P.getInt("ballista moral change");

    @Override
    public PImage[] getImages(int direction) {
        return images[direction];
    }

    @Override
    public int getMoralChange() {
        return MORAL_CHANGE;
    }

    public Ballista(Float x, Float y) {
        super(x, y, HEALTH, ATTACK_DAMAGE, TOTAL_ATTACK_TIME);
    }
}
