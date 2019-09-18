package main;

import java.util.Iterator;
import java.util.LinkedHashSet;

import static util.PrintFormatting.print;

public class Test {

    public static void main(String[] args) {
        LinkedHashSet<Integer> lll = new LinkedHashSet<>();
        lll.add(10);
        lll.add(18);
        lll.add(4);
        for (Iterator<Integer> iterator = lll.iterator(); iterator.hasNext(); ) {
            Integer integer = iterator.next();
            if (integer == 10)
                iterator.remove();
        }
        print(lll);
    }
}
