package ru.xeroxp.launcher;

import net.minecraft.Launcher;
import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.gui.elements.xServer;
import ru.xeroxp.launcher.gui.xSelectTheme;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.misc.xDebug;

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
    private static xLauncher instanse;
    private static boolean oldVer = true;
    private final xTheme theme;
    private xMinecraftFrame minecraft;
    private xOnlineThread online;
    private Point initialClick;
    private String login;
    private String session;

    public xLauncher() {
        instanse = this;
        this.setMinimumSize(new Dimension(xThemeSettings.LAUNCHER_SIZE[0], xThemeSettings.LAUNCHER_SIZE[1]));
        this.setSize(xThemeSettings.LAUNCHER_SIZE[0], xThemeSettings.LAUNCHER_SIZE[1]);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(xSettings.LAUNCHER_NAME + " v" + xMain.getVersion());

        xTheme theme = new xTheme();
        this.theme = theme;
        this.getContentPane().add(theme);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    xLauncher.this.initialClick = e.getPoint();
                    xLauncher.this.getComponentAt(xLauncher.this.initialClick);
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
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
            this.setIconImage(ImageIO.read(xLauncher.class.getResource("/images/" + xThemeSettings.FAVICON)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static xLauncher getIntsanse() {
        return instanse;
    }

    public void iconified() {
        this.setExtendedState(ICONIFIED);
    }

    public xTheme getTheme() {
        return this.theme;
    }

    public void drawServerSelect(String login, String session) {
        this.getContentPane().removeAll();
        this.login = login;
        this.session = session;
        xServer.loadServers();
        xSelectTheme selectTheme = new xSelectTheme();
        selectTheme.addServers();
        this.online = new xOnlineThread(selectTheme);
        Thread onlineThread = new Thread(this.online);
        onlineThread.start();
        this.getContentPane().add(selectTheme);
        this.repaint();
    }

    public void drawMinecraft(String server, String port, String folder, String jar, String version) {
        this.minecraft = new xMinecraftFrame(this.login, this.session, server, port, folder, jar, version);
        this.drawMinecraft();
        this.online.stop();
    }

    public void drawMinecraft(String login) {
        this.minecraft = new xMinecraftFrame(login == null ? "Player" : login);
        this.drawMinecraft();
    }

    void drawMinecraft() {
        this.getContentPane().removeAll();
        this.setVisible(false);
        this.dispose();

        if (oldVer) {
            minecraft.setVisible(true);
            minecraft.pack();
        }
    }

    public class xOnlineThread implements Runnable {
        private final xSelectTheme theme;
        private boolean runned = true;

        public xOnlineThread(xSelectTheme selectTheme) {
            this.theme = selectTheme;
        }

        @Override
        public void run() {
            while (this.runned) {
                try {
                    URLConnection getVer = new URL(xSettings.MONITOR_LINK).openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
                    String onlineLine = in.readLine();
                    in.close();
                    this.theme.updateOnline(onlineLine.split(":"));
                    Thread.sleep(60000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            this.runned = false;
        }
    }

    public class xMinecraftFrame extends JFrame {
        public xMinecraftFrame(String login, String session, String server, String port, String folder, String jar, String version) {
            drawMinecraft();

            oldVer = isOld(version);
            xMain.clientChecker.start();

            if (oldVer) {
                Launcher applet = new Launcher();
                xDebug.infoMessage(login + ":" + session + ":" + server + ":" + port + ":" + folder + ":" + jar + ":" + version);
                applet.init(login, session, server, port, folder, jar, version);
                this.add(applet);
                applet.start();
            } else {
                theme.setVisible(false);
                instanse.setVisible(false);
                new xLoader(login, session, server, port, folder, jar, version);
            }
        }

        public xMinecraftFrame(String userName) {
            drawMinecraft();

            oldVer = isOld(xSettings.OFFLINE_CLIENT[2]);
            xMain.clientChecker.start();

            if (oldVer) {
                Launcher applet = new Launcher();
                applet.init(userName);
                this.add(applet);
                applet.start();
            } else {
                theme.setVisible(false);
                instanse.setVisible(false);
                new xLoader(userName);
            }
        }

        private void drawMinecraft() {
            this.setSize(860, 520);
            this.setLocationRelativeTo(null);
            this.setMinimumSize(new Dimension(860, 520));
            this.setTitle(xSettings.GAME_NAME);
            this.setBackground(Color.WHITE);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            try {
                this.setIconImage(ImageIO.read(ru.xeroxp.launcher.xLauncher.class.getResource("/images/" + xThemeSettings.FAVICON)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isOld(String version) {
            String versionNum = version.replaceAll("\\.", "");
            return ((versionNum.length() == 3) && (Integer.parseInt(versionNum) < 160)) || ((versionNum.length() == 2) && (Integer.parseInt(versionNum) < 16));
        }
    }
}