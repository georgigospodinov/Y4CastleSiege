package screens;

import general.Field;
import general.Player;
import levels.Level1;
import levels.elements.Question;
import magical.Creature;
import magical.CreatureManager;
import screens.components.Button;
import screens.components.Message;

import static main.Run.L;
import static main.Run.P;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;

public class SiegeCompleted extends AScreen {
    private static final float QUESTION_SIZE = P.getFloat("question size") * sm.getHeight();
    private static final float ANSWER_SIZE = P.getFloat("answer size") * sm.getHeight();
    private static final float QUESTION_X = P.getFloat("question x") * sm.getWidth();
    private static final float QUESTION_Y = P.getFloat("question y") * sm.getHeight();
    private static final float POSITIVE_X = P.getFloat("positive x") * sm.getWidth();
    private static final float POSITIVE_Y = P.getFloat("positive y") * sm.getHeight();
    private static final float NEGATIVE_X = P.getFloat("negative x") * sm.getWidth();
    private static final float NEGATIVE_Y = P.getFloat("negative y") * sm.getHeight();

    public static void nextLevel() {
        try {
            Field.level = Field.level.nextLevel.getConstructor().newInstance();
        }
        catch (Exception e) {
            L.log(e);
        }
        sm.setScreen(new Play());
    }

    private void evaluateMoral() {
        if (Field.level.nextLevel == null) {
            Field.level = new Level1();
            sm.setScreen(new GameOver());
            return;
        }

        float roll = sm.random(Player.MAX_MORAL);
        int moral = player.getMoral();
        Creature c = null;
        Boolean side = null;
        if (moral > 0 && roll < moral) {
            c = CreatureManager.getNextGood();
            side = true;
        }
        else if (moral < 0 && roll < -moral) {
            c = CreatureManager.getNextEvil();
            side = false;
        }

        if (c != null) {
            sm.setScreen(new Offer(c, side));
            return;
        }

        nextLevel();
    }

    public SiegeCompleted(Question question) {
        Message q = new Message("CASTLE CONQUERED!", QUESTION_X, QUESTION_Y, QUESTION_SIZE);
        q.addLine(question.question);
        Button p = new Button(question.positiveAnswer, POSITIVE_X, POSITIVE_Y, ANSWER_SIZE);
        p.onClick(() -> {
            player.amendMoral(question.value);
            evaluateMoral();
        });
        Button n = new Button(question.negativeAnswer, NEGATIVE_X, NEGATIVE_Y, ANSWER_SIZE);
        n.onClick(() -> {
            player.amendMoral(-question.value);
            evaluateMoral();
        });
        labels.add(q);
        clickables.add(p);
        clickables.add(n);
    }
}
