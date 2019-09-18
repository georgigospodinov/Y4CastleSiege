package main;

import general.Player;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import screens.AScreen;
import screens.Menu;
import util.PrintFormatting;

import static main.Run.L;
import static main.Run.P;

//import screens.InventoryScreen;
//import screens.Menu;

public class ScreenManager extends PApplet {

    public static ScreenManager sm;
    public static Player player;

    private int width, height;
    private AScreen screen;

    public AScreen getScreen() {
        return screen;
    }

    public void setScreen(AScreen screen) {
        this.screen = screen;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PImage loadImageProp(String imageProperty) {
        String imageFileName;
        try {
            imageFileName = P.getString(imageProperty);
        }
        catch (NullPointerException npe) {
            L.log(npe);
            PrintFormatting.print("No image provided for " + imageProperty + ". Using default shape instead.");
            return null;
        }

        try {
            return sm.loadImage(imageFileName);
        }
        catch (NullPointerException npe) {
            L.log(npe);
            PrintFormatting.print("Failed to load \"" + imageFileName + "\". Using default shape instead.");
            return null;
        }
    }

    @Override
    public void settings() {
        sm = this;
        String fs = P.getString("full screen");
        if (fs.equals("true"))
            fullScreen();

        //<editor-fold desc="reused code from last practical">
        try {
            String w = P.getString("game width");
            switch (w) {
                case "displayWidth":
                    width = displayWidth;
                    break;
                case "displayHeight":
                    width = displayHeight;
                    break;
                default:
                    throw new Exception();
            }
        }
        catch (Exception e) {
            width = P.getInt("game width");
        }
        try {
            String h = P.getString("game height");
            switch (h) {
                case "displayHeight":
                    height = displayHeight;
                    break;
                case "displayWidth":
                    height = displayWidth;
                    break;
                default:
                    throw new Exception();
            }

            // Account for the top and bottom margin
            if (fs.equals("true"))
                height -= 50;
        }
        catch (Exception e) {
            height = P.getInt("game height");
        }

        if (!fs.equals("true"))
            size(width, height);
        //</editor-fold>

    }

    @Override
    public void draw() {
        if (player == null)
            player = new Player();
        if (screen == null)
            screen = new Menu();

        surface.setTitle("Castle Siege");
        screen.render();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getButton() == 37)
            screen.leftClick();
        else if (event.getButton() == 39)
            screen.rightClick();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        screen.press(event.getKeyCode());
    }
}
