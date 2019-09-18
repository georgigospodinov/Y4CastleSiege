package main;

import processing.core.PApplet;
import processing.core.PImage;
import util.Logger;
import util.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;

import static main.ScreenManager.sm;
import static util.PrintFormatting.print;

public class Run {

    public static final Logger L = new Logger("log.txt");
    public static final Random R = new Random();
    public static final Props P = new Props();
    public static final int VISION_RANGE = 400;
    public static final String IMAGE_EXTENSION = ".png";

    public static PImage[] loadImages(String folderName) {
        File folder = new File(folderName);
        if (!folder.isDirectory())
            return null;
        File[] pngs = folder.listFiles((dir, name) -> name.endsWith(IMAGE_EXTENSION));
        if (pngs == null)
            return null;

        int size = pngs.length;
        PImage[] imgs = new PImage[size];
        for (int i = 0; i < size; i++) {
            imgs[i] = sm.loadImage(folderName + i + IMAGE_EXTENSION);
        }
        return imgs;
    }

    public static void main(String[] args) {
        try {
            P.load("assets/config/game.props");
            P.load("assets/config/screen.props");
        }
        catch (FileNotFoundException e) {
            L.log(e);
            L.close();
            print("Could not load a configuration file.", "See log.txt for more info.");
            return;
        }

        PApplet.main(new String[]{"main.ScreenManager"});
    }
}
