package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.gui.xErrorPanel;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.utils.xFileUtils;
import ru.xeroxp.launcher.utils.xTextureUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class xMain {

    public static boolean error = false;
    public static Thread clientChecker;
    private static String version = xSettings.LAUNCHER_VERSION;

    public static void main(String[] args) {
        float heapSizeMegs = (float) (Runtime.getRuntime().maxMemory() / 1024L / 1024L);

        if (heapSizeMegs >= 120.0F) {
            start();
        } else {
            try {
                String classPath = xMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                List<String> params = new ArrayList<String>();
                params.add("javaw");
                params.add("-Xmx512m");
                params.add("-classpath");
                params.add(classPath);
                params.add("ru.xeroxp.launcher.xMain");
                ProcessBuilder pb = new ProcessBuilder(params);
                Process process = pb.start();
                if (process == null) {
                    throw new Exception("!");
                }

                System.exit(0);
            } catch (Exception e) {
                xDebug.errorMessage(e.getMessage());
                start();
            }
        }

        new Thread(new Runnable() {
            public boolean texture = true;
            public boolean client = true;
            boolean check = true;

            @Override
            public void run() {
                while (check) {
                    try {
                        if (!xLauncher.getIntsanse().isVisible()) {
                            if (!xTheme.offlineMode) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        texture = xTextureUtils.check();
                                        client = xAuth.mCheckClient();
                                    }
                                }).start();

                                Thread.sleep(50000);

                                if (!texture || !client) {
                                    error = true;
                                    if (xLoader.process != null) xLoader.process.stop();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new xErrorPanel(xLauncher.getIntsanse(), texture ? "Клиент не прошел проверку" : "Папка текстур не прошла проверку").setVisible(true);
                                            System.exit(1);
                                        }
                                    }).start();
                                    Thread.sleep(10000);
                                    System.exit(1);
                                }
                            } else {
                                check = false;
                            }
                        }

                        Thread.sleep(xSettings.CHECK_TIME);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

        clientChecker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket srv = new ServerSocket(xSettings.LOCAL_PORT);
                    srv.setSoTimeout(0);
                    xDebug.infoMessage("Wait client connection...");
                    Socket client = srv.accept();
                    xDebug.infoMessage("Client connected.");
                    InputStream inputStream = client.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    while (client.isConnected() && !client.isClosed()) {
                        if (!dataInputStream.readUTF().equals("I'm alive")) {
                            break;
                        }

                        Thread.sleep(10000);
                    }

                    dataInputStream.close();
                    inputStream.close();
                    client.close();
                    srv.close();
                    xDebug.errorMessage("Launcher process closed");
                } catch (Exception e) {
                    xDebug.errorMessage(e.getMessage());
                }
            }
        });
    }

    public static void start() {
        xDebug.infoMessage("Starting xLauncher v" + version + " created by XeroXP and OsipXD");
        xLauncher launcher = new xLauncher();
        launcher.setVisible(true);
        Thread thread = new Thread(new xWebThread(launcher));
        thread.start();
        launcher.pack();
    }

    public static void restart() {
        String javaBin = xFileUtils.getJavaPath("bin" + File.separator + "java");
        File currentJar = null;

        try {
            currentJar = new File(xMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assert currentJar != null;
        if (currentJar.getName().endsWith(".jar") || currentJar.getName().endsWith(".exe")) {
            List<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());
            ProcessBuilder builder = new ProcessBuilder(command);

            try {
                builder.start();
                System.exit(0);
            } catch (IOException e) {
                xDebug.errorMessage("Failed to restart launcher: " + e.getMessage());
            }
        }
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String newVersion) {
        version = newVersion;
    }

    public static class xWebThread implements Runnable {
        public static xUpdater updater;
        private final xLauncher launcher;

        public xWebThread(xLauncher launcher) {
            this.launcher = launcher;
        }

        @Override
        public void run() {
            xTheme theme = this.launcher.getTheme();
            theme.getUpdateNews();
            updater = new xUpdater(theme);
            this.launcher.repaint();
        }
    }
}
