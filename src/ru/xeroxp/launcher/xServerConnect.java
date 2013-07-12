package ru.xeroxp.launcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class xServerConnect {
    
   private static List connectServers = new ArrayList();
   private int id;
   private String serverIp;
   private int serverPort;
   
    public xServerConnect(int id, String serverIp, int serverPort) {
      this.id = id;
      this.serverIp = serverIp;
      this.serverPort = serverPort;
   }

   public int getId() {
      return this.id;
   }

   public String getServerIp() {
      return this.serverIp;
   }

   public int getServerPort() {
      return this.serverPort;
   }

   public static void loadServers() {
      connectServers.clear();
      for (int i = 0; i < xSettings.connectServers.length; i++) {
      xServerConnect server = xSettings.connectServers[i];
      connectServers.add(server);
      }
   }

   public static xServerConnect getConnectServer(int id) {
      Iterator var2 = connectServers.iterator();

      while(var2.hasNext()) {
         xServerConnect server = (xServerConnect)var2.next();
         if(server.getId() == id) {
            return server;
         }
      }

      return null;
   }

   public static int getSize() {
      return connectServers.size();
   }

   public static xServerConnect[] getConnectServers() {
      int size = connectServers.size();
      xServerConnect[] serverList = new xServerConnect[size];
      int i = 0;

      for(Iterator var4 = connectServers.iterator(); var4.hasNext(); ++i) {
         xServerConnect server = (xServerConnect)var4.next();
         serverList[i] = server;
      }

      return serverList;
   }
}
