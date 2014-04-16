package ru.xeroxp.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class xButton {
    private static final List buttons = new ArrayList();
    private final int id;
    private final String image;
    private final String pressedImage;
    private final String disabledImage;
    private final int imageX;
    private final int imageY;
    private final int imageSizeX;
    private final int imageSizeY;
    private final String actionListener;
    private final String keyListener;

    public xButton(int id, String image, String pressedImage, String disabledImage, int imageX, int imageY, int imageSizeX, int imageSizeY, String actionListener, String keyListener) {
        this.id = id;
        this.image = image;
        this.pressedImage = pressedImage;
        this.disabledImage = disabledImage;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageSizeX = imageSizeX;
        this.imageSizeY = imageSizeY;
        this.actionListener = actionListener;
        this.keyListener = keyListener;
    }

    public static void loadButtons() {
        buttons.clear();
        Collections.addAll(buttons, xSettingsOfTheme.Buttons);
    }

    public static xButton[] getButtons() {
        int size = buttons.size();
        xButton[] buttonsList = new xButton[size];
        int i = 0;

        for (Iterator var4 = buttons.iterator(); var4.hasNext(); ++i) {
            xButton button = (xButton) var4.next();
            buttonsList[i] = button;
        }

        return buttonsList;
    }

    public int getId() {
        return this.id;
    }

    public String getImage() {
        return this.image;
    }

    public String getPressedImage() {
        return this.pressedImage;
    }

    public String getDisabledImage() {
        return this.disabledImage;
    }

    public int getImageX() {
        return this.imageX;
    }

    public int getImageY() {
        return this.imageY;
    }

    public int getImageSizeX() {
        return this.imageSizeX;
    }

    public int getImageSizeY() {
        return this.imageSizeY;
    }

    public String getActionListener() {
        return this.actionListener;
    }

    public String getKeyListener() {
        return this.keyListener;
    }
}