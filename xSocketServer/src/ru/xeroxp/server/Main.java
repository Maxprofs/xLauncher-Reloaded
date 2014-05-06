package ru.xeroxp.server;

import ru.xeroxp.server.config.Settings;
import ru.xeroxp.server.utils.Debug;

import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Main {
    public static final List<String> files = new ArrayList<String>();
    public static String hash = "";
    public static String formats = "";
    public static String launcherSizeJar = "";
    public static String launcherSizeExe = "";

    public static void main(String[] args) {
        if (args.length == 0) {
            Debug.errorMessage("Empty parameters!");
            System.exit(0);
        }

        if (args[0].equals("start")) {
            start();
        } else if (args[0].equals("stop")) {
            stop();
        }

        System.exit(0);
    }

    private static void start() {
        MultiThreadedServer server = new MultiThreadedServer();
        new Thread(server).start();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) try {
                    Thread.sleep(Settings.MONITOR_TIME_UPDATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        for (int i = 0; i < Settings.CHECK_FORMATS.length; i++) {
            formats = ((i == 0) ? "" : formats + ";") + Settings.CHECK_FORMATS[i];
        }

        File dir = new File(new File("").getAbsolutePath() + "/check");
        Worker.launcherSize(new File("").getAbsolutePath());

        try {
            hash = Worker.check(dir);
            Thread monitor = new StopMonitor();
            monitor.start();
            monitor.join();
            Debug.infoMessage("Right after join...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.infoMessage("Stopping Server");
        server.stop();
    }

    private static void stop() {
        try {
            Socket socket = new Socket(InetAddress.getByName(Settings.STOP_IP), Settings.PORT_STOP);
            socket.setSoTimeout(3000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("stop");
            out.flush();
            socket.close();
            Debug.infoMessage("Server Stopped");
        } catch (Exception e) {
            Debug.errorMessage("Не удалось подключиться к серверу: " + e.getMessage());
        }
    }
}