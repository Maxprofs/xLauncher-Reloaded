package ru.xeroxp.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

public class xUpdater 
{
   private BufferedImage update;
   private BufferedImage bg;
   private BufferedImage image;
   private BufferedImage background;
   private xTheme theme;
   private int totalDownload;
   private double onePercent;
   private boolean firstBg = true;
   private xUtils utils = new xUtils();

   public xUpdater(xTheme theme, boolean n) {
      try {
         this.update = ImageIO.read(xUpdater.class.getResource("/images/updatebar_ok.png"));
         this.image = ImageIO.read(xUpdater.class.getResource("/images/updatebar.png"));
         this.bg = ImageIO.read(xUpdater.class.getResource("/images/updatebar_bg.png"));
      } catch (IOException var3) {
         System.out.println("Failed load updater images");
         System.out.println(var3.getMessage());
      }

      this.theme = theme;
      this.theme.setOpaque(true);
      this.theme.setBackground(new Color(0, 0, 0, 0));
      this.background = theme.background;
      this.theme.revalidate();
      this.theme.repaint();
      this.checkLauncherUpdate();
      this.checkClientUpdate(n);
   }

   private void checkLauncherUpdate() {
      try {
         String e = this.checkLauncherVersion();
         if(e != null) {
            String getVersion = xMain.getVersion();
            if(!e.equals(getVersion)) {
               this.theme.lockAuth(true);
               this.updateLauncher(e);
            }
            this.updateDownload();
            this.theme.lockAuth(false);
         }
      } catch (Exception var3) {
         System.out.println("Failed check launcher update");
         System.out.println(var3.getMessage());
      }
   }

   private void updateLauncher(String checkVersion) {
      xButton.loadButtons();
      for(int i = 0; i < xSettingsOfTheme.Buttons.length; ++i) {
          xButton button = xButton.getButtons()[i];
          if (button.getActionListener().equals("UL")){
              this.theme.buttons[button.getId()].setEnabled(false);
          }
          if (button.getActionListener().equals("RML")){
              this.theme.buttons[button.getId()].setEnabled(false);
          }
      }
      File runningLauncher = null;
      try {
         runningLauncher = new File(xUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      } catch (URISyntaxException var5) {
         System.out.println("Failed to find launcher path");
         System.out.println(var5.getMessage());
      }

      try {
         if(runningLauncher.getPath().endsWith(".jar")) {
            this.unpackLauncher(new URL(xSettings.downLauncherLink + "xLauncher.jar"), runningLauncher);
         } else if(runningLauncher.getPath().endsWith(".exe")) {
            this.unpackLauncher(new URL(xSettings.downLauncherLink + "xLauncher.exe"), runningLauncher);
         }
         xMain.setVersion(checkVersion);
         xMain.restart();
      } catch (IOException var4) {
         System.out.println("Failed update launcher");
         System.out.println(var4.getMessage());
      }
   }

   public void updateDownload() {
      this.theme.setOpaque(true);
      this.theme.setBackground(new Color(0, 0, 0, 0));
      Graphics g2 = this.background.getGraphics();
      g2.setColor(new Color(0, 0, 0, 0));
      g2.drawImage(this.update, 42, 536, (ImageObserver)null);
      g2.fillRect(42, 536, this.bg.getWidth(), this.bg.getHeight());
      g2.dispose();
      this.theme.revalidate();
      this.theme.repaint();
   }

   public void checkUpdateBar(int size) {
      double percent = (double)size / this.onePercent;
      if(percent < 1.0D) {
         percent = 1.0D;
      }
      if(percent > 100.0D) {
         percent = 100.0D;
      }
      this.updateDownload(percent);
   }

   public void updateDownload(double done) {
      this.theme.setOpaque(true);
      this.theme.setBackground(new Color(0, 0, 0, 0));
      Graphics g2 = this.background.getGraphics();
      g2.setColor(new Color(0, 0, 0, 0));
      if(this.firstBg) {
         g2.drawImage(this.bg, 42, 536, (ImageObserver)null);
         this.firstBg = false;
      }
      if(done == 100.0D) {
         g2.drawImage(this.update, 42, 536, (ImageObserver)null);
      } else {
         g2.drawImage(this.image.getSubimage(0, 0, (int)(6.62D * done), 29), 42, 536, (ImageObserver)null);
         this.theme.updatePercent((int)done);
      }
      g2.fillRect(42, 536, this.bg.getWidth(), this.bg.getHeight());
      g2.dispose();
      this.theme.revalidate();
      this.theme.repaint();
   }

   public void updateClient(String version) {
      xButton.loadButtons();
      for(int i = 0; i < xSettingsOfTheme.Buttons.length; ++i) {
          xButton button = xButton.getButtons()[i];
          if (button.getActionListener().equals("UL")){
              this.theme.buttons[button.getId()].setEnabled(false);
          }
          if (button.getActionListener().equals("RML")){
              this.theme.buttons[button.getId()].setEnabled(false);
          }
      }
      try {
         this.utils.deleteFiles();
      } catch (IOException var5) {
         System.out.println("Failed delete client");
         System.out.println(var5.getMessage());
         this.theme.setError("Не удалось очистить клиент");
         return;
      }
      try {
         this.unpackClient(new URL(xSettings.downClientLink + "client.zip"), this.utils.getDirectory());
      } catch (IOException var4) {
         System.out.println("Failed unpack client");
         System.out.println(var4.getMessage());
      }
      try {
         this.updateVersion(version);
      } catch (Exception var3) {
         System.out.println("Failed update client version");
         System.out.println(var3.getMessage());
      }
      for(int i = 0; i < xSettingsOfTheme.Buttons.length; ++i) {
          xButton button = xButton.getButtons()[i];
          if (button.getActionListener().equals("RML")){
              this.theme.buttons[button.getId()].setEnabled(true);
              break;
          }
      }
   }

   public String checkLauncherVersion() {
      try {
         URL e = new URL(xSettings.maininfofile + "?action=launcher");
         URLConnection getVer = e.openConnection();
         BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
         String inputLine = in.readLine();
         in.close();
         return inputLine;
      } catch (Exception var5) {
         System.out.println("Failed check launcher version");
         System.out.println(var5.getMessage());
         return null;
      }
   }

   public String checkClientVersion() {
      try {
         URL e = new URL(xSettings.maininfofile + "?action=version");
         URLConnection getVer = e.openConnection();
         BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
         String inputLine = in.readLine();
         in.close();
         return inputLine;
      } catch (Exception var5) {
         System.out.println("Failed check client version");
         System.out.println(var5.getMessage());
         return null;
      }
   }

   public void checkClientUpdate(boolean n) {
      try {
         String e = this.checkClientVersion();
         if(e != null) {
            String getVersion;
            if (!n){
            getVersion = this.getVersion();
            } else {
            getVersion = "0";
            }
            if(getVersion == null) {
               this.theme.lockAuth(true);
               this.updateClient(e);
               this.theme.lockAuth(false);
               return;
            }
            if(!e.equals(getVersion)) {
               this.theme.lockAuth(true);
               this.updateClient(e);
            }
            this.updateDownload();
            this.theme.lockAuth(false);
         }
      } catch (Exception var3) {
         System.out.println("Failed check client update");
         System.out.println(var3.getMessage());
      }
   }

   public String getVersion() throws Exception {
      File dir = this.utils.getDirectory();
      if(!dir.exists()) {
         dir.mkdirs();
      }
      File versionFile = new File(dir, "version");
      if(!versionFile.exists()) {
         return null;
      } else {
         DataInputStream dis = new DataInputStream(new FileInputStream(versionFile));
         String version = dis.readUTF();
         dis.close();
         return version;
      }
   }

   public void updateVersion(String version) throws Exception {
      File dir = this.utils.getDirectory();
      File versionFile = new File(dir, "version");
      DataOutputStream dos = new DataOutputStream(new FileOutputStream(versionFile));
      dos.writeUTF(version);
      dos.close();
   }

   public File unpackClient(URL url, File targetDir) throws IOException {
      if(!targetDir.exists()) {
         targetDir.mkdirs();
      }
      URLConnection urlconnection = url.openConnection();
      this.totalDownload = urlconnection.getContentLength() / 1024;
      this.onePercent = (double)(this.totalDownload / 100);
      BufferedInputStream in = new BufferedInputStream(url.openStream(), 1024);
      File zip = File.createTempFile("client", ".zip", targetDir);
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zip));
      this.copyInputStream(in, out, false);
      out.close();
      return this.unpackArchive(zip, targetDir);
   }

   public void unpackLauncher(URL url, File target) throws IOException {
      URLConnection urlconnection = url.openConnection();
      this.totalDownload = urlconnection.getContentLength() / 1024;
      this.onePercent = (double)(this.totalDownload / 100);
      BufferedInputStream in = new BufferedInputStream(url.openStream(), 1024);
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
      this.copyInputStream(in, out, false);
      out.close();
   }

   public File unpackArchive(File theFile, File targetDir) throws IOException {
      if(!theFile.exists()) {
         throw new IOException(theFile.getAbsolutePath() + " does not exist");
      } else if(!this.buildDirectory(targetDir)) {
         throw new IOException("Could not create directory: " + targetDir);
      } else {
         ZipFile zipFile = new ZipFile(theFile);
         Enumeration entries = zipFile.entries();
         while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            File file = new File(targetDir, File.separator + entry.getName());
            if(!this.buildDirectory(file.getParentFile())) {
               throw new IOException("Could not create directory: " + file.getParentFile());
            }
            if(!entry.isDirectory()) {
               this.copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(file)), true);
            } else if(!this.buildDirectory(file)) {
               throw new IOException("Could not create directory: " + file);
            }
         }
         zipFile.close();
         theFile.delete();
         return theFile;
      }
   }

   public void copyInputStream(InputStream in, OutputStream out, boolean zip) throws IOException {
      byte[] buffer = new byte[1024];
      int len = in.read(buffer);
      for(int size = 0; len >= 0; len = in.read(buffer)) {
         if(!zip) {
            size += len;
            this.checkUpdateBar(size / 1024);
         }
         out.write(buffer, 0, len);
      }
      in.close();
      out.close();
   }

   public boolean buildDirectory(File file) {
      return file.exists() || file.mkdirs();
   }
}
