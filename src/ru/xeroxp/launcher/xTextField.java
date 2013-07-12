package ru.xeroxp.launcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xTextField {
   private static List fields = new ArrayList();
   private int id;
   private String fieldName;
   private Color fieldColor;
   private int fieldLimit;
   private int fieldX;
   private int fieldY;
   private int fieldSizeX;
   private int fieldSizeY;
   private String image;

   public xTextField(int id, String fieldName, Color fieldColor, int fieldLimit, int fieldX, int fieldY, int fieldSizeX, int fieldSizeY, String image) {
      this.id = id;
      this.fieldName = fieldName;
      this.fieldColor = fieldColor;
      this.fieldLimit = fieldLimit;
      this.fieldX = fieldX;
      this.fieldY = fieldY;
      this.fieldSizeX = fieldSizeX;
      this.fieldSizeY = fieldSizeY;
      this.image = image;
   }

   public int getId() {
      return this.id;
   }
   
   public String getFieldName() {
      return this.fieldName;
   }

   public Color getFieldColor() {
      return this.fieldColor;
   }

   public int getFieldLimit() {
      return this.fieldLimit;
   }
   
   public int getFieldX() {
      return this.fieldX;
   }

   public int getFieldY() {
      return this.fieldY;
   }

   public int getFieldSizeX() {
      return this.fieldSizeX;
   }

   public int getFieldSizeY() {
      return this.fieldSizeY;
   }

   public String getImage() {
      return this.image;
   }

   public static void loadFields() {
      fields.clear();
      for (int i = 0; i < xSettingsOfTheme.Fields.length; i++) {
      xTextField field = xSettingsOfTheme.Fields[i];
      fields.add(field);
      }
   }
   
   public static xTextField getField(int id) {
      Iterator var2 = fields.iterator();

      while(var2.hasNext()) {
         xTextField field = (xTextField)var2.next();
         if(field.getId() == id) {
            return field;
         }
      }

      return null;
   }

   public static int getSize() {
      return fields.size();
   }

   public static xTextField[] getFields() {
      int size = fields.size();
      xTextField[] fieldList = new xTextField[size];
      int i = 0;

      for(Iterator var4 = fields.iterator(); var4.hasNext(); ++i) {
         xTextField field = (xTextField)var4.next();
         fieldList[i] = field;
      }

      return fieldList;
   }
}