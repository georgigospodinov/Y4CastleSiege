package magical;

import general.Field;
import processing.core.PImage;

import static main.Run.*;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;

public class Creature {
    public final int MORAL_CHANGE;
    public final PImage meet, cast;
    public final String offer, accept, deny, name;
    public final Class<? extends Spell> spell;

    public Creature(String name, String offer, String accept, String deny, Class<? extends Spell> spell) {
        this.offer = offer;
        this.accept = accept;
        this.deny = deny;
        this.name = name;

        String folderName = P.getString("creature " + name);
        meet = sm.loadImage(folderName + "meet" + IMAGE_EXTENSION);
        cast = sm.loadImage(folderName + "cast" + IMAGE_EXTENSION);
        this.spell = spell;
        MORAL_CHANGE = P.getInt("creature " + name + " moral change");
    }

    public void cast() {
        try {
            Field.castingSpell = spell.getConstructor(PImage.class).newInstance(cast);
            // Award using spells
            player.amendMoral(MORAL_CHANGE);
        }
        catch (Exception e) {
            L.log(e);
        }
    }
}
