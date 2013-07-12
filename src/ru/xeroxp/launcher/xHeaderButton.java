package ru.xeroxp.launcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xHeaderButton {
    private static List buttons = new ArrayList();
    private int id;
    private String buttonName;
    private String image;
    private String onmouseimage;
    private int imageX;
    private int imageY;
    private int imageSizeX;
    private int imageSizeY;

   public xHeaderButton(int id, String buttonName, String image, String onmouseimage, int imageX, int imageY, int imageSizeX, int imageSizeY) {
      this.id = id;
      this.buttonName = buttonName;
      this.image = image;
      this.onmouseimage = onmouseimage;
      this.imageX = imageX;
      this.imageY = imageY;
      this.imageSizeX = imageSizeX;
      this.imageSizeY = imageSizeY;
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

   public static void loadButtons() {
      buttons.clear();
      for (int i = 0; i < xSettingsOfTheme.HeaderButtons.length; i++) {
      xHeaderButton button = xSettingsOfTheme.HeaderButtons[i];
      buttons.add(button);
      }
   }

   public static xHeaderButton getButton(int id) {
      Iterator var2 = buttons.iterator();

      while(var2.hasNext()) {
         xHeaderButton button = (xHeaderButton)var2.next();
         if(button.getId() == id) {
            return button;
         }
      }

      return null;
   }

   public static int getSize() {
      return buttons.size();
   }

   public static xHeaderButton[] getButtons() {
      int size = buttons.size();
      xHeaderButton[] buttonsList = new xHeaderButton[size];
      int i = 0;

      for(Iterator var4 = buttons.iterator(); var4.hasNext(); ++i) {
         xHeaderButton button = (xHeaderButton)var4.next();
         buttonsList[i] = button;
      }

      return buttonsList;
   }
}