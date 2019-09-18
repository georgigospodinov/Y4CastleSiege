package screens;

import general.Field;
import magical.Creature;
import processing.core.PImage;
import units.Soldier;

import static main.Run.L;
import static main.Run.P;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;
import static processing.core.PConstants.CENTER;
import static util.PrintFormatting.print;

public class Play extends AScreen {

    public static final float PICKED_UP_TYPE_W = P.getFloat("picked up type w") * sm.getWidth();
    public static final float PICKED_UP_TYPE_H = P.getFloat("picked up type h") * sm.getHeight();
    private static final PImage BACKGROUND_TILE = sm.loadImageProp("play background tile");
    private static final int BACKGROUND_TILE_W = (int) (P.getFloat("play background tile w") * sm.getWidth());
    private static final int BACKGROUND_TILE_H = (int) (P.getFloat("play background tile h") * sm.getHeight());

    public static final float SPAWN_LIMIT = sm.getHeight() * 0.8f;

    private Class<? extends Soldier> pickedUpUnitType = null;
    private Creature pickedUpCreature = null;

    public Play() {
        super(BACKGROUND_TILE, BACKGROUND_TILE_W, BACKGROUND_TILE_H);
    }

    @Override
    public void press(int keyCode) {
        /*
        if (keyCode == X ) {  // key associated with code X
         */
        if (keyCode == 32) {  // Space
            Field.setFocusPoint(sm.mouseX, sm.mouseY);
        }
        if (keyCode == 82) {  // R
            Field.level.castle.restore();
        }
        if (keyCode == 8) {  // BackSpace
            try {
                Field.level = Field.level.getClass().getConstructor().newInstance();
                Field.units.clear();
                Field.dead.clear();
                Field.castingSpell = null;  // End any spell being cast.
            }
            catch (Exception e) {
                L.log(e);
            }
        }
    }

    @Override
    public void leftClick() {
        super.leftClick();
        int X = sm.mouseX, Y = sm.mouseY;
        Class<? extends Soldier> selectedUnitType = player.getSelectedUnitType(X, Y);
        Creature selectedCreature = player.getSelectedCreature(X, Y);

        if (selectedUnitType != null) {
            if (pickedUpUnitType == null && pickedUpCreature == null) {
                if (Field.level.canTake(selectedUnitType))
                    pickedUpUnitType = selectedUnitType;
            }
            return;
        }

        if (selectedCreature != null) {
            if (pickedUpUnitType == null && pickedUpCreature == null) {
                if (Field.level.canTake(selectedCreature.name))
                    pickedUpCreature = selectedCreature;
            }
            return;
        }

        if (pickedUpUnitType != null) {
            if (Y < SPAWN_LIMIT) {
                print("Units can only be spawned before the red line.");
            }
            else {
                Field.level.takeOut(pickedUpUnitType);
                Field.createUnit(X, Y, pickedUpUnitType);
                pickedUpUnitType = null;
            }
            return;
        }

        if (pickedUpCreature != null) {
            Field.level.takeOut(pickedUpCreature.name);
            pickedUpCreature.cast();
            pickedUpCreature = null;
        }
    }

    @Override
    public void rightClick() {
        super.rightClick();
        pickedUpUnitType = null;
        pickedUpCreature = null;
    }

    private void renderPickedUpUnitType() {
        if (pickedUpUnitType == null)
            return;

        int X = sm.mouseX, Y = sm.mouseY;
        try {
            PImage image = (PImage) pickedUpUnitType.getField("PICKED_UP_IMAGE").get(null);
            sm.image(image, X - PICKED_UP_TYPE_W / 2, Y - PICKED_UP_TYPE_H / 2);
        }
        catch (Exception e) {
            L.log(e);
        }

        if (Y < SPAWN_LIMIT) {
            sm.strokeWeight(2);
            sm.stroke(255, 0, 0);
            sm.noFill();
            sm.line(0, SPAWN_LIMIT, sm.getWidth(), SPAWN_LIMIT);
        }
    }

    private void renderPickUpCreature() {
        if (pickedUpCreature == null)
            return;

        int X = sm.mouseX, Y = sm.mouseY;
        sm.image(pickedUpCreature.meet, X - PICKED_UP_TYPE_W / 2, Y - PICKED_UP_TYPE_H / 2, PICKED_UP_TYPE_W, PICKED_UP_TYPE_H);
        try {
            float r = (float) pickedUpCreature.spell.getField("RANGE").get(null);
            sm.strokeWeight(2);
            sm.stroke(0, 0, 255);
            sm.fill(0, 63, 255, 0.8f);
            sm.ellipseMode(CENTER);
            sm.ellipse(X, Y, r, r);
        }
        catch (Exception e) {
            L.log(e);
        }
    }

    @Override
    public void render() {
        super.render();
        Field.render();
        player.render();
        renderPickedUpUnitType();
        renderPickUpCreature();
    }
}
