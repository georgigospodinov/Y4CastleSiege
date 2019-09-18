package levels.elements;

import general.Attribute;
import general.Field;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import units.Soldier;
import units.Unit;

import static main.Run.*;
import static main.ScreenManager.sm;
import static processing.core.PConstants.*;

public abstract class Defence {
    public static final int NUMBER_OF_DIRECTIONS = 8;
    public static final int NORTH = 0, NORTH_EAST = 1, EAST = 2, SOUTH_EAST = 3, SOUTH = 4, SOUTH_WEST = 5, WEST = 6, NORTH_WEST = 7;
    public static final float TOWER_WIDTH = P.getFloat("tower width");
    public static final float TOWER_HEIGHT = P.getFloat("tower height");
    public static final float WEAPON_HEIGHT_FACTOR = P.getFloat("weapon height factor");
    public static final float WEAPON_WIDTH_FACTOR = P.getFloat("weapon width factor");
    private static final PImage TOWER = sm.loadImageProp("tower image");

    public static int orientationToDirection(float ori) {
        int floor = PApplet.floor((ori + HALF_PI) / (QUARTER_PI / 2f));
        return PApplet.ceil(floor / 2f) % 8;
    }

    public static PImage[][] loadOrientationImages(String superFolderName) {
        PImage[][] images = new PImage[NUMBER_OF_DIRECTIONS][];
        images[NORTH] = loadImages(superFolderName + "north/");
        images[NORTH_EAST] = loadImages(superFolderName + "north_east/");
        images[EAST] = loadImages(superFolderName + "east/");
        images[SOUTH_EAST] = loadImages(superFolderName + "south_east/");
        images[SOUTH] = loadImages(superFolderName + "south/");
        images[SOUTH_WEST] = loadImages(superFolderName + "south_west/");
        images[WEST] = loadImages(superFolderName + "west/");
        images[NORTH_WEST] = loadImages(superFolderName + "north_west/");
        return images;
    }

    protected PImage collisionImage = TOWER.copy();
    public final PVector position;
    public final int width, height;
    public final float initialHealth;
    protected float health;
    public float lastAttack = 0;
    public final Attribute attackDamage;
    public final Attribute totalAttackTime;
    protected Unit combatTarget = null;

    public PImage getCollisionImage() {
        return collisionImage;
    }

    public void takeDamage(float damage) {
        if (damage < 0)
            throw new IllegalArgumentException("Cannot deal negative damage: " + damage);
        health -= damage;
        if (health < 0)
            health = 0;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public abstract PImage[] getImages(int direction);

    public abstract int getMoralChange();

    public Defence(Float x, Float y, Integer width, Integer height, Float initialHealth) {
        this.initialHealth = initialHealth;
        this.health = initialHealth;
        this.position = new PVector(x, y);
        this.width = width;
        this.height = height;
        attackDamage = new Attribute(0);
        totalAttackTime = new Attribute(0);
        collisionImage.resize(width, height);
    }

    public Defence(Float x, Float y, Float initialHealth, Float attackDamage, Float totalAttackTime) {
        this.initialHealth = initialHealth;
        this.health = initialHealth;
        this.position = new PVector(x, y);
        this.width = (int) (sm.getWidth() * TOWER_WIDTH);
        this.height = (int) (sm.getHeight() * TOWER_HEIGHT);
        this.attackDamage = new Attribute(attackDamage);
        this.totalAttackTime = new Attribute(totalAttackTime);
        collisionImage.resize(width, height);
    }

    public PVector directionFrom(Soldier s) {
        int thisTop = (int) position.y;
        int thisBot = (int) (position.y + this.height);
        int thisLeft = (int) position.x;
        int thisRight = (int) (position.x + this.width);
        int sTop = (int) s.position.y;
        int sBot = (int) s.height + sTop;
        int sLeft = (int) s.position.x;
        int sRight = (int) s.width + sLeft;

        int h = 0, w = 0;
        if (sTop < thisTop)
            h = thisTop - sTop;
        if (thisBot < sBot)
            h = thisBot - sBot;
        if (sLeft < thisLeft)
            w = thisLeft - sLeft;
        if (thisRight < sRight)
            w = thisRight - sRight;

        return new PVector(w, h);
    }

    public float distTo(PVector s) {
        int thisTop = (int) position.y;
        int thisBot = (int) (position.y + this.height);
        int thisLeft = (int) position.x;
        int thisRight = (int) (position.x + this.width);
        int sy = (int) s.y;
        int sx = (int) s.x;

        int h = 0, w = 0;
        if (sy < thisTop)
            h = thisTop - sy;
        if (thisBot < sy)
            h = sy - thisBot;
        if (sx < thisLeft)
            w = thisLeft - sx;
        if (thisRight < sx)
            w = sx - thisRight;
        return PApplet.sqrt(h * h + w * w);
    }

    public void renderHealth(float x, float y, float w, float h) {
        sm.noStroke();

        // Health remaining
        float healthWidth = w * health / initialHealth;
        sm.fill(255, 0, 0);
        sm.rect(x, y + 2, healthWidth, h - 3);

        // Health missing
        float missingWidth = w - healthWidth;
        sm.fill(255);
        sm.rect(x + healthWidth, y + 2, missingWidth, h - 3);

        // Frame
        sm.stroke(0);
        sm.strokeWeight(2);
        sm.noFill();
        sm.rect(x, y, w, h);
    }

    public void renderHealth() {
        renderHealth(position.x, position.y - 15, width, 10);
    }

    public PImage getCurrentImage() {
        float ori = orientation();
        int dir = orientationToDirection(ori);
        PImage[] images = getImages(dir);
        // Out of combat
        if (combatTarget == null)
            return images[0];

        float elapsed = sm.millis() - lastAttack;
        int index;
        if (elapsed >= totalAttackTime.getValue())
            index = images.length - 1;
        else index = (int) (elapsed * images.length / totalAttackTime.getValue());

        return images[index];
    }

    public void render() {
        lifeCycle();
        sm.image(collisionImage, position.x, position.y);
        PImage image = getCurrentImage();
        float w = width * WEAPON_WIDTH_FACTOR, h = height * WEAPON_HEIGHT_FACTOR;
        sm.image(image, position.x + width / 2f - w / 2f, position.y, w, h);
    }

    public Unit findClosestUnit() {
        Unit closest = null;
        float dist = Float.MAX_VALUE, d;
        for (Unit unit : Field.units) {
            // Ignore dead units.
            if (unit.isDead()) continue;

            d = distTo(unit.calcCenterPoint());
            if (d < dist) {
                dist = d;
                closest = unit;
            }
        }

        return dist < VISION_RANGE ? closest : null;
    }

    public void lifeCycle() {
        if (combatTarget == null)
            combatTarget = findClosestUnit();
        else if (combatTarget.isDead())
            combatTarget = null;
        else attack();
    }

    public void attack() {
        int time = sm.millis();
        if (time - lastAttack >= totalAttackTime.getValue()) {
            combatTarget.takeDamage(attackDamage.getValue());
            lastAttack = time;
        }
    }

    public float orientation() {
        if (combatTarget == null)
            return HALF_PI;

        PVector topCenter = new PVector(position.x + width / 2f, position.y);
        PVector orientation = PVector.sub(combatTarget.calcCenterPoint(), topCenter);
        float heading = orientation.heading();
        if (heading < 0)
            heading += TWO_PI;
        return heading;
    }
}
