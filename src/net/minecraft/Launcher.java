package net.minecraft;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.utils.xFileUtils;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends Applet implements AppletStub {
    private static String folder;
    private static String version;
    private final Map<String, String> customParameters = new HashMap<String, String>();
    private Applet applet;
    private xMinecraft minecraft;
    private boolean minecraftStarted = false;
    private int context = 0;
    private boolean active = false;

    @Override
    public boolean isActive() {
        if (this.context == 0) {
            this.context = -1;

            try {
                if (this.getAppletContext() != null) {
                    this.context = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        this.minecraft = new xMinecraft(xSettings.OFFLINE_CLIENT[1]);
        folder = xSettings.OFFLINE_CLIENT[0];
        version = xSettings.OFFLINE_CLIENT[2];
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
                    } catch (Exception e) {
                        e.printStackTrace();
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
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
            } catch (Exception e) {
                this.customParameters.put(name, null);
                return null;
            }
        }
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(xSettings.SITE_LINK);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void appletResize(int width, int height) {
    }

    public class xMinecraft implements Runnable {
        private final String jar;
        private ClassLoader classLoader;

        public xMinecraft(String jar) {
            this.jar = jar;
        }

        @Override
        public void run() {
            File dir;

            if (folder.isEmpty()) {
                dir = new File(xFileUtils.getRootDirectory() + File.separator + "bin" + File.separator);
            } else {
                dir = new File(xFileUtils.getRootDirectory() + File.separator + folder + File.separator + "bin" + File.separator);
            }

            String[] jars = new String[4];
            if (xFileUtils.getPlatform().ordinal() != 2 && xFileUtils.getPlatform().ordinal() != 3) {
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
                } catch (MalformedURLException e) {
                    System.out.println("Failed load libs");
                    System.out.println(e.getMessage());
                }
            }

            classLoader = new URLClassLoader(urls);
            if (folder.isEmpty()) {
                System.setProperty("org.lwjgl.librarypath", xFileUtils.getRootDirectory() + File.separator + "bin" + File.separator + "natives");
                System.setProperty("net.java.games.input.librarypath", xFileUtils.getRootDirectory() + File.separator + "bin" + File.separator + "natives");
            } else {
                System.setProperty("org.lwjgl.librarypath", xFileUtils.getRootDirectory() + File.separator + folder + File.separator + "bin" + File.separator + "natives");
                System.setProperty("net.java.games.input.librarypath", xFileUtils.getRootDirectory() + File.separator + folder + File.separator + "bin" + File.separator + "natives");
            }
        }

        public Applet loadApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class<?> appletClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
            return (Applet) appletClass.newInstance();
        }

        public void patchDir() {
            //noinspection ConstantConditions,PointlessBooleanExpression
            if (!xSettings.PATCH_DIR) return;
            try {
                String mcVer = version;

                for (int j = 0; j < xSettings.MC_VERSIONS.length; j++) {
                    String mcVerFromSettings = xSettings.MC_VERSIONS[j].split("::")[0];

                    if ((!mcVerFromSettings.contains("x") && mcVer.equals(mcVerFromSettings)) || (mcVer.substring(0, 3).equals(mcVerFromSettings.substring(0, 3)))) {
                        Field f = classLoader.loadClass(xSettings.MC_CLASS).getDeclaredField(xSettings.MC_VERSIONS[j].split("::")[1]);
                        Field.setAccessible(new Field[]{f}, true);

                        if (folder.isEmpty()) {
                            f.set(null, xFileUtils.getRootDirectory());
                        } else {
                            f.set(null, new File(xFileUtils.getRootDirectory() + File.separator + folder));
                        }

                        xDebug.infoMessage("File patched: " + xSettings.MC_CLASS + "::" + xSettings.MC_VERSIONS[j].split("::")[1]);
                        return;
                    }
                }

                xDebug.errorMessage("Client version not correct.");
            } catch (Exception e) {
                xDebug.errorMessage("Client field not correct.");
            }
        }
    }
}