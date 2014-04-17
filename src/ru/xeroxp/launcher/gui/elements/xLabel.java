package ru.xeroxp.launcher.gui.elements;

import ru.xeroxp.launcher.config.xThemeSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class xLabel {
    private static final List<xLabel> labels = new ArrayList<xLabel>();
    private final String labelName;
    private final Color labelColor;
    private final int labelX;
    private final int labelY;
    private final int labelSizeX;
    private final int labelSizeY;
    private final String labelLink;

    public xLabel(String labelName, Color labelColor, int labelX, int labelY, int labelSizeX, int labelSizeY, String labelLink) {
        this.labelName = labelName;
        this.labelColor = labelColor;
        this.labelX = labelX;
        this.labelY = labelY;
        this.labelSizeX = labelSizeX;
        this.labelSizeY = labelSizeY;
        this.labelLink = labelLink;
    }

    public static void loadLabels() {
        labels.clear();
        Collections.addAll(labels, xThemeSettings.LABELS);
    }

    public static xLabel[] getLabels() {
        int size = labels.size();
        xLabel[] labelList = new xLabel[size];
        int i = 0;

        for (Iterator var4 = labels.iterator(); var4.hasNext(); ++i) {
            xLabel label = (xLabel) var4.next();
            labelList[i] = label;
        }

        return labelList;
    }

    public String getName() {
        return this.labelName;
    }

    public Color getColor() {
        return this.labelColor;
    }

    public int getLabelX() {
        return this.labelX;
    }

    public int getLabelY() {
        return this.labelY;
    }

    public int getLabelSizeX() {
        return this.labelSizeX;
    }

    public int getLabelSizeY() {
        return this.labelSizeY;
    }

    public String getLabelLink() {
        return this.labelLink;
    }
}
