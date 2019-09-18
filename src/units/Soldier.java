package units;

import general.Attribute;
import general.Field;
import general.Player;
import levels.elements.Defence;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import screens.Play;

import static main.Run.P;
import static main.Run.R;
import static main.ScreenManager.sm;

public abstract class Soldier {

    private static final float MOVEMENT_SATISFACTION_RADIUS_PORTION = P.getFloat("movement satisfaction radius portion");
    static final float DEFAULT_WIDTH = P.getFloat("soldier default width") * sm.getWidth();
    static final float DEFAULT_HEIGHT = P.getFloat("soldier default height") * sm.getHeight();
    private static final float BATTLE_DESYNCH = P.getFloat("battle desynch");

    public static void onImplementingClassInit(PImage selector, PImage pickedUp, PImage[] walk, PImage[] attack) {
        selector.resize((int) Player.UNIT_SELECTOR_W, (int) Player.UNIT_SELECTOR_H);
        pickedUp.resize((int) Play.PICKED_UP_TYPE_W, (int) Play.PICKED_UP_TYPE_H);
        if (walk != null)
            for (PImage w : walk) {
                w.resize((int) DEFAULT_WIDTH, (int) DEFAULT_HEIGHT);
            }
        if (attack != null)
            for (PImage a : attack) {
                a.resize((int) DEFAULT_WIDTH, (int) DEFAULT_HEIGHT);
            }
    }

    public PVector position;
    public final float width, height;
    protected final float satisfactionRadius;
    protected PVector destination;
    private boolean reforming = false, inCombat = false;
    public Defence combatTarget;
    public final Attribute speed;
    public final Attribute attackRange;

    // Animation control, all are in milliseconds
    private float totalWalkTime = 700;
    private int lastWalkChange = 0;  // The last time the walk image was changed
    private int walkIndex = 0;  // The index of the last walk image
    public final Attribute totalAttackTime;
    public final Attribute attackDamage;
    private int lastAttack = 0;

    public abstract PImage[] getWalkImages();

    public abstract PImage[] getAttackImages();

    private PImage getCurrentWalkImage() {
        PImage[] wi = getWalkImages();
        int now = sm.millis();
        float timePerImage = totalWalkTime / wi.length;
        if (now - lastWalkChange > timePerImage) {
            walkIndex++;
            lastWalkChange = now;
        }
        if (walkIndex == wi.length) {
            walkIndex = 0;
            lastWalkChange = now;
        }
        return wi[walkIndex];
    }

    private PImage getCurrentAttackImage() {
        PImage[] ai = getAttackImages();
        int elapsed = sm.millis() - lastAttack;
        int index;
        if (elapsed >= totalAttackTime.getValue())
            index = ai.length - 1;
        else index = (int) (ai.length * elapsed / totalAttackTime.getValue());
        return ai[index];
    }

    public void render() {
        PImage img = inCombat && withinAttackRange() ? getCurrentAttackImage() : getCurrentWalkImage();
        sm.image(img, position.x, position.y);

        // shows a red box around the soldier if it's colliding with others in its unit
//        if (collidesSoldier()) {
//            sm.stroke(255, 0, 0);
//            sm.strokeWeight(5);
//            sm.noFill();
//            sm.rect(position.x, position.y, width, height);
//        }
    }

    Soldier(Float x, Float y, Float width, Float height, Float attackDamage, Float totalAttackTime, Float speed, Float attackRange) {
        position = new PVector(x, y);
        this.width = width;
        this.height = height;
        this.attackDamage = new Attribute(attackDamage);
        this.totalAttackTime = new Attribute(totalAttackTime);
        satisfactionRadius = PApplet.sqrt(width * width + height * height) / MOVEMENT_SATISFACTION_RADIUS_PORTION;
        this.speed = new Attribute(speed);
        this.attackRange = new Attribute(attackRange * height);
    }

    Soldier(Float x, Float y, Float attackDamage, Float totalAttackTime, Float speed, Float attackRange) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, attackDamage, totalAttackTime, speed, attackRange);
    }

    private void bounceFromBorders() {
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y < 0) {
            position.y = 0;
            destination.y = -destination.y;
        }
        if (position.x > sm.getWidth() - width) {
            position.x = sm.getWidth() - width;
        }
        if (position.y > sm.getHeight() - height) {
            position.y = sm.getHeight() - height;
            destination.y = -destination.y;
        }
    }

    public boolean isReforming() {
        return reforming;
    }

    public void setDestination(PVector destination) {
        if (!reforming)
            this.destination = destination;
    }

    private boolean destinationReached() {
        if (destination == null)
            return true;

        float dist = destination.dist(position);
        return dist <= satisfactionRadius;
    }

    private PVector calcNorth() {
        return new PVector(position.x, -sm.getHeight() * 2);
    }

    private void moveTowards(PVector target) {
        PVector t = PVector.sub(target, position).limit(speed.getValue());
        position.add(t);
        bounceFromBorders();
    }

    public void reform(PVector target) {
        destination = target;
        reforming = true;
    }

    private boolean withinAttackRange() {
        if (attackRange.getValue() > this.height)
            return destination.dist(position) <= attackRange.getValue();
        else return collidesCombatTarget();
    }

    private int chaosDesynch = 0;

    /**
     * In the chaos of battle, soldiers do not attack synchronously.
     */
    private void chaos() {
        int bound = (int) (totalAttackTime.getValue() * BATTLE_DESYNCH);
        chaosDesynch = R.nextInt(2 * bound) - bound;
    }

    private void attack() {
        if (withinAttackRange()) {
            int time = sm.millis();
            if (time - lastAttack >= totalAttackTime.getValue() + chaosDesynch) {
                combatTarget.takeDamage(attackDamage.getValue());
                lastAttack = time;
                chaos();
            }
        }
        else {
            if (destinationReached()) {
                destination = PVector.add(new PVector(0, -speed.getValue() * 2), position);
            }
            moveTowards(destination);
        }
    }

    public void move() {
        if (reforming) {
            moveTowards(destination);
            if (destinationReached())
                reforming = false;
            return;
        }
        if (inCombat) {
            // do other stuff
            attack();
            return;
        }

        if (destinationReached()) {
            destination = calcNorth();
        }

        moveTowards(destination);
    }

    public void engage(Defence target) {
        destination = PVector.add(target.directionFrom(this), position);
        combatTarget = target;
        inCombat = true;
    }

    public void disengage() {
        inCombat = false;
        combatTarget = null;
    }

    // TODO: Report - I thought about checking pixels and I found this algorithm.
    // https://www.openprocessing.org/sketch/149174

    private boolean collidesSoldier(Soldier other) {
        return collides(other.inCombat ? other.getCurrentAttackImage() : other.getCurrentWalkImage(), other.position);
    }

    private boolean collides(PImage imgB, PVector other) {
        PImage imgA = inCombat ? getCurrentAttackImage() : getCurrentWalkImage();
        int topA = (int) this.position.y;
        int botA = (int) this.position.y + imgA.height;
        int leftA = (int) this.position.x;
        int rightA = (int) this.position.x + imgA.width;
        int topB = (int) other.y;
        int botB = (int) other.y + imgB.height;
        int leftB = (int) other.x;
        int rightB = (int) other.x + imgB.width;

        if (botA <= topB || botB <= topA || rightA <= leftB || rightB <= leftA)
            return false;

        // If we get here, we know that there is an overlap
        // So we work out where the sides of the overlap are
        int leftO = (leftA < leftB) ? leftB : leftA;
        int rightO = (rightA > rightB) ? rightB : rightA;
        int botO = (botA > botB) ? botB : botA;
        int topO = (topA < topB) ? topB : topA;


        // P is the top-left, S is the bottom-right of the overlap
        int APx = leftO - leftA;
        int APy = topO - topA;
        int ASx = rightO - leftA;
        int ASy = botO - topA - 1;
        int BPx = leftO - leftB;
        int BPy = topO - topB;

        int widthO = rightO - leftO;
        boolean foundCollision = false;

        // Images to test
        imgA.loadPixels();
        imgB.loadPixels();

        // These are widths in BYTES. They are used inside the loop
        //  to avoid the need to do the slow multiplications
        int surfaceWidthA = imgA.width;
        int surfaceWidthB = imgB.width;

        boolean thisPixelVisible, castlePixelVisible;

        // Get start pixel positions
        int pA = (APy * surfaceWidthA) + APx;
        int pB = (BPy * surfaceWidthB) + BPx;

        for (int ay = APy; ay < ASy; ay++) {
            for (int ax = APx; ax < ASx; ax++) {
                // Custom check.
                thisPixelVisible = imgA.pixels[pA] != 0;
                castlePixelVisible = imgB.pixels[pB] != 0;
                if (thisPixelVisible && castlePixelVisible) {
                    foundCollision = true;
                    break;
                }
                // End of custom check.
                pA++;
                pB++;
            }
            if (foundCollision) break;
            pA = pA + surfaceWidthA - widthO;
            pB = pB + surfaceWidthB - widthO;
        }
        return foundCollision;
    }

    private boolean collidesCombatTarget() {
        if (combatTarget == null) return false;
        return collides(combatTarget.getCollisionImage(), combatTarget.position);
    }

    public boolean collidesSoldier() {
        for (Unit unit : Field.units) {
            for (Soldier soldier : unit.soldiers) {
                if (soldier == this)
                    continue;
                if (collidesSoldier(soldier))
                    return true;
            }
        }
        return false;
    }
}

// TODO: Report that I have not found an algorithm to manage units and I came up with X on my own.
// where X is insert whatever I come up in units.Unit. (Unit.reform() and Unit.moveInFormation()).