package ru.xeroxp.launcher.gui.elements;

import ru.xeroxp.launcher.config.xThemeSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class xButton {
    public static final byte UPDATE_ID = 0;
    public static final byte AUTH_ID = 1;
    public static final byte RAM_ID = 2;

    private static final List<xButton> buttons = new ArrayList<xButton>();
    private final int id;
    private final String image;
    private final String pressedImage;
    private final String disabledImage;
    private final int imageX;
    private final int imageY;
    private final int imageSizeX;
    private final int imageSizeY;

    public xButton(int id, String image, String pressedImage, String disabledImage, int imageX, int imageY, int imageSizeX, int imageSizeY) {
        this.id = id;
        this.image = image;
        this.pressedImage = pressedImage;
        this.disabledImage = disabledImage;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageSizeX = imageSizeX;
        this.imageSizeY = imageSizeY;
    }

    public static void loadButtons() {
        buttons.clear();
        Collections.addAll(buttons, xThemeSettings.BUTTONS);
    }

    public static xButton[] getButtons() {
        int size = buttons.size();
        xButton[] buttonsList = new xButton[size];
        int i = 0;

        for (xButton button : buttons) {
            buttonsList[i] = button;
            ++i;
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
}