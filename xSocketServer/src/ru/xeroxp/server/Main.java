package ru.xeroxp.server;

import java.io.File;

public class Main {
    public static String[] files = {};
    public static String hash = "";
    public static String formats = "";
    public static String launcherSizeJar = "";
    public static String launcherSizeExe = "";

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer();
        new Thread(server).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(Settings.MONITOR_TIME_UPDATE);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();

        for (int i = 0; i < Settings.CHECK_FORMATS.length; i++) {
            if (i == 0) {
                formats = Settings.CHECK_FORMATS[i];
            } else {
                formats = formats + ";" + Settings.CHECK_FORMATS[i];
            }
        }

        File dir = new File(new File("").getAbsolutePath() + "/check");
        Worker.launcherSize(new File("").getAbsolutePath());

        try {
            hash = Worker.check(dir);
            Thread monitor = new StopMonitor();
            monitor.start();
            monitor.join();
            System.out.println("Right after join.....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Stopping Server");
        server.stop();
        System.exit(0);
    }
}