package ru.xeroxp.launcher.gui;

import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.misc.xDebug;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class xErrorPanel extends JDialog {
    public xErrorPanel(Frame parent, String error) {
        super(parent);
        this.setTitle("Ошибка");
        this.setSize(xThemeSettings.ERROR_PANEL_SIZE[0], xThemeSettings.ERROR_PANEL_SIZE[1] + 20);
        this.setResizable(false);
        this.setModal(true);
        this.setLocationRelativeTo(parent);
        JPanel backgroundBg = new BackgroundRegisterPanel();
        backgroundBg.setLayout(null);
        backgroundBg.setBorder(null);
        backgroundBg.setOpaque(false);
        this.add(backgroundBg);
        InputStream is = xTheme.class.getResourceAsStream("/font/" + xThemeSettings.MAIN_FONT_FILE);
        Font arial = null;

        try {
            arial = Font.createFont(0, is);
            arial = arial.deriveFont(0, xThemeSettings.ERROR_PANEL_TEXT_SIZE);
        } catch (FontFormatException e2) {
            xDebug.errorMessage("Failed load font: " + e2.getMessage());
        } catch (IOException e2) {
            xDebug.errorMessage("Failed load font: " + e2.getMessage());
        }

        JLabel errorLabel = new JLabel(error);
        errorLabel.setBounds(0, 0, xThemeSettings.ERROR_PANEL_SIZE[0], xThemeSettings.ERROR_PANEL_SIZE[1]);
        errorLabel.setFont(arial);
        errorLabel.setForeground(xThemeSettings.ERROR_PANEL_TEXT_COLOR);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundBg.add(errorLabel);
    }

    public class BackgroundRegisterPanel extends JPanel {
        private Image img;
        private Image bgImage;

        public BackgroundRegisterPanel() {
            setOpaque(true);

            try {
                bgImage = ImageIO.read(xTheme.class.getResource("/images/" + xThemeSettings.ERROR_PANEL_IMAGE)).getScaledInstance(xThemeSettings.ERROR_PANEL_SIZE[0], xThemeSettings.ERROR_PANEL_SIZE[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            int w = this.getWidth();
            int h = this.getHeight();

            if ((img == null) || (img.getWidth(null) != w) || (img.getHeight(null) != h)) {
                img = this.createImage(w, h);

                Graphics g2 = img.getGraphics();
                for (int x = 0; x <= w / xThemeSettings.ERROR_PANEL_SIZE[0]; x++) {
                    for (int y = 0; y <= h / xThemeSettings.ERROR_PANEL_SIZE[1]; y++)
                        g2.drawImage(bgImage, x * xThemeSettings.ERROR_PANEL_SIZE[0], y * xThemeSettings.ERROR_PANEL_SIZE[1], null);
                }

                g2.dispose();
            }

            g.drawImage(img, 0, 0, w, h, null);
        }
    }
}