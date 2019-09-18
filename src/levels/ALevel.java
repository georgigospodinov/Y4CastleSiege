package levels;

import general.Field;
import levels.elements.Castle;
import levels.elements.Catapult;
import levels.elements.Defence;
import levels.elements.Question;
import screens.SiegeCompleted;
import units.Soldier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static main.Run.L;
import static main.Run.P;
import static main.ScreenManager.player;
import static main.ScreenManager.sm;

public abstract class ALevel {
    public final Castle castle = new Castle();
    public final ArrayList<Defence> defences = new ArrayList<>();
    private final Question question;
    public final Class<? extends ALevel> nextLevel;
    final LinkedHashMap<Class<? extends Soldier>, Integer> soldierAmounts = new LinkedHashMap<>();
    final LinkedHashMap<String, Integer> creatureAmounts = new LinkedHashMap<>();

    public boolean canTake(Class<? extends Soldier> soldierType) {
        Integer amount = soldierAmounts.get(soldierType);
        return amount != null && amount > 0;
    }

    public boolean canTake(String creatureName) {
        Integer amount = creatureAmounts.get(creatureName);
        return amount != null && amount >= 1;
    }

    public void takeOut(Class<? extends Soldier> soldierType) {
        int amount = soldierAmounts.get(soldierType);
        soldierAmounts.put(soldierType, amount - 1);
    }

    public void takeOut(String creatureName) {
        int amount = creatureAmounts.get(creatureName);
        creatureAmounts.put(creatureName, amount - 1);
    }

    public int getRemaining(Class<? extends Soldier> soldierType) {
        return soldierAmounts.getOrDefault(soldierType, 0);
    }

    public int getRemaining(String creatureName) {
        return creatureAmounts.getOrDefault(creatureName, 0);
    }

    public abstract Question createQuestion();

    public abstract void fillAvailable();

    public abstract void addDefences();

    ALevel(Class<? extends ALevel> nextLevel) {
        question = createQuestion();
        this.nextLevel = nextLevel;
        fillAvailable();
        Catapult leftCatapult = new Catapult(0f, castle.height + 1f);
        Catapult rightCatapult = new Catapult((float) (sm.getWidth() - leftCatapult.width), castle.height + 1f);
        defences.add(leftCatapult);
        defences.add(rightCatapult);
        addDefences();
    }

    ALevel() {
        this(null);
    }

    private void removeDestroyedDefenceAndReduceMoral() {
        for (Iterator<Defence> iterator = defences.iterator(); iterator.hasNext(); ) {
            Defence defence = iterator.next();
            if (!defence.isDestroyed()) continue;
            player.amendMoral(-defence.getMoralChange());
            iterator.remove();
        }
    }

    private void awardMoralForSparingUnits() {
        soldierAmounts.forEach((soldierClass, amountRemaining) -> {
            try {
                int moralPerUnit = (int) soldierClass.getField("MORAL_CHANGE").get(null);
                int total = moralPerUnit * amountRemaining;
                player.amendMoral(total);
            }
            catch (Exception e) {
                L.log(e);
            }
        });

        creatureAmounts.forEach((creature, amountRemaining) -> {
            int perUse = P.getInt("creature " + creature + " moral change");
            int total = perUse * amountRemaining;
            // Penalize not using spells.
            player.amendMoral(-total / 2);
        });

        defences.forEach(defence -> player.amendMoral(defence.getMoralChange() / 2));
    }

    public void render() {
        if (castle.isDestroyed()) {
            awardMoralForSparingUnits();
            Field.units.clear();
            Field.dead.clear();
            Field.castingSpell = null;  // End any spell being cast.
            sm.setScreen(new SiegeCompleted(question));
        }
        castle.render();
        removeDestroyedDefenceAndReduceMoral();
        defences.forEach(defence -> {
            defence.render();
            defence.renderHealth();
        });
    }
}
