package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.config.xSettingsOfTheme;
import ru.xeroxp.launcher.gui.elements.xServer;

import java.io.File;
import java.io.FilenameFilter;

public class xUtils {
    private static boolean one = false;
    private static final String SEP = System.getProperty("file.separator");

    private static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return true;
        }

        if (!dir.isDirectory()) {
            return dir.delete();
        }

        for (String aChildren : dir.list()) {
            if (!deleteDir(new File(dir, aChildren))) {
                return false;
            }
        }

        return dir.delete();
    }

    public static void deleteFiles() {
        File[] files = getDirectory().listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getName().contains("client") && file.getName().endsWith(".zip")) {
                file.delete();
            }
        }

        xServer.loadServers();
        for (int i = 0; i < xSettingsOfTheme.SERVERS.length; ++i) {
            xServer server = xServer.getServers()[i];

            if (!server.getFolder().isEmpty()) {
                deleteDir(new File(getDirectory() + File.separator + server.getFolder()));
            } else if (!one) {
                File md = new File(getDirectory() + File.separator);

                String[] mds = md.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File mdf, String name) {
                        return name.contains("mods");
                    }
                });

                for (String ignored : mds) {
                    deleteDir(new File(getDirectory() + File.separator + mds[i]));
                }

                deleteDir(new File(getDirectory() + File.separator + "bin"));
                deleteDir(new File(getDirectory() + File.separator + "config"));

                one = true;
            }
        }
    }

    public static File getDirectory() {
        String applicationName = xSettings.MINE_FOLDER;
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;

        switch (getPlatform().ordinal()) {
            case 0:
            case 1:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case 2:
                String applicationData = System.getenv("APPDATA");
                workingDirectory = new File((applicationData == null) ? userHome : applicationData, '.' + applicationName + '/');
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

    public static OS getPlatform() {
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

    public static String getJavaPath() {
        return getJavaPath("");
    }

    public static String getJavaPath(String dir) {
        return System.getProperty("java.home") + SEP + (dir.isEmpty() ? "" : dir + SEP);
    }

    public static String getJavaExecutable() {
        String path = getJavaPath("bin");
        return getPlatform() == OS.windows && (new File(path + "javaw.exe")).isFile() ? path + "javaw.exe" : path + "java";
    }

    public static enum OS {
        linux, solaris, windows, macos, unknown
    }
}