package ru.xeroxp.launcher;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xServer {

   private static List servers = new ArrayList();
   private int id;
   private String serverName;
   private String ip;
   private String port;
   private String folder;
   private String jar;
   private String version;
   private int online;
   private int imageX;
   private int imageY;
   private int imageSizeX;
   private int imageSizeY;
   private int iconX;
   private int iconY;
   private int iconSizeX;
   private int iconSizeY;
   private int titleX;
   private int titleY;
   private int titleSizeX;
   private int titleSizeY;
   private Color titleColor;
   private int onlineX;
   private int onlineY;
   private int onlineSizeX;
   private int onlineSizeY;
   private Color onlineColor;
   private int barX;
   private int barY;
   private double barSizeX;
   private int barSizeY;

   public xServer(int id, String serverName, String ip, String port, String folder, String jar, String version, int online, int imageX, int imageY, int imageSizeX, int imageSizeY, int iconX, int iconY, int iconSizeX, int iconSizeY, int titleX, int titleY, int titleSizeX, int titleSizeY, Color titleColor, int onlineX, int onlineY, int onlineSizeX, int onlineSizeY, Color onlineColor, int barX, int barY, double barSizeX, int barSizeY) {
      this.id = id;
      this.serverName = serverName;
      this.ip = ip;
      this.port = port;
      this.folder = folder;
      this.jar = jar;
      this.version = version;
      this.online = online;
      this.imageX = imageX;
      this.imageY = imageY;
      this.imageSizeX = imageSizeX;
      this.imageSizeY = imageSizeY;
      this.iconX = iconX;
      this.iconY = iconY;
      this.iconSizeX = iconSizeX;
      this.iconSizeY = iconSizeY;
      this.titleX = titleX;
      this.titleY = titleY;
      this.titleSizeX = titleSizeX;
      this.titleSizeY = titleSizeY;
      this.titleColor = titleColor;
      this.onlineX = onlineX;
      this.onlineY = onlineY;
      this.onlineSizeX = onlineSizeX;
      this.onlineSizeY = onlineSizeY;
      this.onlineColor = onlineColor;
      this.barX = barX;
      this.barY = barY;
      this.barSizeX = barSizeX;
      this.barSizeY = barSizeY;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.serverName;
   }

   public String getIp() {
      return this.ip;
   }

   public String getPort() {
      return this.port;
   }

   public String getFolder() {
      return this.folder;
   }
   
   public String getJar() {
      return this.jar;
   }
   
   public String getVersion() {
      return this.version;
   }

   public int getOnline() {
      return this.online;
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
   
   public int getIconX() {
      return this.iconX;
   }

   public int getIconY() {
      return this.iconY;
   }

   public int getIconSizeX() {
      return this.iconSizeX;
   }

   public int getIconSizeY() {
      return this.iconSizeY;
   }

   public int getTitleX() {
      return this.titleX;
   }

   public int getTitleY() {
      return this.titleY;
   }
   
   public int getTitleSizeX() {
      return this.titleSizeX;
   }

   public int getTitleSizeY() {
      return this.titleSizeY;
   }
   
   public Color getTitleColor() {
       return this.titleColor;
   }

   public int getOnlineX() {
      return this.onlineX;
   }

   public int getOnlineY() {
      return this.onlineY;
   }
   
   public int getOnlineSizeX() {
      return this.onlineSizeX;
   }

   public int getOnlineSizeY() {
      return this.onlineSizeY;
   }
   
   public Color getOnlineColor() {
       return this.onlineColor;
   }

   public int getBarX() {
      return this.barX;
   }

   public int getBarY() {
      return this.barY;
   }
   
   public double getBarSizeX() {
      return this.barSizeX;
   }

   public int getBarSizeY() {
      return this.barSizeY;
   }

   public static void loadServers() {
      servers.clear();
      for (int i = 0; i < xSettingsOfTheme.Servers.length; i++) {
      xServer server = xSettingsOfTheme.Servers[i];
      servers.add(server);
      }
   }

   public static xServer getServer(int id) {
      Iterator var2 = servers.iterator();

      while(var2.hasNext()) {
         xServer server = (xServer)var2.next();
         if(server.getId() == id) {
            return server;
         }
      }

      return null;
   }

   public static int getSize() {
      return servers.size();
   }

   public static xServer[] getServers() {
      int size = servers.size();
      xServer[] serverList = new xServer[size];
      int i = 0;

      for(Iterator var4 = servers.iterator(); var4.hasNext(); ++i) {
         xServer server = (xServer)var4.next();
         serverList[i] = server;
      }

      return serverList;
   }
}
