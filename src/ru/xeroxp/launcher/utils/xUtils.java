package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.gui.elements.xServer;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class xUtils {
    private static boolean one = false;

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

    public static void deleteClientFiles() throws IOException {
        File[] files = getDirectory().listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getName().contains("client") && file.getName().endsWith(".zip")) {
                if (!file.delete()) {
                    throw new IOException("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }

        xServer.loadServers();
        for (int i = 0; i < xThemeSettings.SERVERS.length; ++i) {
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

    public static void unpackArchive(File theFile, File targetDir) throws IOException {
        if (!theFile.exists()) {
            throw new IOException(theFile.getAbsolutePath() + " does not exist");
        }

        ZipFile zipFile = new ZipFile(theFile);
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            File file = new File(String.valueOf(targetDir) + File.separator + entry.getName());

            if (!entry.isDirectory()) {
                InputStream inputStream = zipFile.getInputStream(entry);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) >= 0) {
                    outputStream.write(buffer, 0, len);
                }

                inputStream.close();
                outputStream.close();
            } else if (buildDirectory(file)) {
                throw new IOException("Could not create directory: " + file.getAbsolutePath());
            }
        }

        zipFile.close();

        if (!theFile.delete()) {
            throw new IOException("Failed to delete the client file: " + theFile.getAbsolutePath());
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean buildDirectory(File file) {
        return file.exists() || file.mkdirs();
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
        return System.getProperty("java.home") + File.separator + (dir.isEmpty() ? "" : dir + File.separator);
    }

    public static String getJavaExecutable() {
        String path = getJavaPath("bin");
        return getPlatform() == OS.windows && (new File(path + "javaw.exe")).isFile() ? path + "javaw.exe" : path + "java";
    }

    public static enum OS {
        linux, solaris, windows, macos, unknown
    }
}