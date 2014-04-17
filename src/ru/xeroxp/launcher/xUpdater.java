package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.config.xThemeSettings;
import ru.xeroxp.launcher.gui.elements.xButton;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.utils.xDebug;
import ru.xeroxp.launcher.utils.xUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class xUpdater {
    private BufferedImage update;
    private BufferedImage bg;
    private BufferedImage image;
    private final BufferedImage background;
    private final xTheme theme;
    private int totalDownload;
    private double onePercent;
    private boolean firstBg = true;

    public xUpdater(xTheme theme) {
        try {
            this.update = ImageIO.read(xUpdater.class.getResource("/images/updatebar_ok.png"));
            this.image = ImageIO.read(xUpdater.class.getResource("/images/updatebar.png"));
            this.bg = ImageIO.read(xUpdater.class.getResource("/images/updatebar_bg.png"));
        } catch (IOException var3) {
            xDebug.errorMessage("Failed load updater images: " + var3.getMessage());
        }

        this.theme = theme;
        this.theme.setOpaque(true);
        this.theme.setBackground(new Color(0, 0, 0, 0));
        this.background = theme.background;
        this.theme.revalidate();
        this.theme.repaint();
        this.checkLauncherUpdate();
        this.checkClientUpdate(false);
    }

    private void checkLauncherUpdate() {
        try {
            String e = this.checkVersion(UpdateType.launcher);

            if (e != null) {
                String getVersion = xMain.getVersion();

                if (!e.equals(getVersion)) {
                    this.theme.lockAuth(true);
                    this.updateLauncher(e);
                }

                this.updateDownload();
                this.theme.lockAuth(false);
            }
        } catch (Exception var3) {
            xDebug.errorMessage("Failed check launcher update: " + var3.getMessage());
        }
    }

    private void updateLauncher(String checkVersion) {
        disableButtons();
        File runningLauncher = null;

        try {
            runningLauncher = new File(xUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException var5) {
            xDebug.errorMessage("Failed to find launcher path: " + var5.getMessage());
        }

        try {
            if (runningLauncher != null) {
                if (runningLauncher.getPath().endsWith(".jar")) {
                    this.unpackLauncher(new URL(xSettings.DOWN_LAUNCHER_LINK + xSettings.LAUNCHER_FILE_NAME + ".jar"), runningLauncher);
                } else if (runningLauncher.getPath().endsWith(".exe")) {
                    this.unpackLauncher(new URL(xSettings.DOWN_LAUNCHER_LINK + xSettings.LAUNCHER_FILE_NAME + ".exe"), runningLauncher);
                }
            }
            xMain.setVersion(checkVersion);
            xMain.restart();
        } catch (IOException var4) {
            xDebug.errorMessage("Failed update launcher:" + var4.getMessage());
        }
    }

    void updateDownload() {
        this.theme.setOpaque(true);
        this.theme.setBackground(new Color(0, 0, 0, 0));
        Graphics g2 = this.background.getGraphics();
        g2.setColor(new Color(0, 0, 0, 0));
        g2.drawImage(this.update, 42, 536, null);
        g2.fillRect(42, 536, this.bg.getWidth(), this.bg.getHeight());
        g2.dispose();
        this.theme.revalidate();
        this.theme.repaint();
    }

    void checkUpdateBar(int size) {
        double percent = (double) size / this.onePercent;

        if (percent < 1.0D) {
            percent = 1.0D;
        }

        if (percent > 100.0D) {
            percent = 100.0D;
        }

        this.updateDownload(percent);
    }

    void updateDownload(double done) {
        this.theme.setOpaque(true);
        this.theme.setBackground(new Color(0, 0, 0, 0));
        Graphics g2 = this.background.getGraphics();
        g2.setColor(new Color(0, 0, 0, 0));

        if (this.firstBg) {
            g2.drawImage(this.bg, 42, 536, null);
            this.firstBg = false;
        }

        if (done == 100.0D) {
            g2.drawImage(this.update, 42, 536, null);
        } else {
            g2.drawImage(this.image.getSubimage(0, 0, (int) (6.62D * done), 29), 42, 536, null);
            this.theme.updatePercent((int) done);
        }

        g2.fillRect(42, 536, this.bg.getWidth(), this.bg.getHeight());
        g2.dispose();
        this.theme.revalidate();
        this.theme.repaint();
    }

    void updateClient(String version) {
        disableButtons();

        try {
            xUtils.deleteClientFiles();
        } catch (IOException e) {
            xDebug.errorMessage(e.getMessage());
        }

        try {
            this.unpackClient(new URL(xSettings.DOWN_CLIENT_LINK + "client.zip"), xUtils.getDirectory());
        } catch (IOException var4) {
            xDebug.errorMessage(var4.getMessage());
        }

        try {
            this.updateVersion(version);
        } catch (Exception var3) {
            xDebug.errorMessage("Failed update client version: " + var3.getMessage());
        }

        for (int i = 0; i < xThemeSettings.BUTTONS.length; ++i) {
            xButton button = xButton.getButtons()[i];

            if (button.getId() == xButton.RAM_ID) {
                this.theme.buttons[button.getId()].setEnabled(true);
                break;
            }
        }
    }

    String checkVersion(UpdateType type) {
        try {
            URL e = new URL(xSettings.MAIN_INFO_FILE + "?action=" + type.getParameter());
            URLConnection getVer = e.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
            String inputLine = in.readLine();
            in.close();

            return inputLine;
        } catch (Exception var5) {
            xDebug.errorMessage("Failed check " + type.name() + " version: " + var5.getMessage());
        }

        return null;
    }

    public void checkClientUpdate(boolean n) {
        try {
            String e = this.checkVersion(UpdateType.client);

            if (e != null) {
                String getVersion;
                getVersion = n ? "0" : this.getVersion();

                if (getVersion == null) {
                    this.theme.lockAuth(true);
                    this.updateClient(e);
                    this.theme.lockAuth(false);
                    return;
                }

                if (!e.equals(getVersion)) {
                    this.theme.lockAuth(true);
                    this.updateClient(e);
                }

                this.updateDownload();
                this.theme.lockAuth(false);
            }
        } catch (Exception var3) {
            xDebug.errorMessage("Failed check client update: " + var3.getMessage());
        }
    }

    String getVersion() throws Exception {
        File dir = xUtils.getDirectory();

        if (!xUtils.buildDirectory(dir)) {
            throw new IOException("Could not create directory: " + dir);
        }

        File versionFile = new File(dir, "version");
        if (!versionFile.exists()) {
            return null;
        } else {
            DataInputStream dis = new DataInputStream(new FileInputStream(versionFile));
            String version = dis.readUTF();
            dis.close();
            return version;
        }
    }

    void updateVersion(String version) throws Exception {
        File dir = xUtils.getDirectory();
        File versionFile = new File(dir, "version");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(versionFile));
        dos.writeUTF(version);
        dos.close();
    }

    void unpackClient(URL url, File targetDir) throws IOException {
        if (!xUtils.buildDirectory(targetDir)) {
            throw new IOException("Could not create directory: " + targetDir);
        }

        URLConnection urlconnection = url.openConnection();
        this.totalDownload = urlconnection.getContentLength() / 1024;
        this.onePercent = (double) (this.totalDownload / 100);
        BufferedInputStream in = new BufferedInputStream(url.openStream(), 1024);
        File zip = File.createTempFile("client", ".zip", targetDir);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(zip));
        this.copyInputStream(in, out, false);
        out.close();
        xUtils.unpackArchive(zip, targetDir);
    }

    void unpackLauncher(URL url, File target) throws IOException {
        URLConnection urlconnection = url.openConnection();
        this.totalDownload = urlconnection.getContentLength() / 1024;
        this.onePercent = (double) (this.totalDownload / 100);
        BufferedInputStream in = new BufferedInputStream(url.openStream(), 1024);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
        this.copyInputStream(in, out, false);
        out.close();
    }

    void copyInputStream(InputStream in, OutputStream out, boolean zip) throws IOException {
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);

        for (int size = 0; len >= 0; len = in.read(buffer)) {
            if (!zip) {
                size += len;
                this.checkUpdateBar(size / 1024);
            }
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    private void disableButtons() {
        xButton.loadButtons();

        for (int i = 0; i < xThemeSettings.BUTTONS.length; ++i) {
            xButton button = xButton.getButtons()[i];

            if (button.getId() == xButton.UPDATE_ID || button.getId() == xButton.RAM_ID) {
                this.theme.buttons[button.getId()].setEnabled(false);
            }
        }
    }

    private enum UpdateType {
        client {
            @Override
            public String getParameter() {
                return "version";
            }
        },

        launcher {
            @Override
            public String getParameter() {
                return "launcher";
            }
        };

        public abstract String getParameter();
    }
}
