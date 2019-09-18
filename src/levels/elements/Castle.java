package levels.elements;

import processing.core.PImage;

import static main.Run.P;
import static main.ScreenManager.sm;

public class Castle extends Defence {

    public static final PImage CASTLE_IMAGE = sm.loadImage("assets/images/defences/castle/castle.png");
    public static final float CASTLE_HEALTH_BAR_X = P.getFloat("castle health bar x") * sm.getWidth();
    public static final float CASTLE_HEALTH_BAR_Y = P.getFloat("castle health bar y") * sm.getHeight();
    public static final float CASTLE_HEALTH_BAR_WIDTH = P.getFloat("castle health bar width") * sm.getWidth();
    public static final float CASTLE_HEALTH_BAR_HEIGHT = P.getFloat("castle health bar height") * sm.getHeight();
    private static final float HEALTH = P.getFloat("castle health");

    public Castle() {
        super(0f, 0f, sm.getWidth(), CASTLE_IMAGE.height, HEALTH);
        collisionImage = CASTLE_IMAGE.copy();
        collisionImage.resize(width, height);
    }

    @Override
    public PImage[] getImages(int direction) {
        return new PImage[]{CASTLE_IMAGE};
    }

    @Override
    public int getMoralChange() {
        return 0;
    }

    @Override
    public void render() {
        sm.image(CASTLE_IMAGE, 0, 0, width, height);
        renderHealth(CASTLE_HEALTH_BAR_X, CASTLE_HEALTH_BAR_Y, CASTLE_HEALTH_BAR_WIDTH, CASTLE_HEALTH_BAR_HEIGHT);
    }

    public void restore() {
        health = 100;
    }
}
