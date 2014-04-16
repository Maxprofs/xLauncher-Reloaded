package ru.xeroxp.launcher;

import java.io.File;
import java.io.FilenameFilter;

public class xUtils {
    private static boolean one = false;

    private static boolean deleteDir(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (String aChildren : children) {
                    boolean success = deleteDir(new File(dir, aChildren));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }
        return true;
    }

    public void deleteFiles() {
        File[] files = getDirectory().listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().contains("client") && file.getName().endsWith(".zip")) {
                    file.delete();
                }
            }
        }
        xServer.loadServers();
        for (int i = 0; i < xSettingsOfTheme.Servers.length; ++i) {
            xServer server = xServer.getServers()[i];
            if (server.getFolder().isEmpty()) {
                if (!one) {
                    String md = getDirectory() + File.separator;
                    File mdf = new File(md);
                    String[] mdfs = mdf.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File mdf, String name) {
                            return name.contains("mods");
                        }
                    });
                    for (String ignored : mdfs) {
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
        if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) {
            throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        }
        return workingDirectory;
    }

    public OS getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OS.windows;
        }
        if (osName.contains("mac")) {
            return OS.macos;
        }
        if (osName.contains("solaris")) {
            return OS.solaris;
        }
        if (osName.contains("sunos")) {
            return OS.solaris;
        }
        if (osName.contains("linux")) {
            return OS.linux;
        }
        if (osName.contains("unix")) {
            return OS.linux;
        }
        return OS.unknown;
    }

    public String getJavaExecutable() {
        String separator = System.getProperty("file.separator");
        String path = System.getProperty("java.home") + separator + "bin" + separator;
        return getPlatform() == OS.windows && (new File(path + "javaw.exe")).isFile() ? path + "javaw.exe" : path + "java";
    }

    public static enum OS {
        linux, solaris, windows, macos, unknown
    }
}