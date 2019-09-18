package screens;

import processing.core.PImage;
import screens.components.Clickable;
import screens.components.Label;

import java.util.HashSet;

import static main.Run.P;
import static main.ScreenManager.sm;
import static processing.core.PConstants.ARROW;
import static processing.core.PConstants.HAND;

public abstract class AScreen {

    static final float DETAILS_SIZE = sm.getHeight() * P.getFloat("details size");
    static final float DETAILS_X = sm.getWidth() * P.getFloat("details x");
    static final float DETAILS_Y = sm.getHeight() * P.getFloat("details y");
    static final float BACK_SIZE = sm.getHeight() * P.getFloat("back size");
    static final float BACK_X = sm.getWidth() * P.getFloat("back x");
    static final float BACK_Y = sm.getHeight() * P.getFloat("back y");

    protected static final boolean STRETCH = true, TILE = false;

    public static final float COORDINATE_FIX = 3;
    public static final int DEFAULT_COLOR;

    static {
        int red = P.getInt("default color red");
        int green = P.getInt("default color green");
        int blue = P.getInt("default color blue");
        DEFAULT_COLOR = sm.color(red, green, blue);
    }

    protected final boolean bgType;
    protected final PImage bgImage;
    protected HashSet<Clickable> clickables = new HashSet<>();
    protected HashSet<Label> labels = new HashSet<>();

    private AScreen(PImage bgImage, boolean bgType) {
        this.bgType = bgType;
        this.bgImage = bgImage;
    }

    /**
     * Creates an {@link AScreen}. The given {@link PImage} image is treated as a tile.
     * The image resized to the given width and height.
     * It is drawn as many times as needed to cover the screen.
     *
     * @param bgImage tile to repeatedly draw
     * @param w       tile width
     * @param h       tile height
     */
    public AScreen(PImage bgImage, int w, int h) {
        this(bgImage, TILE);
        this.bgImage.resize(w, h);
    }

    /**
     * Creates an {@link AScreen}. The given {@link PImage} image is treated as a full background image.
     * The image is resized to the dimensions of the screen.
     *
     * @param bgImage image to stretch on the screen
     */
    public AScreen(PImage bgImage) {
        this(bgImage, STRETCH);
        this.bgImage.resize(sm.getWidth(), sm.getHeight());
    }

    public AScreen() {
        this(null, STRETCH);
    }

    protected void transformCursor() {
        for (Clickable c : clickables) {
            if (c.within(sm.mouseX, sm.mouseY)) {
                sm.cursor(HAND);
                return;
            }
        }
        sm.cursor(ARROW);
    }

    public void leftClick() {
        for (Clickable c : clickables) {
            if (c.within(sm.mouseX, sm.mouseY)) {
                c.click();
                return;
            }
        }
    }

    public void rightClick() {
    }

    public void press(int keyCode) {
    }

    private void renderBackground() {
        if (bgImage == null) {
            sm.background(DEFAULT_COLOR);
            return;
        }

        if (bgType == STRETCH) {
            sm.background(bgImage);
            return;
        }

        int r = sm.getHeight() / bgImage.height + 1;
        int c = sm.getWidth() / bgImage.width + 1;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                sm.image(bgImage, j * bgImage.width, i * bgImage.height);
            }
        }
    }

    public void render() {
        renderBackground();
        clickables.forEach(Clickable::render);
        labels.forEach(Label::render);
        transformCursor();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
