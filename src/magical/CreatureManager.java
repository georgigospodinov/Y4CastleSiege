package magical;

import java.util.LinkedList;

public class CreatureManager {
    private static final LinkedList<Creature> GOOD = new LinkedList<>(), EVIL = new LinkedList<>();

    static {
        Creature priest = new Creature("priest",
                "Greetings, liberator!\nI am here to offer my magical aid.\n" +
                        "Call upon me in battle,\nand I will make your soldiers faster for limited time.",
                "Thank you so much, priest! We could sure use some help.",
                "Go away, foolish one! My soldiers need none of that.",
                Haste.class);
        Creature angel = new Creature("angel",
                "Greetings, liberator!\nI wish to lend you some of my divine power.\n" +
                        "Turn to me in a dire situation,\nand I will heal your soldiers.",
                "Thank you, your holiness! My soldiers and I are most grateful.",
                "I would never tie myself to an angel!",
                Heal.class);
        GOOD.addLast(priest);
        GOOD.addLast(angel);

        Creature warlock = new Creature("warlock",
                "Well, hello, Conqueror.\nI have knack for the arcane arts, as you might guess.\n" +
                        "Just point me to your opponents.\nI will make them lethargically slow. Temporarily, at least.",
                "You're hired! That will provide a needed advantage.",
                "I do not associate with warlocks!",
                Slow.class);
        Creature necromancer = new Creature("necromancer",
                "Conqueror! Dead soldiers...\nare more useful than you can imagine!\n" +
                        "I can turn the tides of a battle,\nwhen your enemies least expect it.",
                "Sounds reasonable. Be ready for my signal.",
                "Get out of my sight, despicable creature!",
                Reanimate.class);
        EVIL.addLast(warlock);
        EVIL.addLast(necromancer);
    }

    public static Creature getNextGood() {
        return GOOD.pollFirst();
    }

    public static Creature getNextEvil() {
        return EVIL.pollFirst();
    }
}
