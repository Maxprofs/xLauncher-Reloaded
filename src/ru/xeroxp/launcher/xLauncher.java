package ru.xeroxp.launcher;

import net.minecraft.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class xLauncher extends JFrame {
    private static xLauncher launcher;
    private static boolean oldver = true;
    private xMinecraftFrame minecraft;
    private xOnlineThread online;
    private final xTheme theme;
    private Point initialClick;
    private String login;
    private String session;
    private boolean sound = true;

    public xLauncher() {
        launcher = this;
        setMinimumSize(new Dimension(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]));
        setSize(xSettingsOfTheme.LauncherSize[0], xSettingsOfTheme.LauncherSize[1]);
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle(xSettings.LauncherName + " v" + xMain.getVersion());

        xTheme theme = new xTheme();
        this.theme = theme;
        getContentPane().add(theme);

        xUtils utils = new xUtils();
        if (utils.getPlatform().ordinal() == 0) {
            this.sound = false;
        }

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    xLauncher.this.initialClick = e.getPoint();
                    xLauncher.this.getComponentAt(xLauncher.this.initialClick);
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int thisX = xLauncher.this.getLocation().x;
                    int thisY = xLauncher.this.getLocation().y;
                    int xMoved = thisX + e.getX() - (thisX + xLauncher.this.initialClick.x);
                    int yMoved = thisY + e.getY() - (thisY + xLauncher.this.initialClick.y);
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    xLauncher.this.setLocation(X, Y);
                }
            }
        });
        try {
            setIconImage(ImageIO.read(xLauncher.class.getResource("/images/" + xSettingsOfTheme.Favicon)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static xLauncher getLauncher() {
        return launcher;
    }

    public void iconified() {
        this.setExtendedState(ICONIFIED);
    }

    public xTheme getTheme() {
        return this.theme;
    }

    public void drawServerSelect(String login, String session) {
        launcher.getContentPane().removeAll();
        this.login = login;
        this.session = session;
        xServer.loadServers();
        xSelectTheme selectTheme = new xSelectTheme();
        selectTheme.addServers();
        this.online = new xOnlineThread(selectTheme);
        Thread onlineThread = new Thread(this.online);
        onlineThread.start();
        getContentPane().add(selectTheme);
        launcher.repaint();
    }

    public void drawMinecraft(String server, String port, String folder, String jar, String version) {
        launcher.getContentPane().removeAll();
        launcher.setVisible(false);
        launcher.dispose();
        minecraft = new xMinecraftFrame(this.login, this.session, server, port, folder, jar, version);
        if (oldver) {
            minecraft.setVisible(true);
            minecraft.pack();
        }
        this.online.stop();
    }

    public void drawMinecraft(String login) {
        launcher.getContentPane().removeAll();
        launcher.setVisible(false);
        launcher.dispose();
        if (login == null) {
            login = "Player";
        }
        minecraft = new xMinecraftFrame(login);
        if (oldver) {
            minecraft.setVisible(true);
            minecraft.pack();
        }
    }

    public boolean getSound() {
        return this.sound;
    }

    public class xOnlineThread implements Runnable {
        private final xSelectTheme theme;
        private boolean run = true;

        public xOnlineThread(xSelectTheme selectTheme) {
            this.theme = selectTheme;
        }

        @Override
        public void run() {
            while (this.run) {
                try {
                    URL e = new URL(xSettings.monitorLink);
                    URLConnection getVer = e.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
                    String onlineLine = in.readLine();
                    in.close();
                    this.theme.updateOnline(onlineLine.split(":"));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException var5) {
                    var5.printStackTrace();
                }
            }
        }

        public void stop() {
            this.run = false;
        }
    }

    public class xMinecraftFrame extends JFrame {
        public xMinecraftFrame(String login, String session, String server, String port, String folder, String jar, String version) {
            this.setSize(860, 520);
            this.setLocationRelativeTo(null);
            this.setMinimumSize(new Dimension(860, 520));
            this.setTitle(xSettings.gameName);
            this.setBackground(Color.WHITE);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            try {
                this.setIconImage(ImageIO.read(ru.xeroxp.launcher.xLauncher.class.getResource("/images/" + xSettingsOfTheme.Favicon)));
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            String versionr = version.replaceAll("\\.", "");
            boolean old = true;
            if (((versionr.length() == 3) && (Integer.parseInt(versionr) >= 160)) || ((versionr.length() == 2) && (Integer.parseInt(versionr) >= 16))) {
                old = false;
            }
            oldver = old;
            xMain.cm.start();
            if (old) {
                Launcher applet = new Launcher();
                applet.init(login, session, server, port, folder, jar, version);
                this.add(applet);
                applet.start();
            } else {
                theme.setVisible(false);
                launcher.setVisible(false);
                new xLoader(login, session, server, port, folder, jar, version);
            }
        }

        public xMinecraftFrame(String userName) {
            this.setSize(860, 520);
            this.setLocationRelativeTo(null);
            this.setMinimumSize(new Dimension(860, 520));
            this.setTitle(xSettings.gameName);
            this.setBackground(Color.WHITE);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            try {
                this.setIconImage(ImageIO.read(ru.xeroxp.launcher.xLauncher.class.getResource("/images/" + xSettingsOfTheme.Favicon)));
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            String versionr = xSettings.offlineClient[2].replaceAll("\\.", "");
            boolean old = true;
            if (((versionr.length() == 3) && (Integer.parseInt(versionr) >= 160)) || ((versionr.length() == 2) && (Integer.parseInt(versionr) >= 16))) {
                old = false;
            }
            oldver = old;
            xMain.cm.start();
            if (old) {
                Launcher applet = new Launcher();
                applet.init(userName);
                this.add(applet);
                applet.start();
            } else {
                theme.setVisible(false);
                launcher.setVisible(false);
                new xLoader(userName);
            }
        }
    }
}