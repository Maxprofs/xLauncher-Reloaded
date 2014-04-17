package ru.xeroxp.launcher.gui;

import ru.xeroxp.launcher.config.xSettingsOfTheme;
import ru.xeroxp.launcher.utils.xDebug;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class xErrorPanel extends JDialog {
    public xErrorPanel(Frame parent, String error) {
        super(parent);
        setTitle("Ошибка");
        setSize(xSettingsOfTheme.ERROR_PANEL_SIZE[0], xSettingsOfTheme.ERROR_PANEL_SIZE[1] + 20);
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(parent);
        JPanel backgroundBg = new BackgroundRegisterPanel();
        backgroundBg.setLayout(null);
        backgroundBg.setBorder(null);
        backgroundBg.setOpaque(false);
        add(backgroundBg);
        InputStream is = xTheme.class.getResourceAsStream("/font/" + xSettingsOfTheme.MAIN_FONT_FILE);
        Font arial = null;

        try {
            arial = Font.createFont(0, is);
            arial = arial.deriveFont(0, xSettingsOfTheme.ERROR_PANEL_TEXT_SIZE);
        } catch (FontFormatException e2) {
            xDebug.errorMessage("Failed load font: " + e2.getMessage());
        } catch (IOException e2) {
            xDebug.errorMessage("Failed load font: " + e2.getMessage());
        }

        JLabel errorLabel = new JLabel(error);
        errorLabel.setBounds(0, 0, xSettingsOfTheme.ERROR_PANEL_SIZE[0], xSettingsOfTheme.ERROR_PANEL_SIZE[1]);
        errorLabel.setFont(arial);
        errorLabel.setForeground(xSettingsOfTheme.ERROR_PANEL_TEXT_COLOR);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundBg.add(errorLabel);
    }

    public class BackgroundRegisterPanel extends JPanel {
        private Image img;
        private Image bgImage;

        public BackgroundRegisterPanel() {
            setOpaque(true);

            try {
                bgImage = ImageIO.read(xTheme.class.getResource("/images/" + xSettingsOfTheme.ERROR_PANEL_IMAGE)).getScaledInstance(xSettingsOfTheme.ERROR_PANEL_SIZE[0], xSettingsOfTheme.ERROR_PANEL_SIZE[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paintComponent(Graphics g2) {
            int w = getWidth();
            int h = getHeight();

            if ((img == null) || (img.getWidth(null) != w) || (img.getHeight(null) != h)) {
                img = createImage(w, h);

                Graphics g = img.getGraphics();
                for (int x = 0; x <= w / xSettingsOfTheme.ERROR_PANEL_SIZE[0]; x++) {
                    for (int y = 0; y <= h / xSettingsOfTheme.ERROR_PANEL_SIZE[1]; y++)
                        g.drawImage(bgImage, x * xSettingsOfTheme.ERROR_PANEL_SIZE[0], y * xSettingsOfTheme.ERROR_PANEL_SIZE[1], null);
                }

                g.dispose();
            }

            g2.drawImage(img, 0, 0, w, h, null);
        }
    }
}