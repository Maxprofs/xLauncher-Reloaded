package net.minecraft;

import ru.xeroxp.launcher.xSettings;
import ru.xeroxp.launcher.xUtils;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends Applet implements AppletStub, MouseListener {
    private final Map<String, String> customParameters = new HashMap<String, String>();
    private Applet applet;
    private xMinecraft minecraft;
    private boolean minecraftStarted = false;
    private int context = 0;
    private boolean active = false;
    private static String folder;
    private static String version;

    @Override
    public boolean isActive() {
        if (this.context == 0) {
            this.context = -1;

            try {
                if (this.getAppletContext() != null) {
                    this.context = 1;
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        return this.context == -1 ? this.active : super.isActive();
    }

    public void init(String userName, String sessionId, String server, String port, String folder, String jar, String version) {
        this.customParameters.put("stand-alone", "true");
        this.customParameters.put("username", userName);
        this.customParameters.put("sessionid", sessionId);
        this.customParameters.put("server", server);
        this.customParameters.put("port", port);
        this.minecraft = new xMinecraft(jar);
        Launcher.folder = folder;
        Launcher.version = version;
    }

    public void init(String userName) {
        this.customParameters.put("stand-alone", "true");
        this.customParameters.put("username", userName);
        this.customParameters.put("sessionid", "1");
        this.minecraft = new xMinecraft(xSettings.offlineClient[1]);
        folder = xSettings.offlineClient[0];
        version = xSettings.offlineClient[2];
    }

    @Override
    public void start() {
        if (this.applet != null) {
            this.applet.start();
        } else if (!this.minecraftStarted) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    Launcher.this.minecraft.run();

                    try {
                        Launcher.this.minecraft.patchDir();
                        Launcher.this.replace(Launcher.this.minecraft.loadApplet());
                    } catch (ClassNotFoundException var2) {
                        var2.printStackTrace();
                    } catch (InstantiationException var3) {
                        var3.printStackTrace();
                    } catch (IllegalAccessException var4) {
                        var4.printStackTrace();
                    } catch (Exception var5) {
                        var5.printStackTrace();
                    }

                }
            };
            t.setDaemon(true);
            t.start();
            t = new Thread() {
                @Override
                public void run() {
                    while (Launcher.this.applet == null) {
                        Launcher.this.repaint();

                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException var2) {
                            var2.printStackTrace();
                        }
                    }

                }
            };
            t.setDaemon(true);
            t.start();
            this.minecraftStarted = true;
        }
    }

    void replace(Applet applet) {
        this.applet = applet;
        applet.setStub(this);
        applet.setSize(this.getWidth(), this.getHeight());
        this.setLayout(new BorderLayout());
        this.add(applet, "Center");
        applet.init();
        this.active = true;
        applet.start();
        this.validate();
    }

    @Override
    public void stop() {
        if (this.applet != null) {
            this.applet.stop();
            this.active = false;
        }
    }

    @Override
    public void destroy() {
        if (this.applet != null) {
            this.applet.destroy();
        }
    }

    @Override
    public String getParameter(String name) {
        String custom = this.customParameters.get(name);
        if (custom != null) {
            return custom;
        } else {
            try {
                return super.getParameter(name);
            } catch (Exception var4) {
                this.customParameters.put(name, null);
                return null;
            }
        }
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(xSettings.siteLink);
        } catch (MalformedURLException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void appletResize(int width, int height) {
    }

    public class xMinecraft implements Runnable {
        private ClassLoader classLoader;
        private final String jar;

        public xMinecraft(String jar) {
            this.jar = jar;
        }

        @Override
        public void run() {
            xUtils utils = new xUtils();
            File dir;
            if (folder.isEmpty()) {
                dir = new File(utils.getDirectory() + File.separator + "bin" + File.separator);
            } else {
                dir = new File(utils.getDirectory() + File.separator + folder + File.separator + "bin" + File.separator);
            }
            String[] jars = new String[4];
            if (utils.getPlatform().ordinal() != 2 && utils.getPlatform().ordinal() != 3) {
                jars[0] = "lwjgl2.jar";
            } else {
                jars[0] = "lwjgl.jar";
            }

            jars[1] = "jinput.jar";
            jars[2] = "lwjgl_util.jar";
            jars[3] = this.jar;

            URL[] urls = new URL[4];

            for (int i = 0; i < 4; ++i) {
                try {
                    urls[i] = (new File(dir, jars[i])).toURI().toURL();
                } catch (MalformedURLException var7) {
                    System.out.println("Failed load libs");
                    System.out.println(var7.getMessage());
                }
            }

            classLoader = new URLClassLoader(urls);
            if (folder.isEmpty()) {
                System.setProperty("org.lwjgl.librarypath", utils.getDirectory() + File.separator + "bin" + File.separator + "natives");
                System.setProperty("net.java.games.input.librarypath", utils.getDirectory() + File.separator + "bin" + File.separator + "natives");
            } else {
                System.setProperty("org.lwjgl.librarypath", utils.getDirectory() + File.separator + folder + File.separator + "bin" + File.separator + "natives");
                System.setProperty("net.java.games.input.librarypath", utils.getDirectory() + File.separator + folder + File.separator + "bin" + File.separator + "natives");
            }
        }

        public Applet loadApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class<?> appletClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
            return (Applet) appletClass.newInstance();
        }

        public void patchDir() {
            //noinspection ConstantConditions,PointlessBooleanExpression
            if (!xSettings.patchDir) return;
            try {
                String mcver = version;
                for (int j = 0; j < xSettings.mcVersions.length; j++) {
                    String mcVerFromSettings = xSettings.mcVersions[j].split("::")[0];
                    if ((!mcVerFromSettings.contains("x") && mcver.equals(mcVerFromSettings)) || (mcver.substring(0, 3).equals(mcVerFromSettings.substring(0, 3)))) {
                        xUtils utils = new xUtils();
                        Field f = classLoader.loadClass(xSettings.mcClass).getDeclaredField(xSettings.mcVersions[j].split("::")[1]);
                        Field.setAccessible(new Field[]{f}, true);
                        if (folder.isEmpty()) {
                            f.set(null, utils.getDirectory());
                        } else {
                            f.set(null, new File(utils.getDirectory() + File.separator + folder));
                        }
                        System.out.println("File patched: " + xSettings.mcClass + "::" + xSettings.mcVersions[j].split("::")[1]);
                        return;
                    }
                }
                System.out.println("Error: Client version not correct.");
            } catch (Exception e) {
                System.out.println("Error: Client field not correct.");
            }
        }
    }
}