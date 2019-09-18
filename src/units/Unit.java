package units;

import general.Field;
import levels.elements.Castle;
import levels.elements.Defence;
import processing.core.PApplet;
import processing.core.PVector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static main.Run.L;
import static main.Run.VISION_RANGE;
import static main.ScreenManager.sm;

public class Unit {
    private static final float WIDTH_FACTOR = 1.25f, HEIGHT_FACTOR = 1.25f;
    private static final int MARGIN = 10;
    private float health;
    private float initialHealth;
    private float initialWidth;
    private final float healthPerSoldier;
    public final ArrayList<Soldier> soldiers;
    public final Class<? extends Soldier> type;
    private PVector lastPosition;

    public Unit(int soldierCount, Class<? extends Soldier> type, float x, float y) throws Exception {
        this.type = type;
        health = (float) type.getField("HEALTH").get(null);
        initialHealth = health;
        healthPerSoldier = health / soldierCount;
        soldiers = new ArrayList<>(soldierCount);

        Constructor<? extends Soldier> constructor = type.getConstructor(Float.class, Float.class);
        int frontHalf = PApplet.ceil(soldierCount / 2f);
        int hindHalf = soldierCount / 2;
        float frontHalfWidth = 0;
        for (int j = 0; j < frontHalf; j++) {
            frontHalfWidth += Soldier.DEFAULT_WIDTH * WIDTH_FACTOR;
        }
        initialWidth = frontHalfWidth - (Soldier.DEFAULT_WIDTH * (WIDTH_FACTOR - 1));
        frontHalfWidth = frontHalfWidth / 2f;
        if (x < frontHalfWidth + MARGIN) {
            x = frontHalfWidth + MARGIN;
        }
        if (x > sm.getWidth() - frontHalfWidth - MARGIN) {
            x = sm.getWidth() - frontHalfWidth - MARGIN;
        }
        if (y > sm.getHeight() - 2 * Soldier.DEFAULT_HEIGHT * HEIGHT_FACTOR) {
            y = sm.getHeight() - 2 * Soldier.DEFAULT_HEIGHT * HEIGHT_FACTOR;
        }
        PVector p = new PVector(x, y);
        for (int i = 0; i < frontHalf; i++) {
            float sx = p.x - frontHalfWidth;
            float sy = p.y - Soldier.DEFAULT_HEIGHT / 2 * HEIGHT_FACTOR;
            soldiers.add(constructor.newInstance(sx, sy));
            frontHalfWidth -= Soldier.DEFAULT_WIDTH * WIDTH_FACTOR;
        }
        float hindHalfWidth = 0;
        for (int i = 0; i < hindHalf; i++) {
            hindHalfWidth += Soldier.DEFAULT_WIDTH * WIDTH_FACTOR;
        }
        hindHalfWidth = hindHalfWidth / 2f;
        for (int i = 0; i < hindHalf; i++) {
            float sx = p.x - hindHalfWidth;
            float sy = p.y + Soldier.DEFAULT_HEIGHT / 2 * HEIGHT_FACTOR;
            soldiers.add(constructor.newInstance(sx, sy));
            hindHalfWidth -= Soldier.DEFAULT_WIDTH * WIDTH_FACTOR;
        }

    }

    public void render() {
        move();
        for (Soldier s : soldiers) {
            s.render();
        }
        renderHealthBar();
    }

    public void renderHealthBar() {
        float h = 5;
        PVector p = calcCenterPoint();
        float x = p.x - initialWidth / 2;
        float y = p.y + soldiers.get(0).height * HEIGHT_FACTOR;
        sm.noStroke();

        // Health remaining
        float healthWidth = initialWidth * health / initialHealth;
        sm.fill(255, 0, 0);
        sm.rect(x, y + 2, healthWidth, h - 3);

        // Health missing
        float missingWidth = initialWidth - healthWidth;
        sm.fill(255);
        sm.rect(x + healthWidth, y + 2, missingWidth, h - 3);

        // Frame
        sm.stroke(0);
        sm.strokeWeight(2);
        sm.noFill();
        sm.rect(x, y, initialWidth, h);
    }

    public PVector calcCenterPoint() {
        PVector sum = new PVector(0, 0);
        if (soldiers.isEmpty()) return lastPosition;

        for (Soldier soldier : soldiers) {
            sum.add(soldier.position);
            sum.add(soldier.width / 2f, soldier.height / 2f);
        }

        PVector center = sum.div(soldiers.size());
        if (center.x < initialWidth / 2f)
            center.x = initialWidth / 2f + 1;
        if (center.x > sm.getWidth() - initialWidth / 2f)
            center.x = sm.getWidth() - initialWidth / 2f - 1;
        return center;
    }

    private LinkedHashSet<Soldier> getReforming() {
        LinkedHashSet<Soldier> reforming = new LinkedHashSet<>();
        for (Soldier soldier : soldiers) {
            if (soldier.isReforming())
                reforming.add(soldier);
        }
        return reforming;
    }

    public void moveInFormation(float x, float y) {
        PVector p = calcCenterPoint();
        PVector target = new PVector(x, y);
        PVector changeForEach = PVector.sub(target, p);
        for (Soldier s : soldiers) {
            PVector dest = PVector.add(s.position, changeForEach);
            s.setDestination(dest);
        }
    }

    public void reform() {
        PVector p = calcCenterPoint();
        int frontHalf = PApplet.ceil(soldiers.size() / 2f);

        float frontHalfWidth = 0;
        for (int i = 0; i < frontHalf; i++) {
            frontHalfWidth += soldiers.get(i).width * WIDTH_FACTOR;
        }
        frontHalfWidth = frontHalfWidth / 2f;
        for (int i = 0; i < frontHalf; i++) {
            Soldier soldier = soldiers.get(i);
            float x = p.x - frontHalfWidth;
            float y = p.y - soldier.height / 2 * HEIGHT_FACTOR;
            soldier.reform(new PVector(x, y));
            frontHalfWidth -= soldier.width * WIDTH_FACTOR;
        }

        int hindHalf = soldiers.size() / 2;
        float hindHalfWidth = 0;
        for (int i = 0; i < hindHalf; i++) {
            hindHalfWidth += soldiers.get(frontHalf + i).width * WIDTH_FACTOR;
        }
        hindHalfWidth = hindHalfWidth / 2f;
        for (int i = 0; i < hindHalf; i++) {
            Soldier soldier = soldiers.get(frontHalf + i);
            float x = p.x - hindHalfWidth;
            float y = p.y + soldier.height / 2 * HEIGHT_FACTOR;
            soldier.reform(new PVector(x, y));
            hindHalfWidth -= soldier.width * WIDTH_FACTOR;
        }
    }

    private Defence def;

    public void move() {
        // If the soldiers are reforming, keep doing so.
        LinkedHashSet<Soldier> reforming = getReforming();
        if (!reforming.isEmpty()) {
            reforming.forEach(Soldier::move);
            return;
        }

        // Otherwise, the unit may select a new target
        if (def == null) {
            def = getClosest();
            if (def != null) {
                soldiers.forEach(s -> s.engage(def));
            }
        }
        else if (def.isDestroyed()) {
            soldiers.forEach(Soldier::disengage);
            def = null;
            reform();
        }
        soldiers.forEach(Soldier::move);
    }

    public Defence getClosest() {
        PVector p = calcCenterPoint();
        Defence closest = null;
        float dist = Float.MAX_VALUE, d;
        for (Defence defence : Field.level.defences) {
            // Ignore destroyed defences.
            if (defence.isDestroyed()) continue;

            d = defence.distTo(p);
            if (d < dist) {
                dist = d;
                closest = defence;
            }
        }

        Castle c = Field.level.castle;
        if (!c.isDestroyed()) {
            d = c.distTo(p);
            if (d < dist) {
                dist = d;
                closest = c;
            }
        }

        return dist < VISION_RANGE ? closest : null;
    }

    public void takeHeal(float amount) {
        // Zombies cannot be healed.
        if (type.equals(Zombie.class)) return;

        health += amount;
        if (health >= initialHealth) {
            health = initialHealth;
        }

        PVector p = calcCenterPoint();

        int soldiersAlive = PApplet.ceil(health / healthPerSoldier);
        Soldier s = soldiers.get(0);
        try {
            Constructor<? extends Soldier> constructor = type.getConstructor(Float.class, Float.class);
            while (soldiers.size() < soldiersAlive) {
                Soldier resurrected = constructor.newInstance(p.x, p.y);
                soldiers.add(resurrected);
                if (s.combatTarget != null)
                    resurrected.engage(s.combatTarget);
            }
        }
        catch (Exception e) {
            L.log(e);
        }

        if (s.combatTarget == null)
            reform();
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            soldiers.clear();
            return;
        }

        int soldiersAlive = PApplet.ceil(health / healthPerSoldier);
        while (soldiers.size() > soldiersAlive) {
            Soldier dead = soldiers.remove(0);
            lastPosition = dead.position;
        }
    }

    public boolean isDead() {
        return soldiers.isEmpty();
    }

    public int countDead() {
        int initial = (int) (initialHealth / healthPerSoldier);
        return initial - soldiers.size();
    }

    public void unhealable() {
        initialHealth = health;
        float soldierCount = initialHealth / healthPerSoldier;
        int frontHalf = PApplet.ceil(soldierCount / 2f);
        float frontHalfWidth = 0;
        for (int j = 0; j < frontHalf; j++) {
            frontHalfWidth += Soldier.DEFAULT_WIDTH * WIDTH_FACTOR;
        }
        initialWidth = frontHalfWidth - (Soldier.DEFAULT_WIDTH * (WIDTH_FACTOR - 1));
    }

    @Override
    public String toString() {
        if (soldiers.isEmpty())
            return "Dead units.";
        return type.getSimpleName() + "s at " + health + " health";
    }
}
