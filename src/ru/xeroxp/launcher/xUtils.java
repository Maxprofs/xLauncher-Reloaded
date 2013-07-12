package ru.xeroxp.launcher;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;

public class xUtils
{
  private static boolean one = false; 
  public void deleteFiles() throws IOException {
    File[] files = getDirectory().listFiles();
    for(int i = 0; i < files.length; i++) {
        if (files[i].isFile()) {
            if (files[i].getName().contains("client") && files[i].getName().endsWith(".zip")) {
                files[i].delete();
            }
        }
    }
    xServer.loadServers();
    for(int i = 0; i < xSettingsOfTheme.Servers.length; ++i) {
        xServer server = xServer.getServers()[i];
        if (server.getFolder().isEmpty()){
            if (!one){
                String md = getDirectory() + File.separator;
                File mdf = new File(md);
                String[] mdfs = mdf.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File mdf, String name) {
                        return name.contains("mods");
                    }
                });
                for(int m = 0; m < mdfs.length; ++m) {
                    deleteDir(new File(getDirectory() + File.separator + mdfs[i]));
                }
                deleteDir(new File(getDirectory() + File.separator + "bin"));
                deleteDir(new File(getDirectory() + File.separator + "config"));
                one = true;
            }
        } else {
            deleteDir(new File(getDirectory() + File.separator + server.getFolder()));
        }
    }
  }

  public static boolean deleteDir(File dir) {
    if (dir.exists()) {
      if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
          boolean success = deleteDir(new File(dir, children[i]));
          if (!success) {
            return false;
          }
        }
      }
      return dir.delete();
    }
    return true;
  }

  public File getDirectory() {
    String applicationName = xSettings.mineFolder;
    String userHome = System.getProperty("user.home", ".");
    File workingDirectory;
    switch (getPlatform().ordinal()) {
    case 0:
    case 1:
      workingDirectory = new File(userHome, '.' + applicationName + '/');
      break;
    case 2:
      String applicationData = System.getenv("APPDATA");
      if (applicationData != null) {
        workingDirectory = new File(applicationData, "." + applicationName + '/');
      } else {
        workingDirectory = new File(userHome, '.' + applicationName + '/');
      }
      break;
    case 3:
      workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
      break;
    default:
      workingDirectory = new File(userHome, applicationName + '/');
    }
    if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())){
      throw new RuntimeException("The working directory could not be created: " + workingDirectory);
    }
    return workingDirectory;
  }

  public OS getPlatform() {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) {return OS.windows;}
    if (osName.contains("mac")) {return OS.macos;}
    if (osName.contains("solaris")) {return OS.solaris;}
    if (osName.contains("sunos")) {return OS.solaris;}
    if (osName.contains("linux")) {return OS.linux;}
    if (osName.contains("unix")) {return OS.linux;}
    return OS.unknown;
  }

  public String getDate()
  {
    Calendar c = Calendar.getInstance();
    String date = c.get(5) + ".";
    int month = c.get(2) + 1;
    date = date + Integer.toString(month);
    return date;
  }

  public static enum OS {
    linux, solaris, windows, macos, unknown;
  }
}