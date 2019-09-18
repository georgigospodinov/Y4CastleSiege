package magical;

import general.Field;
import processing.core.PImage;
import processing.core.PVector;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public abstract class Spell {

    private static final float SPELL_W = P.getFloat("spell w") * sm.getWidth();
    private static final float SPELL_H = P.getFloat("spell h") * sm.getHeight();
    private static final int SPELL_CASTING_TIME = P.getInt("spell casting time");
    private static final float SPELL_MOVE_SPEED = P.getFloat("spell move speed") * sm.getWidth();

    public PVector position;
    public final PVector targetLocation;
    private boolean hasNotBeenCast = true;
    private Integer castStart = null;
    private final PImage castingImage;

    public abstract void takeEffect();

    public void render() {
        if (position.x >= sm.getWidth() / 2f && hasNotBeenCast) {
            // The game will enter this else-if multiple times. This will simulate the casting staying in one place.

            if (castStart == null) {
                castStart = sm.millis();
            }
            // The spell has been cast.
            else if (sm.millis() - castStart >= SPELL_CASTING_TIME) {
                takeEffect();
                hasNotBeenCast = false;
            }
        }
        else if (position.x >= sm.getWidth()) {
            Field.castingSpell = null;
        }
        else position.x += SPELL_MOVE_SPEED;


        sm.imageMode(CENTER);
        sm.image(castingImage, position.x, position.y, SPELL_W, SPELL_H);
        sm.imageMode(CORNER);
    }

    public Spell(PImage castingImage) {
        this.castingImage = castingImage;
        position = new PVector(0, sm.getHeight() / 2f);
        targetLocation = new PVector(sm.mouseX, sm.mouseY);
    }
}
