package ru.xeroxp.launcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xButton {
    private static List buttons = new ArrayList();
    private int id;
    private String buttonName;
    private String image;
    private String pressedimage;
    private String disabledimage;
    private int imageX;
    private int imageY;
    private int imageSizeX;
    private int imageSizeY;
    private String actionlistener;
    private String keylistener;

   public xButton(int id, String buttonName, String image, String pressedimage, String disabledimage, int imageX, int imageY, int imageSizeX, int imageSizeY, String actionlistener, String keylistener) {
      this.id = id;
      this.buttonName = buttonName;
      this.image = image;
      this.pressedimage = pressedimage;
      this.disabledimage = disabledimage;
      this.imageX = imageX;
      this.imageY = imageY;
      this.imageSizeX = imageSizeX;
      this.imageSizeY = imageSizeY;
      this.actionlistener = actionlistener;
      this.keylistener = keylistener;
   }

   public int getId() {
      return this.id;
   }

   public String getButtonName() {
      return this.buttonName;
   }

   public String getImage() {
      return this.image;
   }

   public String getPressedImage() {
      return this.pressedimage;
   }

   public String getDisabledImage() {
      return this.disabledimage;
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
      return this.actionlistener;
   }
   
   public String getKeyListener() {
      return this.keylistener;
   }

   public static void loadButtons() {
      buttons.clear();
      for (int i = 0; i < xSettingsOfTheme.Buttons.length; i++) {
      xButton button = xSettingsOfTheme.Buttons[i];
      buttons.add(button);
      }
   }

   public static xButton getButton(int id) {
      Iterator var2 = buttons.iterator();

      while(var2.hasNext()) {
         xButton button = (xButton)var2.next();
         if(button.getId() == id) {
            return button;
         }
      }

      return null;
   }

   public static int getSize() {
      return buttons.size();
   }

   public static xButton[] getButtons() {
      int size = buttons.size();
      xButton[] buttonsList = new xButton[size];
      int i = 0;

      for(Iterator var4 = buttons.iterator(); var4.hasNext(); ++i) {
         xButton button = (xButton)var4.next();
         buttonsList[i] = button;
      }

      return buttonsList;
   }
}