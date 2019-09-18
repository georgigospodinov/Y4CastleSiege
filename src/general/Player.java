package general;

import magical.Creature;
import processing.core.PImage;
import units.Archer;
import units.Footman;
import units.Soldier;

import java.util.LinkedHashSet;

import static main.Run.L;
import static main.Run.P;
import static main.ScreenManager.sm;

public class Player {
    private static final float UNIT_SELECTOR_GAP = P.getFloat("unit selector gap") * sm.getWidth();
    private static final float UNIT_SELECTOR_X = P.getFloat("unit selector x") * sm.getWidth();
    private static final float UNIT_SELECTOR_Y = P.getFloat("unit selector y") * sm.getHeight();
    public static final float UNIT_SELECTOR_W = P.getFloat("unit selector w") * sm.getWidth();
    public static final float UNIT_SELECTOR_H = P.getFloat("unit selector h") * sm.getHeight();
    public static final PImage UNIT_HOLDER = sm.loadImageProp("unit holder");
    public static final float UNIT_HOLDER_W_FACTOR = P.getFloat("unit holder w factor");
    public static final float UNIT_HOLDER_H_FACTOR = P.getFloat("unit holder h factor");

    private static final float CREATURE_SELECTOR_GAP = P.getFloat("creature selector gap") * sm.getWidth();
    private static final float CREATURE_SELECTOR_X = P.getFloat("creature selector x") * sm.getWidth();
    private static final float CREATURE_SELECTOR_Y = P.getFloat("creature selector y") * sm.getHeight();
    public static final float CREATURE_SELECTOR_W = P.getFloat("creature selector w") * sm.getWidth();
    public static final float CREATURE_SELECTOR_H = P.getFloat("creature selector h") * sm.getHeight();

    public static final int MAX_MORAL = P.getInt("max moral");

    private int moral = 0;
    private LinkedHashSet<Class<? extends Soldier>> availableSoldiers = new LinkedHashSet<>();
    private LinkedHashSet<Creature> availableCreatures = new LinkedHashSet<>();

    public void addCreature(Creature c) {
        availableCreatures.add(c);
    }

    public void amendMoral(int amount) {
        moral += amount;
        if (moral < -MAX_MORAL)
            moral = -MAX_MORAL;
        else if (moral > MAX_MORAL)
            moral = MAX_MORAL;
    }

    public int getMoral() {
        return moral;
    }

    public Player() {
        availableSoldiers.add(Footman.class);
        availableSoldiers.add(Archer.class);
    }

    private float iterationX, iterationY;

    public void renderUnitSelector() {
        iterationX = UNIT_SELECTOR_X;
        iterationY = UNIT_SELECTOR_Y;
        // Draws the wooden background rectangle
        try {
            float height = ((PImage) availableSoldiers.iterator().next().getField("SELECTOR_IMAGE").get(null)).height;
            float width = 0;
            for (int i = 0; i < availableSoldiers.size(); i++) {
                width += UNIT_SELECTOR_W + UNIT_SELECTOR_GAP;
            }
            width -= UNIT_SELECTOR_GAP;
            float halfChangeW = width * (UNIT_HOLDER_W_FACTOR - 1) / 2f;
            float halfChangeH = height * (UNIT_HOLDER_H_FACTOR - 1) * 0.65f;
            width *= UNIT_HOLDER_W_FACTOR;
            height *= UNIT_HOLDER_H_FACTOR;
            sm.image(UNIT_HOLDER, UNIT_SELECTOR_X - halfChangeW, UNIT_SELECTOR_Y - halfChangeH, width, height);
        }
        catch (Exception e) {
            L.log(e);
        }
        // Draws the soldiers.
        availableSoldiers.forEach(soldierClass -> {
            try {
                PImage image = (PImage) soldierClass.getField("SELECTOR_IMAGE").get(null);
                sm.image(image, iterationX, iterationY);
                sm.fill(0, 0, 255);
                sm.textSize(15);
                sm.text(Field.level.getRemaining(soldierClass), iterationX + image.width * 0.33f, iterationY - 3);
                iterationX += UNIT_SELECTOR_W + UNIT_SELECTOR_GAP;
            }
            catch (Exception e) {
                L.log(e);
            }
        });
    }

    public void renderCreatureSelector() {
        if (availableCreatures.isEmpty())
            return;
        iterationX = CREATURE_SELECTOR_X;
        iterationY = CREATURE_SELECTOR_Y;
        // Draws the wooden background rectangle
        try {
            float height = CREATURE_SELECTOR_H;
            float width = 0;
            for (int i = 0; i < availableCreatures.size(); i++) {
                width += CREATURE_SELECTOR_W + CREATURE_SELECTOR_GAP;
            }
            width -= CREATURE_SELECTOR_GAP;
            float halfChangeW = width * (UNIT_HOLDER_W_FACTOR - 1) / 2f;
            float halfChangeH = height * (UNIT_HOLDER_H_FACTOR - 1) / 2f;
            width *= UNIT_HOLDER_W_FACTOR;
            height *= UNIT_HOLDER_H_FACTOR;
            sm.image(UNIT_HOLDER, CREATURE_SELECTOR_X - halfChangeW, CREATURE_SELECTOR_Y - halfChangeH, width, height);
        }
        catch (Exception e) {
            L.log(e);
        }
        // Draws the creatures
        availableCreatures.forEach(creature -> {
            try {
                sm.image(creature.meet, iterationX, iterationY, CREATURE_SELECTOR_W, CREATURE_SELECTOR_H);
                sm.text(Field.level.getRemaining(creature.name), iterationX + CREATURE_SELECTOR_W * 0.33f, iterationY - 3);
                iterationX += CREATURE_SELECTOR_W + CREATURE_SELECTOR_GAP;
            }
            catch (Exception e) {
                L.log(e);
            }
        });
    }

    public void render() {
        renderUnitSelector();
        renderCreatureSelector();
    }

    public Class<? extends Soldier> getSelectedUnitType(int x, int y) {
        iterationX = UNIT_SELECTOR_X;
        iterationY = UNIT_SELECTOR_Y;
        for (Class<? extends Soldier> soldierClass : availableSoldiers) {
            if (iterationX <= x && x <= iterationX + UNIT_SELECTOR_W) {
                if (iterationY <= y && y <= iterationY + UNIT_SELECTOR_H) {
                    return soldierClass;
                }
            }
            iterationX += UNIT_SELECTOR_W + UNIT_SELECTOR_GAP;
        }
        return null;
    }

    public Creature getSelectedCreature(int x, int y) {
        iterationX = CREATURE_SELECTOR_X;
        iterationY = CREATURE_SELECTOR_Y;
        for (Creature creature : availableCreatures) {
            if (iterationX <= x && x <= iterationX + CREATURE_SELECTOR_W) {
                if (iterationY <= y && y <= iterationY + CREATURE_SELECTOR_H) {
                    return creature;
                }
            }
            iterationX += CREATURE_SELECTOR_W + CREATURE_SELECTOR_GAP;
        }
        return null;
    }
}
