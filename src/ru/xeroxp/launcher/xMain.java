package ru.xeroxp.launcher;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;

class xMain {

    private static String version = xSettings.launcherVersion;
    public static boolean error = false;
    public static Thread cm;

    public static void main(String[] args) {
        float heapSizeMegs = (float) (Runtime.getRuntime().maxMemory() / 1024L / 1024L);
        if (heapSizeMegs >= 120.0F) {
            start();
        } else {
            try {
                String e = xMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                ArrayList<String> params = new ArrayList<String>();
                params.add("javaw");
                params.add("-Xmx512m");
                params.add("-classpath");
                params.add(e);
                params.add("ru.xeroxp.launcher.xMain");
                ProcessBuilder pb = new ProcessBuilder(params);
                Process process = pb.start();
                if (process == null) {
                    throw new Exception("!");
                }

                System.exit(0);
            } catch (Exception var6) {
                System.out.println(var6.getMessage());
                start();
            }
        }
        new Thread(new Runnable() {
            public boolean texture = false;
            public boolean client = true;
            boolean check = true;

            public void run() {
                while (check) {
                    try {
                        if (!xLauncher.getLauncher().isVisible()) {
                            if (!xTheme.gameOffline) {

                                new Thread(new Runnable() {
                                    public void run() {
                                        texture = xAuth.checkTextures();
                                        client = xAuth.mClientCheck();
                                    }
                                }).start();

                                Thread.sleep(50000);

                                if (texture) {
                                    error = true;
                                    if (xLoader.process != null) xLoader.process.stop();

                                    new Thread(new Runnable() {
                                        public void run() {
                                            new xErrorPanel(xLauncher.getLauncher(), "����� ������� �� ������ ��������").setVisible(true);
                                            System.exit(1);
                                        }
                                    }).start();

                                    Thread.sleep(10000);
                                    System.exit(1);
                                }

                                if (!client) {
                                    error = true;
                                    if (xLoader.process != null) xLoader.process.stop();
                                    new Thread(new Runnable() {
                                        public void run() {
                                            new xErrorPanel(xLauncher.getLauncher(), "������ �� ������ ��������").setVisible(true);
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
                        Thread.sleep(xSettings.checkTime);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
        cm = new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket srv = new ServerSocket(xSettings.localPort);
                    srv.setSoTimeout(0);
                    Socket client = srv.accept();
                    InputStream sin = client.getInputStream();
                    DataInputStream in = new DataInputStream(sin);

                    in.close();
                    sin.close();
                    client.close();
                    srv.close();
                    System.out.println("Launcher process closed");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    public static void start() {
        System.out.println("Starting xLauncher v" + version + " created by XeroXP");
        xLauncher launcher = new xLauncher();
        launcher.setVisible(true);
        Thread thread = new Thread(new xWebThread(launcher));
        thread.start();
        launcher.pack();
    }

    public static void restart() {
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        File currentJar = null;

        try {
            currentJar = new File(xMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException var6) {
            var6.printStackTrace();
        }

        assert currentJar != null;
        if (currentJar.getName().endsWith(".jar") || currentJar.getName().endsWith(".exe")) {
            ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());
            ProcessBuilder builder = new ProcessBuilder(command);

            try {
                builder.start();
                System.exit(0);
            } catch (IOException var5) {
                System.out.println("Failed to restart launcher!");
                System.out.println(var5.getMessage());
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

        public void run() {
            xTheme theme = this.launcher.getTheme();
            theme.getUpdateNews();
            updater = new xUpdater(theme, false);
            this.launcher.repaint();
        }
    }
}
