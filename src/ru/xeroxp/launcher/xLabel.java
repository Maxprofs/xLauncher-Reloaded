package ru.xeroxp.launcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xLabel {

   private static List labels = new ArrayList();
   private int id;
   private String labelName;
   private Color labelColor;
   private int labelX;
   private int labelY;
   private int labelSizeX;
   private int labelSizeY;
   private String labelLink;

   public xLabel(int id, String labelName, Color labelColor, int labelX, int labelY, int labelSizeX, int labelSizeY, String labelLink) {
      this.id = id;
      this.labelName = labelName;
      this.labelColor = labelColor;
      this.labelX = labelX;
      this.labelY = labelY;
      this.labelSizeX = labelSizeX;
      this.labelSizeY = labelSizeY;
      this.labelLink = labelLink;
   }

   public int getId() {
      return this.id;
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

   public static void loadLabels() {
      labels.clear();
      for (int i = 0; i < xSettingsOfTheme.Labels.length; i++) {
      xLabel label = xSettingsOfTheme.Labels[i];
      labels.add(label);
      }
   }
   
   public static xLabel getLabel(int id) {
      Iterator var2 = labels.iterator();

      while(var2.hasNext()) {
         xLabel label = (xLabel)var2.next();
         if(label.getId() == id) {
            return label;
         }
      }

      return null;
   }

   public static int getSize() {
      return labels.size();
   }

   public static xLabel[] getLabels() {
      int size = labels.size();
      xLabel[] labelList = new xLabel[size];
      int i = 0;

      for(Iterator var4 = labels.iterator(); var4.hasNext(); ++i) {
         xLabel label = (xLabel)var4.next();
         labelList[i] = label;
      }

      return labelList;
   }
}
