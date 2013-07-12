package ru.xeroxp.launcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xCheckbox {
    private static List checkboxes = new ArrayList();
    private int id;
    private String checkboxLabel;
    private int labelX;
    private int labelY;
    private int labelSizeX;
    private int labelSizeY;
    private Color labelColor;
    private String image;
    private String selectedimage;
    private int imageX;
    private int imageY;
    private int imageSizeX;
    private int imageSizeY;
    private String itemlistener;

   public xCheckbox(int id, String checkboxLabel, int labelX, int labelY, int labelSizeX, int labelSizeY, Color labelColor, String image, String selectedimage, int imageX, int imageY, int imageSizeX, int imageSizeY, String itemlistener) {
      this.id = id;
      this.checkboxLabel = checkboxLabel;
      this.labelX = labelX;
      this.labelY = labelY;
      this.labelSizeX = labelSizeX;
      this.labelSizeY = labelSizeY;
      this.labelColor = labelColor;
      this.image = image;
      this.selectedimage = selectedimage;
      this.imageX = imageX;
      this.imageY = imageY;
      this.imageSizeX = imageSizeX;
      this.imageSizeY = imageSizeY;
      this.itemlistener = itemlistener;
   }

   public int getId() {
      return this.id;
   }

   public String getCheckboxLabel() {
      return this.checkboxLabel;
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
   
   public Color getLabelColor() {
       return this.labelColor;
   }

   public String getImage() {
      return this.image;
   }

   public String getSelectedImage() {
      return this.selectedimage;
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
   
   public String getItemListener() {
      return this.itemlistener;
   }

   public static void loadCheckboxes() {
      checkboxes.clear();
      for (int i = 0; i < xSettingsOfTheme.Checkboxes.length; i++) {
      xCheckbox checkbox = xSettingsOfTheme.Checkboxes[i];
      checkboxes.add(checkbox);
      }
   }

   public static xCheckbox getCheckbox(int id) {
      Iterator var2 = checkboxes.iterator();

      while(var2.hasNext()) {
         xCheckbox checkbox = (xCheckbox)var2.next();
         if(checkbox.getId() == id) {
            return checkbox;
         }
      }

      return null;
   }

   public static int getSize() {
      return checkboxes.size();
   }

   public static xCheckbox[] getCheckboxes() {
      int size = checkboxes.size();
      xCheckbox[] checkboxesList = new xCheckbox[size];
      int i = 0;

      for(Iterator var4 = checkboxes.iterator(); var4.hasNext(); ++i) {
         xCheckbox checkbox = (xCheckbox)var4.next();
         checkboxesList[i] = checkbox;
      }

      return checkboxesList;
   }
}