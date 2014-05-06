package ru.xeroxp.launcher;

import ru.xeroxp.launcher.config.xSettings;
import ru.xeroxp.launcher.gui.elements.xButton;
import ru.xeroxp.launcher.gui.elements.xServer;
import ru.xeroxp.launcher.gui.xTheme;
import ru.xeroxp.launcher.misc.xConfig;
import ru.xeroxp.launcher.misc.xDebug;
import ru.xeroxp.launcher.utils.xFileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class xUpdater {
    private final xTheme theme;

    public xUpdater(xTheme theme) {
        this.theme = theme;
        this.theme.progressBar.init();
        this.checkLauncherUpdate();
        this.checkClientsUpdate(false);
    }

    private static void deleteTempFiles() throws IOException {
        File[] files = xFileUtils.getRootDirectory().listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".tmp") && !file.delete()) {
                throw new IOException("Failed to delete file: " + file.getAbsolutePath());
            }
        }
    }

    private static void deleteClientFiles() throws IOException {
        boolean one = false;

        deleteTempFiles();
        xServer.loadServers();

        for (xServer server : xServer.getServers()) {
            String path = null;
            if (!server.getFolder().isEmpty()) {
                path = xFileUtils.getRootDirectory().getPath() + File.separator + server.getFolder();
            } else if (!one) {
                one = true;
                path = xFileUtils.getRootDirectory().getPath();
            }

            if (path != null) {
                File rootDir = new File(path);

                String[] fileList = rootDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File mdf, String name) {
                        return name.contains("mods") || name.contains("bin") || name.contains("lib") || name.contains("assets");
                    }
                });

                for (String file : fileList) {
                    xFileUtils.delete(new File(path + File.separator + file));
                }
            }
        }
    }

    private void checkLauncherUpdate() {
        try {
            String latestVersion = this.getLatestVersions(UpdateType.launcher);

            if (latestVersion != null) {
                String getVersion = xMain.getVersion();

                if (!latestVersion.equals(getVersion)) {
                    this.theme.setLockAuth(true);
                    this.updateLauncher(latestVersion);
                }

                this.theme.progressBar.update();
                this.theme.setLockAuth(false);
            }
        } catch (Exception e) {
            xDebug.errorMessage("Failed check launcher update: " + e.getMessage());
        }
    }

    private void updateLauncher(String version) {
        disableButtons();

        try {
            File runningLauncher = new File(xUpdater.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            if (runningLauncher.getPath().endsWith(".jar")) {
                xFileUtils.downloadFile(new URL(xSettings.DOWN_LAUNCHER_LINK + xSettings.LAUNCHER_FILE_NAME + ".jar"), runningLauncher);
            } else if (runningLauncher.getPath().endsWith(".exe")) {
                xFileUtils.downloadFile(new URL(xSettings.DOWN_LAUNCHER_LINK + xSettings.LAUNCHER_FILE_NAME + ".exe"), runningLauncher);
            }

            xMain.setVersion(version);
            xMain.restart();
        } catch (URISyntaxException e) {
            xDebug.errorMessage("Failed to find launcher path: " + e.getMessage());
        } catch (IOException e) {
            xDebug.errorMessage("Failed update launcher:" + e.getMessage());
        }
    }

    private void updateClient(String client, String version, boolean updateConfigs) {
        this.disableButtons();

        try {
            this.downloadClient(client);

            if (updateConfigs) {
                this.downloadConfig(client);
            }

            this.updateVersion(client, version);
        } catch (IOException e) {
            xDebug.errorMessage("Client update failed: " + e.getMessage());
        }

        for (xButton button : xButton.getButtons()) {
            if (button.getId() == xButton.RAM_ID || button.getId() == xButton.AUTH_ID) {
                this.theme.buttons[button.getId()].setEnabled(true);
                break;
            }
        }
    }

    private String getLatestVersions(UpdateType type) {
        try {
            URLConnection getVer = new URL(xSettings.MAIN_INFO_FILE + "?action=" + type.getParameter()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(getVer.getInputStream()));
            String inputLine = in.readLine();
            in.close();

            return inputLine;
        } catch (Exception e) {
            xDebug.errorMessage("Failed check " + type.name() + " version: " + e.getMessage());
        }

        return null;
    }

    private Map<String, String> parseVersions(String inputVersions) {
        if (inputVersions == null) {
            throw new IllegalArgumentException("Versions is null!");
        }

        String[] versions = inputVersions.split(", ");
        Map<String, String> result = new HashMap<String, String>();

        for (String version : versions) {
            String[] client = version.split(":");
            result.put(client[0], client[1]);
        }

        return result;
    }

    public void checkClientsUpdate(boolean forceUpdate) {
        try {
            Map<String, String> latestVersions = this.parseVersions(this.getLatestVersions(UpdateType.client));

            for (Map.Entry<String, String> version : latestVersions.entrySet()) {
                String client = version.getKey();
                String latestVersion = version.getValue();
                String currentVersion = this.getVersion(client);

                if (currentVersion == null) {
                    this.theme.setLockAuth(true);
                    this.updateClient(client, latestVersion, true);
                    this.theme.setLockAuth(false);
                    return;
                }

                if (!latestVersion.equals(currentVersion) || forceUpdate) {
                    this.theme.setLockAuth(true);
                    this.updateClient(client, latestVersion, forceUpdate);
                }

                this.theme.progressBar.update();
                this.theme.setLockAuth(false);
            }
        } catch (Exception e) {
            xDebug.errorMessage("Failed check client update:\n" + e.toString());
            e.printStackTrace();
        }
    }

    private String getVersion(String client) throws IOException {
        xConfig config = new xConfig(xConfig.VERSIONS);
        return config.get("version." + client);
    }

    private void updateVersion(String client, String version) throws IOException {
        xConfig config = new xConfig(xConfig.VERSIONS);
        config.set("version." + client, version);
    }

    private void downloadClient(String client) throws IOException {
        File targetDir = new File(xFileUtils.getRootDirectory().getPath() + File.separator + client);
        xFileUtils.buildDirectory(targetDir);

        File clientZip = File.createTempFile("client", ".tmp", targetDir);
        URL url = new URL(xSettings.DOWN_CLIENT_LINK + client + "/client.zip");
        xFileUtils.downloadFile(url, clientZip);
        deleteClientFiles();
        xFileUtils.unZip(clientZip, targetDir);
    }

    private void downloadConfig(String client) throws IOException {
        File targetDir = new File(xFileUtils.getRootDirectory().getPath() + File.separator + client);
        xFileUtils.buildDirectory(targetDir);

        File configZip = File.createTempFile("config", ".tmp", targetDir);
        URL url = new URL(xSettings.DOWN_CLIENT_LINK + client + "/config.zip");
        xFileUtils.downloadFile(url, configZip);
        xFileUtils.unZip(configZip, targetDir);
    }

    private void disableButtons() {
        xButton.loadButtons();

        for (int i = 0; i < xButton.getButtons().length; ++i) {
            xButton button = xButton.getButtons()[i];

            this.theme.buttons[button.getId()].setEnabled(false);
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
