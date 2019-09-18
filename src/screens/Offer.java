package screens;

import magical.Creature;
import processing.core.PImage;
import screens.components.Button;
import screens.components.Message;

import static main.Run.P;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;
import static screens.SiegeCompleted.nextLevel;

public class Offer extends AScreen {
    private static final float OFFER_SIZE = P.getFloat("offer size") * sm.getHeight();
    private static final float DEAL_SIZE = P.getFloat("deal size") * sm.getHeight();
    private static final float OFFER_Y = P.getFloat("offer y") * sm.getHeight();
    private static final float ACCEPT_X = P.getFloat("accept x") * sm.getWidth();
    private static final float ACCEPT_Y = P.getFloat("accept y") * sm.getHeight();
    private static final float DENY_X = P.getFloat("deny x") * sm.getWidth();
    private static final float DENY_Y = P.getFloat("deny y") * sm.getHeight();
    private static final int MEET_HEIGHT = (int) (P.getFloat("meet height") * sm.getHeight());
    private static final int DENIAL_MORAL = P.getInt("denial moral");

    private final PImage creature;
    private final boolean side;

    public Offer(Creature creature, boolean side) {
        this.creature = creature.meet.copy();
        this.creature.resize(0, MEET_HEIGHT);
        this.side = side;
        Message offer = new Message(creature.offer, side ? 0 : this.creature.width, OFFER_Y, OFFER_SIZE);
        Button accept = new Button(creature.accept, ACCEPT_X, ACCEPT_Y, DEAL_SIZE);
        accept.onClick(() -> {
            player.addCreature(creature);
            nextLevel();
        });
        Button deny = new Button(creature.deny, DENY_X, DENY_Y, DEAL_SIZE);
        deny.onClick(() -> {
            player.amendMoral(side ? -DENIAL_MORAL : DENIAL_MORAL);
            nextLevel();
        });

        labels.add(offer);
        clickables.add(accept);
        clickables.add(deny);
    }

    @Override
    public void render() {
        super.render();
        float x = side ? sm.getWidth() - creature.width : 0;
        sm.image(creature, x, 0);
    }
}
