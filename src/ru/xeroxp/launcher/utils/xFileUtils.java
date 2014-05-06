package ru.xeroxp.launcher.utils;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.xLauncher;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class xFileUtils {
    public static boolean delete(File dir) {
        if (!dir.exists()) {
            return true;
        }

        if (!dir.isDirectory()) {
            return dir.delete();
        }

        for (String aChildren : dir.list()) {
            if (!delete(new File(dir, aChildren))) {
                return false;
            }
        }

        return dir.delete();
    }


    public static void downloadFile(URL url, File target) throws IOException {
        URLConnection urlConnection = url.openConnection();
        xLauncher.getIntsanse().getTheme().progressBar.setOnePercent(urlConnection.getContentLength() / 1024);
        BufferedInputStream in = new BufferedInputStream(url.openStream(), 1024);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
        copyInputStream(in, out);
        out.close();
    }

    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);

        for (int size = 0; len >= 0; len = in.read(buffer)) {
            size += len;
            xLauncher.getIntsanse().getTheme().progressBar.update(size / 1024);
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    public static File getRootDirectory() {
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

    public static void unZip(File theFile, File targetDir) throws IOException {
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
            } else {
                buildDirectory(file);
            }
        }

        zipFile.close();

        if (!theFile.delete()) {
            throw new IOException("Failed to delete temp file: " + theFile.getAbsolutePath());
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static void buildDirectory(File file) throws IOException {
        if (file.exists() || file.mkdirs()) {
            throw new IOException("Could not create directory: " + file);
        }
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