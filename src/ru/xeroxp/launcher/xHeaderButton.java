package ru.xeroxp.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class xHeaderButton {
    private static final List buttons = new ArrayList();
    private final String buttonName;
    private final String image;
    private final String onmouseimage;
    private final int imageX;
    private final int imageY;
    private final int imageSizeX;
    private final int imageSizeY;

    public xHeaderButton(String buttonName, String image, String onmouseimage, int imageX, int imageY, int imageSizeX, int imageSizeY) {
        this.buttonName = buttonName;
        this.image = image;
        this.onmouseimage = onmouseimage;
        this.imageX = imageX;
        this.imageY = imageY;
        this.imageSizeX = imageSizeX;
        this.imageSizeY = imageSizeY;
    }

    public static void loadButtons() {
        buttons.clear();
        Collections.addAll(buttons, xSettingsOfTheme.HeaderButtons);
    }

    public static xHeaderButton[] getButtons() {
        int size = buttons.size();
        xHeaderButton[] buttonsList = new xHeaderButton[size];
        int i = 0;

        for (Iterator var4 = buttons.iterator(); var4.hasNext(); ++i) {
            xHeaderButton button = (xHeaderButton) var4.next();
            buttonsList[i] = button;
        }

        return buttonsList;
    }

    public String getButtonName() {
        return this.buttonName;
    }

    public String getImage() {
        return this.image;
    }

    public String getOnMouseImage() {
        return this.onmouseimage;
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