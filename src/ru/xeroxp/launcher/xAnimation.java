//class author dr.death
package ru.xeroxp.launcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class xAnimation implements ActionListener {

    private static boolean animationRunning = false;
    private final Component oldComp;
    private final Component newComp;
    private final Timer timer;
    private final int goodX;
    private final AnimationType anitype;

    public xAnimation(Component oldComp, Component newComp, int goodX, AnimationType type) {
        this.oldComp = oldComp;
        this.newComp = newComp;
        this.goodX = goodX;
        timer = new Timer(1 / 10, this);
        this.anitype = type;
    }

    @SuppressWarnings("static-access")
    public void start() {
        while (animationRunning) {
            try {
                Thread.currentThread().sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        animationRunning = true;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int oldX = oldComp.getX();
        int newX = newComp.getX();
        if (anitype == AnimationType.RIGHT_TO_LEFT_SLIDE) {
            oldX--;
            newX--;
        } else {
            oldX++;
            newX++;
        }
        if (newX != goodX) {
            oldComp.setBounds(oldX, oldComp.getY(), oldComp.getWidth(),
                    oldComp.getHeight());
            newComp.setBounds(newX, newComp.getY(), newComp.getWidth(),
                    newComp.getHeight());
        } else {
            animationRunning = false;
            timer.stop();
        }
    }

    public enum AnimationType {
        RIGHT_TO_LEFT_SLIDE, LEFT_TO_RIGHT_SLIDE
    }
}
