package ru.xeroxp.launcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class xErrorPanel extends JDialog
{
  private static final long serialVersionUID = 1L;
  
  private JPanel BackgroundBg = new BackgroundRegisterPanel();
  private JLabel errorlabel;
  private Font arial = null;
  
  public xErrorPanel(Frame parent, String error)
  {
    super(parent);
    setTitle("Ошибка");
    setSize(xSettingsOfTheme.ErrorPanelSize[0], xSettingsOfTheme.ErrorPanelSize[1] + 20);
    setResizable(false);
    setModal(true);
    setLocationRelativeTo(parent);
    BackgroundBg.setLayout(null);
    BackgroundBg.setBorder(null);
    BackgroundBg.setOpaque(false);
    add(BackgroundBg);
    InputStream is = xTheme.class.getResourceAsStream("/font/" + xSettingsOfTheme.FontFile1);
    try
    {
        this.arial = Font.createFont(0, is);
        this.arial = this.arial.deriveFont(0, xSettingsOfTheme.ErrorPanelTextSize);
    } catch (FontFormatException e2) {
        System.out.println("Failed load font");
        System.out.println(e2.getMessage());
    } catch (IOException e2) {
        System.out.println("Failed load font");
        System.out.println(e2.getMessage());
    }
    errorlabel = new JLabel(error);
    errorlabel.setBounds(0, 0, xSettingsOfTheme.ErrorPanelSize[0], xSettingsOfTheme.ErrorPanelSize[1]);
    errorlabel.setFont(arial);
    errorlabel.setForeground(xSettingsOfTheme.ErrorPanelTextColor);
    errorlabel.setHorizontalAlignment( JLabel.CENTER );
    BackgroundBg.add(errorlabel);
  }
  
public class BackgroundRegisterPanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  private Image img;
  private Image bgImage;

  public BackgroundRegisterPanel()
  {
    setOpaque(true);
    try
    {
      bgImage = ImageIO.read(xTheme.class.getResource("/images/" + xSettingsOfTheme.ErrorPanelImage)).getScaledInstance(xSettingsOfTheme.ErrorPanelSize[0], xSettingsOfTheme.ErrorPanelSize[1], 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paintComponent(Graphics g2) {
    int w = getWidth();
    int h = getHeight();
    if ((img == null) || (img.getWidth(null) != w) || (img.getHeight(null) != h)) {
      img = createImage(w, h);

      Graphics g = img.getGraphics();
      for (int x = 0; x <= w / xSettingsOfTheme.ErrorPanelSize[0]; x++) {
        for (int y = 0; y <= h / xSettingsOfTheme.ErrorPanelSize[1]; y++)
          g.drawImage(bgImage, x * xSettingsOfTheme.ErrorPanelSize[0], y * xSettingsOfTheme.ErrorPanelSize[1], null);
      }
      if ((g instanceof Graphics2D)) {
        Graphics2D gg = (Graphics2D)g;

      }
      g.dispose();
    }
    g2.drawImage(img, 0, 0, w, h, null);
  }
}
}