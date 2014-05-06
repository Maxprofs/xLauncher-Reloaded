package ru.xeroxp.launcher.gui.elements;

import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.misc.xDebug;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class xProgressBar {
    private final xTheme theme;
    private final JLabel text = new JLabel();
    private BufferedImage update;
    private BufferedImage bg;
    private BufferedImage image;
    private double onePercent;
    private boolean firstBg = true;

    public xProgressBar(xTheme theme) {
        try {
            this.update = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.PROGRESSBAR_IMAGES[0]));
            this.image = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.PROGRESSBAR_IMAGES[1]));
            this.bg = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.PROGRESSBAR_IMAGES[2]));
        } catch (IOException e) {
            xDebug.errorMessage("Failed load updater images: " + e.getMessage());
        }

        this.text.setBounds(xThemeSettings.PERCENT_LABEL_BOUNDS[0], xThemeSettings.PERCENT_LABEL_BOUNDS[1], xThemeSettings.PERCENT_LABEL_BOUNDS[2], xThemeSettings.PERCENT_LABEL_BOUNDS[3]);
        this.text.setForeground(xThemeSettings.PERCENT_LABEL_COLOR);

        this.theme = theme;
        this.theme.add(this.text);
    }

    public void init() {
        this.theme.setOpaque(true);
        this.theme.setBackground(new Color(0, 0, 0, 0));
        this.theme.revalidate();
        this.theme.repaint();
    }

    private double sizeToPercent(int size) {
        double percent = (double) size / this.onePercent;

        if (percent < 1.0D) {
            percent = 1.0D;
        } else if (percent > 100.0D) {
            percent = 100.0D;
        }

        return percent;
    }

    public void setOnePercent(int total) {
        this.onePercent = total / 100;
    }

    public void update() {
        this.update(100.0D);
    }

    public void update(int size) {
        this.update(this.sizeToPercent(size));
    }

    private void update(double percent) {
        this.theme.setOpaque(true);
        this.theme.setBackground(new Color(0, 0, 0, 0));

        Graphics g = this.theme.background.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));

        if (percent == 100.0D || percent == -1.0D) {
            g.drawImage(this.update, 42, 536, null);
        } else {
            if (this.firstBg) {
                g.drawImage(this.bg, 42, 536, null);
                this.firstBg = false;
            }

            g.drawImage(this.image.getSubimage(0, 0, (int) (6.62D * percent), 29), 42, 536, null);
            this.updateText((int) percent);
        }

        g.fillRect(42, 536, this.bg.getWidth(), this.bg.getHeight());
        g.dispose();

        this.theme.revalidate();
        this.theme.repaint();
    }

    private void updateText(int percent) {
        this.text.setText("Обновление " + percent + "%");
        this.text.setVisible(percent < 99);
    }
}
